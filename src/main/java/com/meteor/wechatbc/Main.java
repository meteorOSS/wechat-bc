package com.meteor.wechatbc;


import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.launch.Launch;
import com.meteor.wechatbc.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main implements Launch {
    private static final String MAIN_THREAD_NAME = "WECHATBC_MAIN";
    private Logger logger = LoggerFactory.getLogger(MAIN_THREAD_NAME);

    public static void main(String[] args) {
        System.exit(main0());
    }

    public static int main0() {
        return new Main().start();
    }

    public int start(){

        Thread.currentThread().setName(MAIN_THREAD_NAME);

        WeChatClient weChatClient = login(logger);

        weChatClient.start();

        try {
            weChatClient.loop();
        }finally {
            weChatClient.stop();
        }
        return 0;
    }

}
