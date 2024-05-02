package com.meteor.wechatbc.impl.fileupload;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.meteor.wechatbc.impl.HttpAPIImpl;
import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.impl.fileupload.model.UploadMediaRequest;
import com.meteor.wechatbc.impl.fileupload.model.UploadResponse;
import com.meteor.wechatbc.impl.model.Session;
import com.meteor.wechatbc.util.URL;
import okhttp3.*;
import okio.BufferedSink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * 分块上传文件至微信服务器
 */
public class FileChunkUploader {

    private final WeChatClient weChatClient;

    private final HttpAPIImpl httpAPI;

    private final Logger logger = LogManager.getLogger(FileChunkUploader.class);

    private FileChunkUploader(WeChatClient weChatClient){
        this.weChatClient = weChatClient;
        this.httpAPI = ((HttpAPIImpl)weChatClient.getWeChatCore().getHttpAPI());
    }

    public static FileChunkUploader INSTANCE;

    public static void init(WeChatClient weChatClient){
        INSTANCE = new FileChunkUploader(weChatClient);
    }

    private final long CHUNK_SIZE = 524288; // 分块大小 : 5m

    /**
     * 文件类型
     */
    private static class FileTypeDetector {
        private static final String PIC = "pic";
        private static final String VIDEO = "video";
        private static final String DOC = "doc";

        // 支持的图片格式
        private static final Set<String> imageType = new HashSet<>(Arrays.asList("jpg", "jpeg", "png","gif"));
        // 视频文件扩展名
        private static final String videoType = "mp4";

        // 获取文件类型
        public static String getMessageType(String filename) {
            String ext = getFileExt(filename).toLowerCase();
            if (imageType.contains(ext)) {
                return PIC;
            }
            if (ext.equals(videoType)) {
                return VIDEO;
            }
            return DOC;
        }

        private static String getFileExt(String filename) {
            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < filename.length() - 1) {
                return filename.substring(dotIndex + 1);
            }
            return "";
        }
    }


    public UploadResponse upload(File file, UploadMediaRequest uploadMediaRequest){

        Session session = weChatClient.getWeChatCore().getSession();


        try {

            String filename = file.getName();

            // 获取文件上传类型
            MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
            String mimeType = fileTypeMap.getContentType(file);

            // 计算文件md5
            String md5 = calculateFileMD5(file);
            // 文件类型
            String messageType = FileTypeDetector.getMessageType(file.getName());

            long fileSize = file.length();
            // 分块数量 (向上取整)
            long chunks = (fileSize + CHUNK_SIZE - 1) / CHUNK_SIZE;

            uploadMediaRequest.setDataLen(fileSize);
            uploadMediaRequest.setTotalLen(fileSize);
            uploadMediaRequest.setFiledMD5(md5);
            uploadMediaRequest.setClientMediaId(System.currentTimeMillis() / 1000);
            uploadMediaRequest.setBaseRequest(session.getBaseRequest());
            uploadMediaRequest.setFromUserName(session.getWxInitInfo().getUser().getUserName());

            for(int chunk=0;chunk<chunks;chunk++){
                // 定位当前块的起始位置
                long start = chunk * CHUNK_SIZE;
                // 计算当前块的大小（最后一块可能小于chunkSize）
                long chunkLength = Math.min(fileSize - start, CHUNK_SIZE);
                RequestBody chunkBody = new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return MediaType.parse(mimeType); // 文件的MediaType
                    }
                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {
                        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                            raf.seek(start);
                            byte[] buffer = new byte[8192];
                            long remaining = chunkLength;
                            while (remaining > 0) {
                                int read = raf.read(buffer, 0, (int)Math.min(buffer.length, remaining));
                                if (read == -1) break;
                                sink.write(buffer, 0, read);
                                remaining -= read;
                            }
                        }
                    }
                    @Override
                    public long contentLength() throws IOException {
                        return chunkLength; // 当前块的大小
                    }
                };
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id", "WECHAT_BC")
                        .addFormDataPart("name", filename)
                        .addFormDataPart("type", mimeType)
                        .addFormDataPart("size", String.valueOf(fileSize))
                        .addFormDataPart("mediatype", messageType)
                        .addFormDataPart("uploadmediarequest", JSON.toJSONString(uploadMediaRequest)) // 构建上传请求JSON
                        .addFormDataPart("webwx_data_ticket", session.getBaseRequest().getDataTicket())
                        .addFormDataPart("pass_ticket", session.getBaseRequest().getPassTicket())
                        .addFormDataPart("filename", filename)
                        .addFormDataPart("lastModifiedDate", new Date().toString())
                        .addFormDataPart("chunk", String.valueOf(chunk))
                        .addFormDataPart("filename",filename,chunkBody);
                if(chunks>1){
                    builder.addFormDataPart("chunks", String.valueOf(chunks));
                }
                HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                        .encodedPath(URL.UPLOAD_FILE)
                        .addQueryParameter("f","json")
                        .build();
                Request request = httpAPI.getBASE_REQUEST()
                        .newBuilder()
                        .url(httpUrl)
                        .post(builder.build())
                        .build();
                try(
                        Response response = httpAPI.getOkHttpClient().newCall(request).execute();
                ) {
                    logger.debug("[{}] {} / {}",filename,chunk+1,chunks);
                    UploadResponse uploadResponse = JSON.toJavaObject(response.body().string(), UploadResponse.class);
                    // 检查中途块传输是否出现问题
                    if(chunk != chunks-1){
                        if(!uploadResponse.isFull()){
                            logger.error("{} 传输出现错误",filename);
                            throw new RuntimeException("文件上传出现错误,文件名: "+filename);
                        }
                    }else {
                        logger.info(uploadResponse.toString());
                        return uploadResponse;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    /**
     * 计算文件md5值
     * @param file
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private String calculateFileMD5(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        try (InputStream is = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }
        return new HexBinaryAdapter().marshal(digest.digest()).toLowerCase();
    }
}
