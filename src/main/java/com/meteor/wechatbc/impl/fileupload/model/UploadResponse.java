package com.meteor.wechatbc.impl.fileupload.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class UploadResponse {
    @JSONField(name = "BaseResponse")
    private BaseResponse baseResponse;

    @JSONField(name = "MediaId")
    private String mediaId;

    @JSONField(name = "StartPos")
    private long startPos;

    @JSONField(name = "CDNThumbImgHeight")
    private int cdnThumbImgHeight;

    @JSONField(name = "CDNThumbImgWidth")
    private int cdnThumbImgWidth;

    @JSONField(name = "EncryFileName")
    private String encryFileName;

    /**
     * 是否完整
     */
    public boolean isFull(){
        return getBaseResponse().getRet() == 0;
    }
}
