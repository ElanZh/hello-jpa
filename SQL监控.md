## springboot 集成p6spy 监控sql

* ### [p6spy官网](https://p6spy.readthedocs.io/en/latest/)

* ### [springboot 自动配置](https://github.com/gavlyukovskiy/spring-boot-data-source-decorator)

## 只须引入maven依赖：
    ```xml
           ...
        <dependencies>
            <dependency>
                <groupId>com.github.gavlyukovskiy</groupId>
                <artifactId>p6spy-spring-boot-starter</artifactId>
                <version>1.5.8</version>
            </dependency>
        </dependencies>
           ...
    ```
然后启动项目就能看到真正执行的完整sql以及sql耗时和链接等信息

装饰顺序如下：

```P6DataSource -> ProxyDataSource -> FlexyPoolDataSource -> DataSource```
   
配置文件中可做的配置有以下几项：
```yaml
decorator:
  datasource:
    p6spy:
      enable-logging: true
      # log-file: p6spy.log
      multiline: true
      logging: slf4j
      log-format: 执行SQL- 耗时 %(executionTime)ms | 类型 %(category) | connection%(connectionId) | %(sqlSingleLine);
```

也可以使用p6spy的[官方配置](https://p6spy.readthedocs.io/en/latest/configandusage.html)
   