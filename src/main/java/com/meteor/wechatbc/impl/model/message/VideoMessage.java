package com.meteor.wechatbc.impl.model.message;

import com.meteor.wechatbc.entitiy.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class VideoMessage extends Message {

    @Override
    public String getContent() {
        return "(视频消息)";
    }

    @Setter
    @Getter
    private byte[] bytes;


    public File saveFile(File file){
        try(FileOutputStream fileOutputStream = new FileOutputStream(file);
        ) {
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            return file;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
