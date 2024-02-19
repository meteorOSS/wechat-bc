package com.meteor.wechatbc;

import com.meteor.wechatbc.impl.HttpAPI;
import org.apache.logging.log4j.Logger;

public interface WeChatCore {

    HttpAPI getHttpAPI();

    String getAPIVersion();

    Logger getLogger();

}
