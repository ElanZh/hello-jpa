# JPA 使用示例

---
```hello.elan.jpa.auth``` 包
## 使用JPA来实现经典的 账号(manager)-角色(role)-菜单(resource) 权限控制系统
其中：
* 账号-角色 多对多
* 菜单-角色 多对多
* 菜单-子菜单 一对多

修改```application.yaml```中的数据库链接，就可以直接启动项目

项目第一次启动时```InitRunner```会向三张表插入一堆有父子级的菜单，一个admin角色，一个admin账号

其中 ```AuthService``` 使用 ```JpaSpecificationExecutor``` 执行各种复杂查询

---
```hello.elan.jpa.freight``` 包
## 使用JPA 实现 城配货运三个实体的关联关系：
运单(Waybill)-运单地址(WaybillAddress)-包裹(Wrap)

* 一个运单对应多个地址

* 每个地址有一组要卸车的包裹

* 新增顺序为，新增包裹，把包裹分配给运单，同时将同一地点的包裹生成运单地址

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
### SQL监控

Hibernate打出的sql没法看，要设置各种trace/debug级别的日志，而且碰到枚举的入参就不会显示值

因此，引入p6spy : [引入过程](./SQL监控.md)

---

### 如果启动碰到了tomcat报错，用这个解决：[tomcat报错解决](./Tomcat报错.md)

不解决也没啥问题