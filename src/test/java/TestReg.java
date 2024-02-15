import org.junit.Test;

public class TestReg {
    @Test
    public void test(){
        String format = "@59f186785e4da8c84bdb4b1d09384714a86d647ba371160b08f4b961b2bbc4a3:<br/>差不多，磨盘变成了键盘";
        System.out.println(format.replaceAll("@([^:])+:<br/>",""));
    }
}
