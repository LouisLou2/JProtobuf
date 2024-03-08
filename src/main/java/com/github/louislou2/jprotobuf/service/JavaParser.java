package com.github.louislou2.jprotobuf.service;

import com.github.louislou2.jprotobuf.constant.FileTypeEnum;
import com.github.louislou2.jprotobuf.model.CheckResult;
import com.github.louislou2.jprotobuf.model.TypeKind;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;

import java.util.*;

public class JavaParser {
    private static Map<String, String> typeMap;
    private static final Set<String> boxTypes;
    private static final int lastNameInd=10;
    private static final Set<String>baseTypes;
    private static final String list="java.util.List";
    private static final String set="java.util.Set";
    private static final String vector="java.util.Vector";
    private static final String map="java.util.Map";
    private static final int BASE=0; // 基本数据类型
    private static final int BOX=1; // 基本数据类型包装类
    private static final int DEF =2; // 自定义类
    private static final int SELF=3;
    private static final int ELE1=4;// 包含一个类型参数的Collection
    private static final int KV=5; //键值对集合，各种Table
    
    static{
        typeMap=new HashMap<>();//因为不会出现多个线程同时修改该map，不用担心线程安全性
        typeMap.put("int", "int32");
        typeMap.put("long", "int64");
        typeMap.put("boolean", "bool");
        typeMap.put("string", "string");//是的这里java中应该是String,这里暂且将java.lang.String视作string的包装类吧
        typeMap.put("float", "float");
        typeMap.put("double", "double");
        typeMap.put("byte", "int32");
        typeMap.put("short", "int32");
        typeMap.put("char", "int32");
        //typeMap.put("byte[]", "bytes"); 这中原始数组还没有制定方案
        boxTypes=new HashSet<>();
        boxTypes.addAll(List.of(
                "java.lang.Integer",
                "java.lang.Character",
                "java.lang.Long",
                "java.lang.Boolean",
                "java.lang.Float",
                "java.lang.Double",
                "java.lang.Byte",
                "java.lang.Short",
                "java.lang.String"
        ));
        baseTypes=new HashSet<>();
        baseTypes.addAll(List.of(
                "int","long","boolean","String","float","double","byte","short","char"
        ));
    }
    private static boolean isStructured(int kind){
        return kind==ELE1||kind==KV;
    }
    private static boolean isBasic(int kind){
        return kind==BASE||kind==BOX;
    }
    /**
     * 包装类转基本类，例如java.lang.Integer->int
     * 注意使用此类前已经确定他是一个严格书写正确的包装类的全类名，也就是说，属于boxTypes集合
     * @param canonicalName
     * @return
     */
    private static String getUnboxingType(String canonicalName){
        if(canonicalName.charAt(lastNameInd)=='I')return "int";
        if(canonicalName.charAt(lastNameInd)=='C')return "char";
        return Character.toLowerCase(canonicalName.charAt(lastNameInd)) + canonicalName.substring(lastNameInd+1);
    }

    /**
     * 此方法仅仅用来处理[基本数据类型][包装类]以及[用户自定义类][此类本身]
     * 其他均不服务
     * @param type
     * @param kind
     * @return
     */
    private static String getSimpleProtoType(PsiType type,int kind,PsiClass wrapClass){
        String canonicalName=type.getCanonicalText();
        assert kind==BASE||kind==BOX||kind== DEF||kind==SELF;
        if(kind== DEF)
            return getMessageName(type.getPresentableText());
        if(kind== SELF)
            return wrapClass.getName();
        String name=kind==BASE?canonicalName:getUnboxingType(canonicalName);
        return typeMap.get(name);
    }
    /**
     * 0：基本数据类型
     * 1：包装类
     * 2：用户自定义类
     * 3：线性表或集合
     * 4：键值对集合(Map)
     * TODO:这里也许可以考虑将他们做成常量,一会再说吧
     * @return
     */
    public static int getKind(PsiType type,PsiClass wrapClass){
        // 注意是PsiPrimitiveType不是JavaPrimitiveType
        if(type instanceof PsiPrimitiveType){
            //表明是基本类型
            return BASE;
        }
        if(boxTypes.contains(type.getCanonicalText())){
            // 表明是基本类型的包装类
            return BOX;
        }
        PsiClassType classType=(PsiClassType)type;
        PsiClass classInfo=classType.resolve();
        assert classInfo != null;
        String outerCanon=classInfo.getQualifiedName();
        assert outerCanon != null;
        if(outerCanon.equals(wrapClass.getQualifiedName()))
            return SELF;
        if(outerCanon.equals(list)|| outerCanon.equals(vector) || outerCanon.equals(set))
            return ELE1;
        if(outerCanon.equals(map))
            return KV;
        /*
        说明可能是用户自定义类，但是这里也有Java其他数据结构的嫌疑，
        因为在上面判断的数据结构类型只有几个.这里姑且当做就是用户自定义类吧
        TODO: 进一步判断
         */
        return DEF;
    }

