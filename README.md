# GEO-EMAIL

==前言==

本组件（geo-email）是作者在大三上的时候，觉得使用 javax.email 太麻烦了，准备自己封装一个，但是又想使用起来很简单，于是乎就想到了自己写一个组件的想法（先前没写过组件），所以有很多不足的地方请见谅

## 组件介绍

【geo-email】 将来会用来处理 Email 的各种问题，目前还只能处理 邮件发送

## 使用方法

使用 maven 配置引入

**目前还是不能够使用maven的哇，直接使用源代码到项目中去吧**
**作者使用私有仓库是这样配置的**

```xml
        <dependency>
            <groupId>com.geo</groupId>
            <artifactId>geo-email-spring-boot-starter</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```

编写 yml 文件

```yml
geo-email:
  host: "[smtp server host]" # smtp 服务器 域名
  from: "[sender email address]"# 发送者邮箱
  auth-code: "[auth code]" # 邮箱认证码
  ssl-enable: "[is enable ssl]" # 是否开启 ssl 加密
  port: "smtp server port" # smtp 服务器端口
  fallback: "is send with no ssl" # 是否把消息回发到 ssl 以外的邮箱
  charset: "[charset]" # 邮件编码
  personal: "[who send email]" # 邮件发送者组织
```

最后直接调用即可

```java
    @GetMapping("/email/send")
    public String emailTest() {
        Status status = EmailUtils.sendHtmlEmail(
                "1601761326@qq.com",
                "【GEO-EMAIL】",
                "private repository is create success!!! <b> you started the first step </b>");
        log.info(status.name());
        return "Hello Navi~";
    }
```



1.0.0 版本

本版本还有很多问题，仅仅作为作者个人使用，并用来首次发布用的，后面的问题会在接下来的版本中一个一个解决

<!-- 作者花了很多精力才搞完这个版本的编写，优化，等工作，最后要结束的时候才看到网上已经有类似功能的更好的工具或者包存在了，还是比较伤心的，不过继续加油吧 -->

#### 1.0.0 问题集

```
1、加密问题没有处理，仅仅只使用了别人写好的邮件加密，关于和 smtp 服务器建立连接， authcode 授权码加密等问题还没有解决
	解决目标  --  实现类似于 druid 那样的东西
2、日志信息不完善，有很多可能出现问题的地方没有进行日志收集，这个确实是因为作者见过的世面太少了，没有完全统计的能力
    解决目标  --  遇到一个解决一个即可
3、返回类型不够完善 ，在邮件处理完成之后有多个状态，没有进行统计，理由和上面一样
    解决目标  --  遇到一个添加一个
4、代码逻辑比较简单，但是分层不够明确，比如 util 层还有 sender 在，呃，知道有这个错误，但是实在想不出来放哪里好了，就索性放这里了
    解决目标  --  学习架构之后来解决
5、代码有很多依赖，因为邮件处理有很多需要考虑的东西，所以本版本还有很多是别人的东西（代码），所以在配置依赖的时候很痛苦。。。后面作者一个一个学了之后来自己写了（配置依赖最苦了）
    解决目标  --  代码 70 % 自己写的
6、try-catch 比较多，因为还没有些异常处理（就写了一个initexception），陷入了回调地狱。这应该会是第二个解决的问题
    解决目标  --  使用异常处理类处理异常，并完善状态
7、参数校验没有做完，只做了一部分，特别是后面的 util，因为写累了。。。这应该会是第一个解决的问题
    解决目标  -- 添加参数校验就行
```

