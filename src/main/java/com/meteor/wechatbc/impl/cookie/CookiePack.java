package com.meteor.wechatbc.impl.cookie;

import lombok.Getter;
import okhttp3.Cookie;

import java.io.*;

/**
 * 代理cookie类以实现序列化
 */
public class CookiePack implements Serializable {

    @Getter private Cookie cookie;

    public CookiePack(Cookie cookie){
        this.cookie = cookie;
    }

    /**
     * 以16进制字符串的形式编码
     * @return
     */
    public String encode(){
        try(
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        ) {
            objectOutputStream.writeObject(this);
            return toHexoString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String toHexoString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    /**
     * 将16进制字符串转换为字节数组
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * 以16进制字符串的形式解码
     */
    public static CookiePack decode(String encodeCookie){
        byte[] bytes = hexStringToBytes(encodeCookie);
        try(
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream);
        ) {
           return CookiePack.class.cast(objectInputStream.readObject());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(cookie.name());
        objectOutputStream.writeObject(cookie.value());
        objectOutputStream.writeLong(cookie.persistent()?cookie.expiresAt():-1);
        objectOutputStream.writeObject(cookie.domain());
        objectOutputStream.writeObject(cookie.path());
        objectOutputStream.writeBoolean(cookie.secure());
        objectOutputStream.writeBoolean(cookie.httpOnly());
        objectOutputStream.writeBoolean(cookie.hostOnly());
    }

    private void reloadObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Cookie.Builder builder = new Cookie.Builder();
        builder.name((String)objectInputStream.readObject());
        builder.value((String)objectInputStream.readObject());

        long l = objectInputStream.readLong();

        if(l!=-1) builder.expiresAt(l);
        String domain = null;
        builder.domain(domain = (String)objectInputStream.readObject());
        builder.path((String)objectInputStream.readObject());

        boolean secure = objectInputStream.readBoolean();
        if(secure) builder.secure();
        boolean httpOnly = objectInputStream.readBoolean();
        if(httpOnly) builder.httpOnly();
        boolean hostOnly = objectInputStream.readBoolean();
        if(hostOnly) builder.hostOnlyDomain(domain);

        this.cookie = builder.build();
    }

}
