package com.meteor.wechatbc.impl.fileupload.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class BaseResponse {
    private int Ret;
    private String ErrMsg;
}
