package com.meteor.wechatbc.impl.contact;

public enum RetrievalType {

    NICK_NAME("根据昵称查询"),USER_NAME("根据username查询");
    private String comment;

    RetrievalType(String comment){
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