    /**
     * 在已知该PsiTyte只能是BOX或者SELF的情况下调用
     * @param type
     * @return
     */
    //public static String getStrKind4BoxAndSelf(PsiType type){
    //    int kind = boxTypes.contains(type.getCanonicalText())?BOX:DEF;
    //    return getSimpleProtoType(type.getCanonicalText(),kind);
    //}

    /**
     * 在已知该PsiTyte只能是BOX或者SELF的情况下调用
     * @param type
     * @return
     */
    public static int getKind4BoxAndDef(PsiType type){
        return boxTypes.contains(type.getCanonicalText())?BOX: DEF;
    }

    /**
     * 这个方法为了检查此自定义类，即DEF类
     * 并将必要的结果
     * @param type
     */
    public static TypeKind forDefCheck(PsiType type,PsiClass wrapClass) throws IllegalArgumentException{
        PsiClass aclass=PsiUtil.resolveClassInType(type);
        // TODO:在很长的泛型参数链中检测循环依赖的问题确实没解决
        String defineLoca=PathManager.getDefineLocation(aclass);
        if(PathManager.inSpecifiedDirWithJPath(defineLoca, FileTypeEnum.POJO)){
            if(!PathManager.inSpecifiedDirWithJPath(defineLoca,FileTypeEnum.PROTO))
                // TODO:说明需要的.proto文件不存在，但是对应的java类在pojo文件夹，这里就需要放下手头的活，进行解析java为.proto
                ProtoGenerator.writeProtoFile(aclass,aclass.getProject(),PathManager.protoDir+"/"+PathManager.getRelaCorDir(defineLoca));
        }else{
            // 能到这里，需要的.proto文件不存在，说明引用的类不在pojo文件夹，这是不可接受的错误
            // 终止一切操作，抛出异常，异常会层层向上传递
            throw new IllegalArgumentException("错误原因:引用了不在pojo文件夹的类");
        }
        String selfStr =PathManager.getRelaCorProtoPath(defineLoca);
        return TypeKind.makeTypeKind4Define(DEF,selfStr);
    }

    /**
     * invoked only when atype is ELE1
     * @param atype
     * @param wrapClass
     */
    public static TypeKind forELE1Check(PsiType atype,PsiClass wrapClass){
        PsiClassType classType=(PsiClassType)atype;
        PsiType[] typeParams=classType.getParameters();
        assert typeParams.length==1;
        int kind=getKind(typeParams[0],wrapClass);
        assert kind== DEF||kind== SELF;
        if(kind==SELF){
            return TypeKind.makeTypeKind4ELE1(ELE1,TypeKind.makeTypeKind4Basic(kind));
        }
        TypeKind param=forDefCheck(atype,wrapClass);
        return TypeKind.makeTypeKind4ELE1(ELE1,param);
    }
    public static TypeKind forKVCheck(PsiType atype,PsiClass wrapClass){
        PsiClassType classType=(PsiClassType)atype;
        PsiType[] typeParams=classType.getParameters();
        assert typeParams.length==2;
        int kind1=getKind(typeParams[0],wrapClass);
        int kind2=getKind(typeParams[1],wrapClass);
        assert (kind1 == DEF||kind1==SELF)&&(kind2 == DEF||kind2==SELF);
        TypeKind param1;
        TypeKind param2;
        if(kind1==SELF) param1=TypeKind.makeTypeKind4Basic(SELF);
        else param1= forDefCheck(atype,wrapClass);
        if(kind1==SELF) param2=TypeKind.makeTypeKind4Basic(SELF);
        else param2= forDefCheck(atype,wrapClass);
        return TypeKind.makeTypeKind4KV(KV,new Pair<>(param1,param2));
    }

