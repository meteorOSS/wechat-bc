import cn.hutool.http.HttpRequest;
import com.meteor.wechatbc.impl.model.Session;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static String extractId(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            Pattern pattern = Pattern.compile("var user_name\\s*=\\s*\"(\\w+)\"");
            Matcher matcher = pattern.matcher(responseBody);
            if (matcher.find()) {
                return matcher.group(1);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String s = extractId("https://mp.weixin.qq.com/s/LWwEbv_SULsQxRLlIWHOsA");
        System.out.println(s);

    }
}
