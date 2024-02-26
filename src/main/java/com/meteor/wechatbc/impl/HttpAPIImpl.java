package com.meteor.wechatbc.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.meteor.wechatbc.entitiy.SendMessage;
import com.meteor.wechatbc.entitiy.contact.Contact;
import com.meteor.wechatbc.entitiy.synccheck.SyncCheckResponse;
import com.meteor.wechatbc.entitiy.session.BaseRequest;
import com.meteor.wechatbc.impl.cookie.WeChatCookie;
import com.meteor.wechatbc.impl.fileupload.FileChunkUploader;
import com.meteor.wechatbc.impl.fileupload.model.UploadMediaRequest;
import com.meteor.wechatbc.impl.fileupload.model.UploadResponse;
import com.meteor.wechatbc.impl.interceptor.WeChatInterceptor;
import com.meteor.wechatbc.impl.model.MsgType;
import com.meteor.wechatbc.impl.model.Session;
import com.meteor.wechatbc.impl.model.WxInitInfo;
import com.meteor.wechatbc.util.HttpUrlHelper;
import com.meteor.wechatbc.util.URL;
import lombok.Getter;
import okhttp3.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 微信接口的实现
 */
public class HttpAPIImpl implements HttpAPI {


    private WeChatClient weChatClient;

    @Getter private OkHttpClient okHttpClient;

    private WeChatInterceptor weChatInterceptor;

    private final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    @Getter private final Request BASE_REQUEST = new Request.Builder().addHeader("Content-Type","application/json; charset=UTF-8").url(URL.BASE_URL).build();

    private WeChatCookie weChatCookie;

    public HttpAPIImpl(WeChatClient weChatClient){
        this.weChatClient = weChatClient;
    }