    /**
     * 要在CodeAnalyser.hasError，先利用此方法检查1.是否存在语法错误，2.同时也可以知道是否引用了未定义类
     * 目的是检查用户自定义类的引用关系，
     * 如果发现了引用了一个类，在pojoDir但是不在protoDir，应该连环地，先解析那个类
     * 如果发现引用一些类，不在pojoDir,应该提醒这是不行的，或者给出选择是否要进行移动
     * @param aclass
     * @return
     */
    public static CheckResult kindPreCheck(PsiClass aclass) throws IllegalArgumentException{
        PsiField[] fields=aclass.getFields();
        CheckResult res=new CheckResult(fields.length);
        for(PsiField field:fields){
            PsiType type=field.getType();
            int kind=getKind(type,aclass);
            switch (kind){
                case BASE,BOX,SELF->{
                    res.addTypeKind(TypeKind.makeTypeKind4Basic(kind));
                }
                case DEF -> {
                    // TODO:开始检查
                    res.addTypeKind(forDefCheck(type,aclass));
                }
                case ELE1 -> {
                    res.addTypeKind(forELE1Check(type,aclass));
                }
                case KV->{
                    res.addTypeKind(forKVCheck(type,aclass));
                }
                default->{
                    throw new IllegalArgumentException("error java type");
                }
            }
        }
        return res;
    }
    public static String getProtoString(PsiClass aclass) throws IllegalArgumentException{
        CheckResult res= kindPreCheck(aclass);
        
        String className=aclass.getName();
        String messageName=getMessageName(className);
        String outerClassName=getOuterClassName(className);
        StringBuilder header = new StringBuilder();
        StringBuilder content= new StringBuilder();
        header.append("syntax = \"proto3\";\n\n").append("\noption java_outer_classname = ").append('\"').append(outerClassName).append("\";\n\n");
        content.append("message ").append(messageName).append(" {");
        var fields=aclass.getFields();
        parseStr4Nesting(fields,res,aclass,header,content,1);
        content.append("\n}");
        return header +content.toString();
    }
    public static void parseStr4Nesting(PsiField[] fields,CheckResult res,PsiClass wrapClass,StringBuilder importBuilder, StringBuilder contentBuilder, int orderStart){
        List<TypeKind>kinds=res.getKinds();
        assert fields.length==kinds.size();
        for(int i=0;i<fields.length;++i){
            contentBuilder.append("\n    ");
            int kind=kinds.get(i).getKind();
            String fieldName=fields[i].getName();
            PsiType type=fields[i].getType();
            switch (kind){
                case BASE,BOX,SELF,DEF->{
                    String baseProtoType=getSimpleProtoType(type,kind,wrapClass);
                    contentBuilder.append(baseProtoType).append(" ").append(fieldName);
                    if(kind==DEF) importBuilder.append("import \"").append(kinds.get(i).getDefLoca()).append("\";\n");
                }
                case ELE1->{
                    TypeKind param=kinds.get(i).getParam();
                    contentBuilder.append("repeated ");
                    contentBuilder.append(getSimpleProtoType(type,kind,wrapClass)).append(" ").append(fieldName);
                    if(param.getKind()==DEF) importBuilder.append("import \"").append(param.getDefLoca()).append("\";\n");
                }
                case KV->{
                    Pair<TypeKind,TypeKind> params=kinds.get(i).getParams();
                    contentBuilder.append("map<");
                    contentBuilder.append(getSimpleProtoType(type,kind,wrapClass)).append(", ");
                    contentBuilder.append(getSimpleProtoType(type,kind,wrapClass)).append("> ").append(fieldName);
                    if(params.getFirst().getKind()==DEF) importBuilder.append("import \"").append(params.getFirst().getDefLoca()).append("\";\n");
                    if(params.getSecond().getKind()==DEF) importBuilder.append("import \"").append(params.getSecond().getDefLoca()).append("\";\n");
                }
            }
            contentBuilder.append(" = ").append(orderStart+i).append(';');
        }
    }

    /**
     * @param className
     * @return .proto文件中java_outer_classname的名字，目前还没有书写用户如何指定，先用以下策略
     */
    public static String getOuterClassName(String className){
        return className+"Proto";
    }

    /**
     * .proto文件中message的名字，目前还没有书写用户如何指定，先用以下策略
     * @param className
     * @return
     */
    public static String getMessageName(String className){
        return className+"Pro";
    }

    /**
     * .proto文件的名字，目前还没有书写用户如何指定，先用以下策略
     * @param className
     * @return
     */
    public static String getProtoFileName(String className){
        return className+".proto";
    }
}
