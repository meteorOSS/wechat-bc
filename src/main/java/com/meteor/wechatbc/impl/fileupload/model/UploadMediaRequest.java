package com.meteor.wechatbc.impl.fileupload.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.meteor.wechatbc.entitiy.session.BaseRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadMediaRequest {
    @JSONField(name = "UploadType")
    private int uploadType = 2;
    @JSONField(name = "BaseRequest")
    private BaseRequest baseRequest;
    @JSONField(name = "ClientMediaId")
    private long clientMediaId;
    @JSONField(name = "TotalLen")
    private long totalLen;
    @JSONField(name = "StartPos")
    private long startPos = 0;
    @JSONField(name = "DataLen")
    private long dataLen;
    @JSONField(name = "MediaType")
    private long mediaType = 4;
    @JSONField(name = "FromUserName")
    private String fromUserName;
    @JSONField(name = "ToUserName")
    private String toUserName;
    @JSONField(name = "FileMd5")
    private String filedMD5;

}
