## 集成QueryDSL

1. 引入maven依赖：
    ```xml
    <project>
        ...
        <properties>
            ...
            <query-dsl.version>4.2.1</query-dsl.version>
        </properties>
        ...
        <dependencies>
            ...
            <!--query-dsl-->
            <dependency>
                <groupId>com.query-dsl</groupId>
                <artifactId>query-dsl-apt</artifactId>
                <version>${query-dsl.version}</version>
                <scope>provided</scope>
            </dependency>
            <dependency>
                <groupId>com.query-dsl</groupId>
                <artifactId>query-dsl-jpa</artifactId>
                <version>${query-dsl.version}</version>
            </dependency>
            <dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>slf4j-log4j12</artifactId>
                <version>1.6.1</version>
            </dependency>
        </dependencies>
    </project>
    ```

2. 引入maven插件
    ```xml
    <project>
        <build>
        <plugins>
            ...
            <plugin>
                <groupId>com.mysema.maven</groupId>
                <artifactId>apt-maven-plugin</artifactId>
                <version>1.1.3</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>process</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>target/generated-sources/java</outputDirectory>
                            <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            ...
        </plugins>
        </build>
    </project>
    ```

3. 配置JPAQueryFactory

    使用QueryDSL的功能时，会依赖使用到JPAQueryFactory，而JPAQueryFactory在这里依赖使用EntityManager

    ```java
    // 这样装配JPAQueryFactory，让spring管理这个查询factory
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
    ```
   
4. 执行 ```mvn clean -DskipTests compile```

    maven 插件会在 target 下生成 ```generated-sources/java``` 文件夹，其中会为你的每一个```@Entity```实体建立 查询方法，接下来就可以使用QueryDSL了
    
---
## 使用QueryDSL

各实体的Repo可以对 ```QueryDslPredicateExecutor<>```继承，也可以不继承，继承之后对Repo的支持更多。

