package com.meteor.wechatbc;


import com.meteor.wechatbc.entitiy.contact.Contact;
import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.launch.Launch;
import com.meteor.wechatbc.plugin.Plugin;
import com.meteor.wechatbc.plugin.PluginClassLoader;
import com.meteor.wechatbc.scheduler.WeChatRunnable;
import com.meteor.wechatbc.util.BaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;


public class Main implements Launch {

    private static final String MAIN_THREAD_NAME = "WECHATBC_MAIN";
    private Logger logger = LogManager.getLogger(MAIN_THREAD_NAME);

    public static void main(String[] args) {
        Thread.currentThread().setContextClassLoader(new PluginClassLoader(new URL[0], Main.class.getClassLoader()));
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
            System.out.println("getContactByNickName");
            Contact contact = weChatClient.getContactManager().getContactByNickName("zzzsh");
            System.out.println(contact.toString());
            weChatClient.loop();
        }finally {
            weChatClient.stop();
        }

        return 0;
    }


}
