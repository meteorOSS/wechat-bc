package com.meteor.wechatbc.impl.model.message;


import com.meteor.wechatbc.entitiy.message.Message;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 封装二维码收款到账的消息
 */
public class PayMessage extends Message {

    @Getter
    private double amount; // 金额

    @Getter
    private String notes; // 备注

    /*
    提取金额信息
     */
    public void extractAmount(){
        String content = super.getContent();
        String reg = "微信支付收款([-+]?[0-9]*\\.?[0-9]+)元";
        Pattern compile = Pattern.compile(reg);
        Matcher matcher = compile.matcher(content);
        while (matcher.find()){
            this.amount = Double.parseDouble(matcher.group(1));
            break;
        }
    }


    /**
     * 提取备注信息
     */
    public void extractNotes(){
        String content = super.getContent();
        String reg = "<br/>付款方备注(.+?)<br/>";
        Pattern compile = Pattern.compile(reg);
        Matcher matcher = compile.matcher(content);
        while (matcher.find()){
            this.notes = matcher.group(1);
            break;
        }
    }

    @Override
    public String getContent() {
        return String.format("(收款信息 金额: %s 备注: %s)",amount,notes);
    }
}
