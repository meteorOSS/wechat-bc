package com.meteor.wechatbc.impl.model.message;

import com.meteor.wechatbc.entitiy.message.Message;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * 图片消息
 */
public class ImageMessage extends Message {

    private BufferedImage bufferedImage;

    @Setter private byte[] bytes; // 图片的二进制信息


    /**
     * 图片二进制流转换为BufferedImage
     */
    public BufferedImage convertHexToBufferedImage() {
        if(bufferedImage!=null) return bufferedImage;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            return bufferedImage = ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将图片保存至磁盘
     * @param file
     */
    public File saveImage(File file,String type) {
        try {
            ImageIO.write(convertHexToBufferedImage(), type, file);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getContent() {
        return "(图片)";
    }
}
