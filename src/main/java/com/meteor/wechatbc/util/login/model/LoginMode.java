package com.meteor.wechatbc.util.login.model;

public enum LoginMode{
        LOGIN_MODE408(401,"等待扫码中..."),
        LOGIN_MODE200(200,"已确认登陆"),
        LOGIN_MODE201(201,"已扫描二维码，请确认登陆");
        private Integer code;
        private String msg;
        LoginMode(Integer code,String msg){
            this.code = code;
            this.msg = msg;
        }
        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }