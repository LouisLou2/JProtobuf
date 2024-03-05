package com.github.louislou2.jprotobuf.model;

import java.util.*;

public class CheckResult {
    private int num;
    private List<TypeKind>kinds;
    public CheckResult(int num){
        this.num=num;
        kinds=new ArrayList<>(num);
    }
    public void addTypeKind(TypeKind kind){
        assert kinds.size()<num;
        kinds.add(kind);
    }
}
