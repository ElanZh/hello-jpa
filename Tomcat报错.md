# 启动项目Tomcat报错 ： 找不到 tcnative-1.dll

内嵌Tomcat 9，把日志级别设为Debug后，从控制台看到启动时tomcat报无法找到JNI动态链接库的错误：
```java
org.apache.tomcat.jni.LibraryNotFoundError: Can't load library: D:\xxx\bin\tcnative-1.dll, Can't load library: D:\xxx\bin\libtcnative-1.dll, no tcnative-1 in java.library.path, no libtcnative-1 in java.library.path
	at org.apache.tomcat.jni.Library.<init>(Library.java:102) ~[tomcat-embed-core-9.0.13.jar:9.0.13]
	at org.apache.tomcat.jni.Library.initialize(Library.java:206) ~[tomcat-embed-core-9.0.13.jar:9.0.13]
	at org.apache.catalina.core.AprLifecycleListener.init(AprLifecycleListener.java:198) [tomcat-embed-core-9.0.13.jar:9.0.13]
	at org.apache.catalina.core.AprLifecycleListener.isAprAvailable(AprLifecycleListener.java:107) [tomcat-embed-core-9.0.13.jar:9.0.13]
	at org.apache.catalina.connector.Connector.<init>(Connector.java:80) [tomcat-embed-core-9.0.13.jar:9.0.13]
```

这是因为Tomcat中的connector为了提高性能，采用了加载与操作系统绑定（非跨平台）的本地库的方式，比如Windows系统中就是.dll动态链接库。上述异常中找不到的两个.dll库文件，默认会去Tomcat的bin目录下去找。

但是由于SpringBoot的Tomcat是嵌入式的，没有这两个.dll，所以只好自己去[官网下载](http://archive.apache.org/dist/tomcat/tomcat-connectors/native/1.2.14/binaries/):

```tomcat-native-1.2.14-win32-bin.zip```

复制其中的```tcnative-1.dll```到 C:\Windows\System32

64位系统要复制 x64 下的 ```tcnative-1.dll``` 到同样的目录