package com.meteor.wechatbc;

import org.slf4j.Logger;

public interface WeChatCore {

    HttpAPI getHttpAPI();

    String getAPIVersion();

    Logger getLogger();

}
