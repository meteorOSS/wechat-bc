import com.meteor.wechatbc.login.WeChatLogin;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeChatBcTest {

    private Logger logger = LoggerFactory.getLogger(WeChatBcTest.class);
    private WeChatLogin weChatLogin = new WeChatLogin(logger);
    @Test
    public void getUUID(){
        String loginUUID = weChatLogin.getLoginUUID();
        logger.info(loginUUID);
    }

    @Test
    public void login(){
        weChatLogin.login();
    }
}
