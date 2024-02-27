package com.meteor.wechatbc.impl.event.listener;

import com.meteor.wechatbc.entitiy.message.Message;
import com.meteor.wechatbc.event.EventHandler;
import com.meteor.wechatbc.impl.HttpAPI;
import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.impl.event.Listener;
import com.meteor.wechatbc.impl.event.sub.ReceiveMessageEvent;
import com.meteor.wechatbc.impl.model.MsgType;
import com.meteor.wechatbc.impl.model.message.ImageMessage;
import com.meteor.wechatbc.impl.model.message.RevokeMessage;
import com.meteor.wechatbc.impl.model.message.VideoMessage;
import lombok.AllArgsConstructor;

import java.io.File;

@AllArgsConstructor
public class RevokeTestListener implements Listener {
    private WeChatClient weChatClient;

    @EventHandler
    void onMessage(ReceiveMessageEvent receiveMessageEvent){
        Message message = receiveMessageEvent.getMessage();
        if(message.getMsgType()== MsgType.RevokeMsg){

            HttpAPI httpAPI = weChatClient.getWeChatCore().getHttpAPI();


            RevokeMessage revokeMessage = (RevokeMessage) message;
            Message oldMessage = revokeMessage.getOldMessage();

            if(oldMessage.getMsgType()==MsgType.TextMsg){
                httpAPI.sendMessage(message.getFromUserName(),"撤回内容: "+oldMessage.getContent());
            }else if(oldMessage.getMsgType()==MsgType.VideoMsg){
                httpAPI.sendMessage(message.getFromUserName(),"撤回内容为视频消息，正在上传");
                VideoMessage videoMessage = (VideoMessage)oldMessage;
                httpAPI.sendVideo(message.getFromUserName(),videoMessage.saveFile(new File("test.mp4")));
            }

        }
    }
}
