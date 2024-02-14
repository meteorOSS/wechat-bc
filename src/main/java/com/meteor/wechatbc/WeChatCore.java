package com.meteor.wechatbc;

import org.apache.logging.log4j.Logger;

public interface WeChatCore {

    HttpAPI getHttpAPI();

    String getAPIVersion();

    Logger getLogger();

}
