package com.meteor.wechatbc.entitiy.contact;

import com.meteor.wechatbc.entitiy.message.Message;

public enum ContactType {

    GROUP("群"),FRIEND("好友"),GG("公众号"),CONTACT("用户");

    private String comment;
    ContactType(String comment){
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public static ContactType from(Contact contact){
        if(contact.getUserName().startsWith("@@")) return GROUP;
        else if(contact.getUserName().startsWith("@")) return FRIEND;
        else if(contact.getVerifyFlag() == 8 || contact.getVerifyFlag() == 24 || contact.getVerifyFlag() == 136) return GG;
        return CONTACT;
    }
}
