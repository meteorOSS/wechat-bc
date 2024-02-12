import com.meteor.wechatbc.event.Event;
import com.meteor.wechatbc.event.EventHandler;
import com.meteor.wechatbc.impl.event.EventManager;
import com.meteor.wechatbc.impl.event.Listener;
import com.meteor.wechatbc.plugin.Plugin;
import org.junit.Test;
import org.slf4j.Logger;

public class TestEventBus {

    private static class TestEvent extends Event{
    }

    @Test
    public void test(){
        EventManager eventManager = new EventManager();
        eventManager.registerPluginListener(new Plugin() {
            @Override
            public Logger getLogger() {
                return null;
            }

            @Override
            public void setEnable(boolean enable) {
            }

            @Override
            public boolean isEnable() {
                return false;
            }

            @Override
            public void onLoad() {

            }

            @Override
            public void onEnable() {

            }

            @Override
            public void onDisable() {

            }
        }, new Listener() {

            @EventHandler
            public void onTest(TestEvent testEvent){
                System.out.println("testEvent()");
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });

        eventManager.callEvent(new TestEvent());
    }
}
