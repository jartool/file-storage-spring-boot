# file-storage-spring-boot

### 一、简介

------------

	一个简单的文件存储模块
![3.png](https://i.loli.net/2021/10/26/qXWZvRlAj9L6oHN.png)
### 二、食用方式
- #### maven

------------

```xml
<!-- file-storage-spring-boot -->
<dependency>
    <groupId>io.github.jartool</groupId>
    <artifactId>file-storage-spring-boot</artifactId>
    <version>1.0.0</version>
</dependency>
<!-- thymeleaf -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```
- #### configuration

------------
```java
//启动类Add: @EnableFileStorage注解即可
@EnableFileStorage
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```
```yaml
#yml配置
jartool:
  storage:
    file:
      view: /storage  #文件存储页面访问地址,默认:/storage
      storage-path: /home/user/storage  #文件上传目录,默认用户目录下: $User/storage
      upload-url: /storage/file/upload  #文件上传接口url
      download-url: /storage/file/download  #文件下载接口url
      delete-url: /storage/file/delete  #文件删除接口url
    auth:
      enable: true  #是否开启页面授权,默认: true
      url: /storageAuth  #授权校验接口url,默认: /storageAuth
      key: auth  #授权key,默认: auth
      username: admin  #用户名,默认: admin
      password: admin  #密码,默认: admin
```
