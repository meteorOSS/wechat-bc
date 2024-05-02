package com.meteor.wechatbc.entitiy.session;


import com.alibaba.fastjson2.annotation.JSONField;
import com.meteor.wechatbc.impl.cookie.CookiePack;
import lombok.Data;
import lombok.ToString;
import okhttp3.Cookie;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公共请求
 * 登陆后的所有接口都需要携带该类
 */
@Data
@ToString
public class BaseRequest implements Serializable {
    @JSONField(name = "Uin")
    private String uin;
    @JSONField(name = "Sid")
    private String sid;
    @JSONField(name = "Skey")
    private String skey;
    @JSONField(name = "DeviceID")
    private String deviceId;
    @JSONField(serialize = false)
    private String dataTicket;
    @JSONField(serialize = false)
    private String passTicket;
    @JSONField(serialize = false)
    private String authTicket;

    @JSONField(serialize = false)

    transient private List<Cookie> initCookie; // 该类不能进行序列化

    private List<CookiePack> cookiePacks; // 代理了cookie类来实现序列化

    public void setInitCookie(List<Cookie> initCookie) {
        this.initCookie = initCookie;
        this.cookiePacks = initCookie.stream().map(cookie -> new CookiePack(cookie))
                .collect(Collectors.toList());
    }

    public List<Cookie> getInitCookie() {
        if(initCookie == null){
            this.initCookie = cookiePacks.stream().map(cookiePack -> cookiePack.getCookie()).collect(Collectors.toList());
        }
        return initCookie;
    }

    // 解析xml
    public BaseRequest(String xmlData){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

            Element errorElement = (Element) document.getElementsByTagName("error").item(0);

            String ret = errorElement.getElementsByTagName("ret").item(0).getTextContent();
            if(ret.equalsIgnoreCase("0")){
                NodeList skeyList = document.getElementsByTagName("skey");
                if (skeyList.getLength() > 0) {
                    Element skeyElement = (Element) skeyList.item(0);
                    this.skey = skeyElement.getTextContent();;
                }
                NodeList wxsidList = document.getElementsByTagName("wxsid");
                if (wxsidList.getLength() > 0) {
                    Element wxsidElement = (Element) wxsidList.item(0);
                    this.sid = wxsidElement.getTextContent();
                }
                NodeList wxuniList = document.getElementsByTagName("wxuin");
                if (wxsidList.getLength() > 0) {
                    Element wxuniElement = (Element) wxuniList.item(0);
                    this.uin = wxuniElement.getTextContent();
                }
                NodeList passTicketList = document.getElementsByTagName("pass_ticket");
                if (passTicketList.getLength() > 0) {
                    Element passTicketElement = (Element) passTicketList.item(0);
                    this.passTicket = passTicketElement.getTextContent();
                }
            }else {
                String message = errorElement.getElementsByTagName("message").item(0).getTextContent();
                LogManager.getLogger("BASE-REQUEST").error("登录失败：" + message + " CODE: " + ret);
                throw new RuntimeException();
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
