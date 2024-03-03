package com.github.louislou2.jprotobuf.debugger;

import com.github.louislou2.jprotobuf.model.TypeInfo;

import java.util.ArrayList;
import java.util.List;

public class AnalyserTest {
    public static void main(String[]args){
        test2();
    }
    private static void parseFields(List<String> psiFields){
        for(final var fieldStr: psiFields){
            List<String>type=new ArrayList<>();
            int lastLtPos=-1;
            int LtPos= fieldStr.indexOf('<');
            while(LtPos!=-1){
                type.add(fieldStr.substring(lastLtPos+1,LtPos));
                lastLtPos=LtPos;
                LtPos= fieldStr.indexOf('<',LtPos+1);
            }
            if(lastLtPos==-1)
                type.add(fieldStr);
            else{
                int firstGtPos=fieldStr.indexOf('>',lastLtPos);
                type.add(fieldStr.substring(lastLtPos+1,firstGtPos));
            }
            System.out.println(type);
        }
    }
    // 复杂度为O(nlogn),暂时没想到更高效率的方法
    private static TypeInfo getTypeInfo(String typeStr){
        int len=typeStr.length();
        TypeInfo info=new TypeInfo();
        int LtPos=typeStr.indexOf('<');
        if(LtPos==-1){
            info.setAndNoParams(typeStr);
            return info;
        }
        var type=typeStr.substring(0,LtPos);
        info.setTypeName(type);
        List<TypeInfo> typeParams=new ArrayList<>();
        if(hasOneParam(type)){// 这个if删去不影响后续结果，但是这里预先判断一下，省得
            var paramStr=typeStr.substring(LtPos+1,len-1);
            typeParams.add(getTypeInfo(paramStr));
            info.setTypeParams(typeParams);
            return info;
        }
        int count=0;
        boolean haveMeet=false;
        int begin=LtPos+1;
        int paraEnd=len-1;
        for(int i=begin;i<paraEnd;++i){
            if(count==0&&typeStr.charAt(i)==','){
                var paramStr=typeStr.substring(begin,i);
                typeParams.add(getTypeInfo(paramStr));
                count=0;
                begin=i+1;
                continue;
            }
            if(typeStr.charAt(i)=='<') ++count;
            else if(typeStr.charAt(i)=='>')--count;
        }
        var lastParam=typeStr.substring(begin,paraEnd);
        typeParams.add(getTypeInfo(lastParam));
        info.setTypeParams(typeParams);
        return info;
    }
    private static boolean hasOneParam(String type){
        //目前先这么着吧
        return type.equals("List")||type.equals("Set");
    }
    public static void test(){
        List<String>psiFields=List.of(
                "Short","List<Map<Integer>>","List<Byte>"
        );
        parseFields(psiFields);
    }
    public static void test2(){
        String infoStr="Map<Map<List<a>,b>,Map<Set<a>,List<b>>>";
        //String infoStr="Map<Integer,List<String>>";
        TypeInfo info=getTypeInfo(infoStr);
        System.out.println(info);
    }
}