    public void init(){
        this.weChatCookie = new WeChatCookie(weChatClient.getWeChatCore().getSession().getBaseRequest().getInitCookie());
        this.weChatInterceptor = new WeChatInterceptor(weChatClient.getWeChatCore());
        this.okHttpClient = new OkHttpClient.Builder()
                .cookieJar(this.weChatCookie) // cookie处理
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120,TimeUnit.SECONDS)
                .addInterceptor(this.weChatInterceptor) // 拦截器
                .build();
    }

    @Override
    public void initWeChat() {
        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.WXINIT)
                .addQueryParameter("_",String.valueOf(System.currentTimeMillis()))
                .build();

        Request request = BASE_REQUEST.newBuilder().url(httpUrl).post(RequestBody.create(mediaType,JSON.toJSONString(new JSONObject()))).build();

        try(
                Response response = okHttpClient.newCall(request).execute()
        ) {
            String responseToString = response.body().string();
            Session session = weChatClient.getWeChatCore().getSession();
            WxInitInfo wxInitInfo = JSON.parseObject(responseToString, WxInitInfo.class);
            session.setWxInitInfo(wxInitInfo);
            session.setCheckSyncKey(wxInitInfo.getSyncKey());
            session.setSyncKey(wxInitInfo.getSyncKey());
            Contact user = wxInitInfo.getUser();
            weChatClient.getLogger().debug("已初始化微信信息:");
            weChatClient.getLogger().debug("用户信息:");
            weChatClient.getLogger().debug(user.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SyncCheckResponse syncCheck() {
        Session session = weChatClient.getWeChatCore().getSession();
        BaseRequest baseRequest = session.getBaseRequest();
        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.SYNCCHECK)
                .addQueryParameter("r",String.valueOf(System.currentTimeMillis()))
                .addQueryParameter("skey",baseRequest.getSkey())
                .addQueryParameter("sid",baseRequest.getSid())
                .addQueryParameter("uin",baseRequest.getUin())
                .addQueryParameter("deviceid",baseRequest.getDeviceId())
                .addQueryParameter("_",String.valueOf(System.currentTimeMillis()))
                .addQueryParameter("synckey",weChatClient.getWeChatCore().getSession().getCheckSyncKey().toString())
                .build();
        Request request = BASE_REQUEST.newBuilder().url(httpUrl).get().build();
        try (
            Response response = okHttpClient.newCall(request).execute();
        ){
            String body = response.body().string();
            body = HttpUrlHelper.getValueByKey(body,"window.synccheck");
            JSONReader reader = JSONReader.of(body);
            reader.getContext().config(JSONReader.Feature.AllowUnQuotedFieldNames, true); // 允许未加引号的字段名
            JSONObject jsonObject = reader.read(JSONObject.class);
            return new SyncCheckResponse(jsonObject.getString("retcode"),jsonObject.getString("selector"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject getMessage() {
        Session session = weChatClient.getWeChatCore().getSession();
        BaseRequest baseRequest = session.getBaseRequest();
        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.WEBWXSYNC)
                .addQueryParameter("sid",baseRequest.getSid())
                .addQueryParameter("skey",baseRequest.getSkey())
                .addQueryParameter("pass_ticket",baseRequest.getPassTicket())
                .addQueryParameter("rr",String.valueOf(System.currentTimeMillis()))
                .build();
        Request request = BASE_REQUEST.newBuilder().url(httpUrl)
                .post(RequestBody.create(mediaType,JSON.toJSONString(session)))
                .build();
        try(
                Response response = okHttpClient.newCall(request).execute()
        ) {
            String body = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(body);
            return jsonObject;
        } catch (IOException e) {
            weChatClient.getLogger().info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject getContact() {
        Session session = weChatClient.getWeChatCore().getSession();
        BaseRequest baseRequest = session.getBaseRequest();
        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.GET_CONTACT)
                .addQueryParameter("skey",baseRequest.getSkey())
                .addQueryParameter("pass_ticket",baseRequest.getPassTicket())
                .addQueryParameter("rr",String.valueOf(System.currentTimeMillis()))
                .build();
        Request request = BASE_REQUEST.newBuilder().url(httpUrl)
                        .post(RequestBody.create(mediaType,JSONObject.toJSONString(new JSONObject())))
                                .build();
        try(
                Response response = okHttpClient.newCall(request).execute();
        ) {
            String body = response.body().string();
            return JSON.parseObject(body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public JSONObject sendMessage(String toUserName,String content) {
        Session session = weChatClient.getWeChatCore().getSession();
        String s = HttpUrlHelper.generateTimestampWithRandom();
        SendMessage sendMessage = SendMessage.builder()
                .fromUserName(session.getWxInitInfo().getUser().getUserName())
                .localId(s)
                .clientMsgId(s)
                .content(content)
                .type(MsgType.TextMsg.getIdx())
                .toUserName(toUserName)
                .build();
        BaseRequest baseRequest = session.getBaseRequest();
        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.SEND_MESSAGE)
                .addQueryParameter("pass_ticket","pass_ticket")
                .addQueryParameter("lang","zh_CN")
                .build();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Msg",sendMessage);
        jsonObject.put("Scene",0);
        Request request = BASE_REQUEST.newBuilder().url(httpUrl)
                .post(RequestBody.create(mediaType,jsonObject.toString()))
                .build();
        try(Response response = okHttpClient.newCall(request).execute()) {
            String body = response.body().string();
            return JSON.parseObject(body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getMsgImage(String msgId) {
        Session session = weChatClient.getWeChatCore().getSession();
        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.GET_MSG_IMG)
                .addQueryParameter("MsgID",msgId)
                .addQueryParameter("skey",session.getBaseRequest().getSkey())
                .build();
        Request request = BASE_REQUEST.newBuilder().url(httpUrl)
                .get()
                .build();
        try(Response response = okHttpClient.newCall(request).execute()) {
            return response.body().bytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendVideo(SendMessage sendMessage){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Msg",sendMessage);
        jsonObject.put("Scene",0);

        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.SEND_VIDEO)
                .addQueryParameter("f","json")
                .addQueryParameter("pass_ticket","pass_ticket")
                .addQueryParameter("lang","zh_CN")
                .addQueryParameter("fun","async")
                .build();

        Request request = BASE_REQUEST.newBuilder()
                .url(httpUrl)
                .post(RequestBody.create(mediaType,jsonObject.toString()))
                .build();
        try(Response response = okHttpClient.newCall(request).execute()) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendImage(SendMessage sendMessage){
        Session session = weChatClient.getWeChatCore().getSession();

        BaseRequest baseRequest = session.getBaseRequest();

        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.SEND_IMAGE)
                .addQueryParameter("pass_ticket",baseRequest.getPassTicket())
                .addQueryParameter("lang","zh_CN")
                .addQueryParameter("fun","async")
                .addQueryParameter("f","json")
                .build();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Msg",sendMessage);
        jsonObject.put("Scene",0);

        weChatClient.getLogger().debug(        jsonObject.toString());
        Request request = BASE_REQUEST.newBuilder().url(httpUrl)
                .post(RequestBody.create(mediaType,jsonObject.toString()))
                .build();

        try(Response response = okHttpClient.newCall(request).execute()) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送图片
     *
     * @param toUserName
     * @return
     */
    @Override
    public boolean sendImage(String toUserName, File file) {

        // 尝试上传图片

        UploadMediaRequest uploadMediaRequest = UploadMediaRequest.builder()
                .toUserName(toUserName).build();

        UploadResponse uploadResponse = FileChunkUploader.INSTANCE.upload(file, uploadMediaRequest);

        Session session = weChatClient.getWeChatCore().getSession();

        // 如果传输成功
        if(uploadResponse.isFull()){
            String s = HttpUrlHelper.generateTimestampWithRandom();
            SendMessage sendMessage = SendMessage.builder()
                    .fromUserName(session.getWxInitInfo().getUser().getUserName())
                    .localId(s)
                    .clientMsgId(s)
                    .content("")
                    .type(MsgType.ImageMsg.getIdx())
                    .toUserName(toUserName)
                    .mediaId(uploadResponse.getMediaId())
                    .build();
            sendImage(sendMessage);
        }

        return false;


    }

    @Override
    public boolean sendVideo(String toUserName, File file) {

        UploadMediaRequest uploadMediaRequest = UploadMediaRequest.builder()
                .toUserName(toUserName).build();

        UploadResponse uploadResponse = FileChunkUploader.INSTANCE.upload(file, uploadMediaRequest);

        Session session = weChatClient.getWeChatCore().getSession();

        // 如果传输成功
        if(uploadResponse.isFull()){
            String s = HttpUrlHelper.generateTimestampWithRandom();

            SendMessage sendMessage = SendMessage.builder()
                    .fromUserName(session.getWxInitInfo().getUser().getUserName())
                    .localId(s)
                    .clientMsgId(s)
                    .content("")
                    .type(MsgType.VideoMsg.getIdx())
                    .toUserName(toUserName)
                    .mediaId(uploadResponse.getMediaId())
                    .build();

            // 发送视频消息
            sendVideo(sendMessage);
        }

        return false;
    }

    @Override
    public File getIcon(String userName) {

        File file = new File(weChatClient.getDataFolder(),"img/icon/"+userName+".jpg");
        if(file.exists()) return file;

        Contact contact = weChatClient.getContactManager().getContactCache().get(userName);
        Session session = weChatClient.getWeChatCore().getSession();
        if(contact.getHeadImgUrl()!=null){
            HttpUrl ur = URL.BASE_URL.newBuilder().encodedPath(URL.GET_ICON)
                    .addQueryParameter("username",userName)
                    .addQueryParameter("skey",session.getBaseRequest().getSkey())
                    .addQueryParameter("type","big")
                    .addQueryParameter("chatroomid",contact.getEncryChatRoomId())
                    .addQueryParameter("seq","0")
                    .build();

            Request request = BASE_REQUEST.newBuilder()
                    .url(ur).get().build();

            try(Response response = okHttpClient.newCall(request).execute()){
                BufferedImage bufferedImage = convertHexToBufferedImage(response.body().bytes());
                saveImage(bufferedImage,new File(weChatClient.getDataFolder(),"img/icon/"+userName+".jpg"));
                return file;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return null;
    }

    @Override
    public byte[] getVideo(long msgId) {
        Session session = weChatClient.getWeChatCore().getSession();
        HttpUrl url = URL.BASE_URL.newBuilder().encodedPath(URL.GET_VIDEO)
                .addQueryParameter("msgid",String.valueOf(msgId))
                .addQueryParameter("skey",session.getBaseRequest().getSkey())
                .build();

        Request request = BASE_REQUEST.newBuilder()
                .url(url)
                .addHeader("Referer",url.toString())
                .addHeader("Range","bytes=0-")
                .get()
                .build();

        System.out.println("get video()");
        try(Response response = okHttpClient.newCall(request).execute()) {
            return response.body().bytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveImage(BufferedImage bufferedImage,File file) {
        try {
            ImageIO.write(bufferedImage, "PNG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage convertHexToBufferedImage(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
