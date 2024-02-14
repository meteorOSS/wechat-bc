package org.example;

import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.launch.Launch;
import org.apache.logging.log4j.LogManager;

public class CreateWeChatClient implements Launch {
    public static void main(String[] args) {
        WeChatClient weChatClient = new CreateWeChatClient().createWeChatClient();
        weChatClient.start();
        weChatClient.loop();
    }

    public WeChatClient createWeChatClient(){
        return login(LogManager.getLogger("wechat-client"));
    }
}
