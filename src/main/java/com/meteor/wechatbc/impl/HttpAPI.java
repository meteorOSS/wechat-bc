package com.meteor.wechatbc.impl;

import com.alibaba.fastjson2.JSONObject;
import com.meteor.wechatbc.entitiy.contact.GetBatchContact;
import com.meteor.wechatbc.entitiy.message.SentMessage;
import com.meteor.wechatbc.entitiy.synccheck.SyncCheckResponse;

import java.io.File;
import java.util.List;

public interface HttpAPI {

    /**
     * 初始化
     */
    void init();

    /**
     * 初始化微信接口
     */
    void initWeChat();

    /**
     * 检查新消息
     */
    SyncCheckResponse syncCheck();

    /**
     * 获取最新消息
     */
    JSONObject getMessage();

    /**
     * 取得联系人列表
     */
    JSONObject getContact();

    /**
     * 批量获取联系人详情，人或群均可。获取群详情主要是获取群内联系人列表。获取人详情主要是获取群内的某个人的详细信息
     *
     * @param queryContactList
     * @return
     */
    JSONObject batchGetContactDetail(List<GetBatchContact> queryContactList);

    /**
     * 发送消息
     */
    SentMessage sendMessage(String toUserName, String content);

    /**
     * 获得消息图片
     */
    byte[] getMsgImage(String msgId);

    void revoke(SentMessage sentMessage);

    /**
     * 发送图片
     *
     * @return
     */
    SentMessage sendImage(String toUserName, File file);

    /**
     * 发送视频
     *
     * @return
     */
    SentMessage sendVideo(String toUserName, File file);

    /**
     * 获取用户头像
     *
     * @return
     */
    File getIcon(String userName);

    /**
     * 获取视频消息的响应
     */
    byte[] getVideo(long msgId);

    /**
     * 获取语音消息
     * @param msgId
     * @return
     */
    byte[] getVoice(long msgId);
}
