# JPA 使用示例

---

## 使用JPA来实现经典的 账号(manager)-角色(role)-菜单(resource) 权限控制系统
其中：
* 账号-角色 多对多
* 菜单-角色 多对多
* 菜单-子菜单 一对多

修改```application.yaml```中的数据库链接，就可以直接启动项目

项目第一次启动时```InitRunner```会向三张表插入一堆有父子级的菜单，一个admin角色，一个admin账号

其中 ```AuthService``` 使用 ```JpaSpecificationExecutor``` 执行各种复杂查询

---

### 为了展现JPA的终极形态，引入QueryDSL:

* [QueryDSL官网](http://www.querydsl.com/)

* [集成指南](http://www.querydsl.com/static/querydsl/latest/reference/html/ch02.html#jpa_integration)

* [github](https://github.com/querydsl/querydsl)

* [如何使用](https://blog.csdn.net/phapha1996/article/details/83614975)

* [本项目的集成过程](./QueryDSL使用指南.md)

QueryDSL的方法使用在 ```QueryService中```

使用QueryDSL需要先 mvn compile

---

### 如果启动碰到了tomcat报错，用这个解决：[tomcat报错解决](./Tomcat报错.md)

不解决也没啥问题