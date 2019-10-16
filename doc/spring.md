#SpringFramework
## 简介


## 设计核心
Spring框架的一个设计核心：**非侵入性**。这意味着开发者无需在自身的业务/域模型上被迫引入框架特定的类和接口

## 构建环境
    引入spring-context自动会引入spring-beans,spring-aop,spring-core,spring-expression，构成spring开发环境
    
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.1.9.RELEASE</version>
    </dependency>