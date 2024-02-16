# WeChatBc

> 类似开发公众号一样，开发个人微信号

参考了Bukkit的框架设计，呼吸顺畅的编写你自己的逻辑 ⭐

### 安装

在发布页得到最新的jar文件，新建bat脚本在同一级目录，内容如下

```
@echo off
java -jar WeChatBc-1.0-SNAPSHOT-jar-with-dependencies.jar
pause
```

双击启动

![image](https://github.com/meteorOSS/WeChatBc/assets/61687266/347897f3-92d0-4d08-a881-f48987a76055)



### 快速开始

使用wechatbc做定制开发非常简单，将wechatbc作为依赖引入

```xml
    <dependencies>
        <dependency>
            <groupId>com.meteor</groupId>
            <artifactId>wechatbc</artifactId>
            <version>1.0</version>
            <scope>system</scope>
            <systemPath>${basedir}/src/lib/WeChatBc-1.0-SNAPSHOT-jar-with-dependencies.jar</systemPath>
        </dependency>
    </dependencies>
```

新增 `plugin.yml` 文件，它用于描述一个wechatbc插件

```yaml
name: 'TestPlugin'
main: 'com.meteor.TestPlugin'
version: 1.0
authors:
  - 'meteor'
description: '这是一个示例插件，展示了怎么编写WechatBc插件'
```

其中main为插件的主类，继承BasePlugin

``` java
public class TestPlugin extends BasePlugin implements Listener {

    @Override
    public void onEnable() {
        // 注册事件
        getWeChatClient().getEventManager().registerPluginListener(this,this);
    }

    /**
     * 接收信息事件
     * @param messageEvent
     */
    @EventHandler
    public void onReceiveMessage(ReceiveMessageEvent messageEvent){
        if(messageEvent.getContent().equalsIgnoreCase("今夕是何年?")){
            HttpAPI httpAPI = getWeChatClient().getWeChatCore().getHttpAPI();
            httpAPI.sendMessage(messageEvent.getMessage().getFromUserName(), String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        }
    }
}
```

打包为jar，放入plugins文件夹，重启服务。效果如下:

![image](https://github.com/meteorOSS/WeChatBc/assets/61687266/b812954f-a8bf-4a71-8203-901f5abcdef1)

### 插件资源

随着wechatbc功能的迭代，为方便测试写了一些好玩的东西。
如果你基于wechatbc开发了插件，欢迎提交PR，我会把你的插件放在这里

[微信setu插件](https://github.com/meteorOSS/WeChatSetu)

![image](https://github.com/meteorOSS/WeChatBc/assets/61687266/91686c0f-da30-48bd-8848-e02ba8075235)



**该项目目前处于初步阶段，后续的迭代会更加完善**

**任何问题欢迎提交issue或加入Q群653440235反馈**


























