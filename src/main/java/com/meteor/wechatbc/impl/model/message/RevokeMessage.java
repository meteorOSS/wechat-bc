package com.meteor.wechatbc.impl.model.message;

import com.meteor.wechatbc.entitiy.message.Message;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class RevokeMessage extends Message {
    private Message oldMessage;

    /**
     * 获取撤回的消息ID
     */
    public String getOldMessageID(){
        String pattern = ";msgid&gt;(\\d+)&lt;/";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(getContent());

        if (m.find()) {
            return m.group(1); // 提取括号中的数字部分
        } else {
            return "No oldMsgId found";
        }
    }
}
