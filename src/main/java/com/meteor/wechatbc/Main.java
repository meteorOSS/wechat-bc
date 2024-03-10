package com.meteor.wechatbc;


import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.launch.login.DefaultPrintQRCodeCallBack;
import com.meteor.wechatbc.plugin.PluginClassLoader;
import com.meteor.wechatbc.util.VersionCheck;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;


public class Main{

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


    private void infoLogo(){
        System.out.println("\n" +
                " __          __   _____ _           _     ____   _____ \n" +
                " \\ \\        / /  / ____| |         | |   |  _ \\ / ____|\n" +
                "  \\ \\  /\\  / /__| |    | |__   __ _| |_  | |_) | |     \n" +
                "   \\ \\/  \\/ / _ \\ |    | '_ \\ / _` | __| |  _ <| |     \n" +
                "    \\  /\\  /  __/ |____| | | | (_| | |_  | |_) | |____ \n" +
                "     \\/  \\/ \\___|\\_____|_| |_|\\__,_|\\__| |____/ \\_____|\n" +
                "                                                       \n" +
                "                                                       \n");
        System.out.println("开源仓库: https://github.com/meteorOSS/WeChatBc");
        System.out.println("如果对你有帮助的话，请帮忙点个Star哦");
        VersionCheck.check("meteorOSS","wechat-bc");
        System.out.println("wechatbc 交流群: 653440235");
    }

    public int start(){
        this.infoLogo();
        Thread.currentThread().setName(MAIN_THREAD_NAME);
        weChatClient = new WeChatClient();
        // 登录
        weChatClient.login(new DefaultPrintQRCodeCallBack());
        try {
            // 挂起控制台
            weChatClient.loop();
        }finally {
            weChatClient.stop();
        }
        return 0;
    }
}
