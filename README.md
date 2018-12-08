本项目演示如何实现将发送给Controller的请求，以一定的比例发送给另一个URL地址，支持GET和POST方法（请求体以JSON方式提交）。

在需要进行请求复制转发的Controller方法上添加如下注解：
```java
@RequestCopy(url = "http://localhost:8080/post/test", ratio = 0.05F)
```

这里代表将该方法的请求，以0.05（5%）的比例，发送到http://localhost:8080/post/test
1. 如果是GET方法，URL的参数也会同样发送到该地址；
2. 如果是POST方法，除URL的参数外，RequestBody的JSON对象也同样会发送。

在application.yml配置文件中，通过增加配置项：
```
request-copy: on
```
打开请求复制；如果不写此配置项或为其他值，用@RequestCopy修饰的方法不会进行请求复制。

总结：通过SpringMVC拦截器+注解的方式实现了GET和POST方法的1：1原样复制发送，并使用新线程执行，原请求继续执行原来的处理流程。



---

**示例:**

1. GET: http://localhost:8080 ，该请求会复制发送到http://localhost:8080/test
2. POST: http://localhost:8080/post, {"id":123, "name":"jack"}，该请求会复制发送到http://localhost:8080/post/test