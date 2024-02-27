import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String input = "&lt;sysmsg type=\"revokemsg\"&gt;&lt;revokemsg&gt;&lt;session&gt;wxid_4fyhcp6kuztd22&lt;/session&gt;&lt;oldmsgid&gt;1630382222&lt;/oldmsgid&gt;&lt;msgid&gt;4729511481770835265&lt;/msgid&gt;&lt;replacemsg&gt;&lt;![CDATA[\"zzzsh\" 撤回了一条消息]]&gt;&lt;/replacemsg&gt;&lt;/revokemsg&gt;&lt;/sysmsg&gt;";
        String oldMsgId = extractOldMsgId(input);
        System.out.println("Extracted oldMsgId: " + oldMsgId);
    }

    public static String extractOldMsgId(String input) {
        String pattern = "oldmsgid&gt;(\\d+)&lt;";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);

        if (m.find()) {
            return m.group(1); // 提取括号中的数字部分
        } else {
            return "No oldMsgId found";
        }
    }
}
