package com.meteor.wechatbc.impl.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.meteor.wechatbc.entitiy.session.BaseRequest;
import com.meteor.wechatbc.entitiy.session.SyncKey;
import lombok.Data;
import lombok.ToString;

import java.io.*;

/**
 * 会话缓存，用于维持与微信链接
 */
@Data
@ToString
public class Session implements Serializable {
    @JSONField(name = "BaseRequest")
    BaseRequest baseRequest;

    @JSONField(serialize = false)
    WxInitInfo wxInitInfo;

    // 作为同步消息的synckey，不参与序列化
    SyncKey checkSyncKey;

    // 获取消息的SyncKey
    @JSONField(name = "SyncKey")
    SyncKey syncKey;

    public void setCheckSyncKey(SyncKey checkSyncKey) {
        this.checkSyncKey = checkSyncKey;
    }

    public void setSyncKey(SyncKey syncKey) {
        this.syncKey = syncKey;
    }

    public void saveHotLoginData(File file){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
