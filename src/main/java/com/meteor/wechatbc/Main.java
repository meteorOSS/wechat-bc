package com.meteor.wechatbc;


import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.launch.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.net.URL;


public class Main implements Launch {

    private static final String MAIN_THREAD_NAME = "WECHATBC_MAIN";
    private Logger logger = LogManager.getLogger(MAIN_THREAD_NAME);

    public static void main(String[] args) {

        URL log4Jresource = Main.class.getResource("/log4j2.xml");
        System.exit(main0());
    }

    public static int main0() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(weChatClient!=null){
                weChatClient.stop();
            }
        }));
        return new Main().start();
    }

    public static WeChatClient weChatClient = null;

    public int start(){


        Thread.currentThread().setName(MAIN_THREAD_NAME);

        weChatClient = login(logger);

        weChatClient.start();
        weChatClient.initPluginManager();

        try {
            weChatClient.loop();
        }finally {
            weChatClient.stop();
        }
        return 0;
    }

}
