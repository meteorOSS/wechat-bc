package com.meteor.wechatbc.launch;

import com.meteor.wechatbc.entitiy.session.BaseRequest;
import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.launch.login.WeChatLogin;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.InputStream;

/**
 * 在控制台启动之前进行扫码登录流程
 */
public interface Launch {

    default void infoLogo(){
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

    }

    /**
     * 登录
     * @param logger
     * @return
     */
    default WeChatClient login(Logger logger){

        infoLogo();

        WeChatLogin weChatLogin = new WeChatLogin(logger);

        weChatLogin.login();

        BaseRequest loginInfo = weChatLogin.getLoginInfo();

        WeChatClient weChatClient = new WeChatClient(logger);

        weChatClient.initWeChatCore(loginInfo);

        return weChatClient;
    }


}
