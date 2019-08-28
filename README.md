# shizy-springboot-Test

在springboot上实现一些功能作技术储备

### 实现的功能

##### 1、基本curd及redis缓存
```
RESTFul api
```

##### 2、多环境部署

* IDEA的Active profiles处，添加`dev`

* Maven打包命令
```
mvn clean package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Pdev
mvn clean package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Prelease
```
打包完idea启动项目报错，Rebuild project或者mvn clean

* jar包运行命令
```
(java -server -Xmx1024m -Xms256m -XX:+UseParallelGC -XX:ParallelGCThreads=20  -jar Shizy-SpringBoot-Demo-1.0-SNAPSHOT.jar &)
```

##### 3、自定义注解切面 todo
```
实现自定义注解，在添加该注解的方法上，注入一个切面，去写入MongoDB作为访问log
```

##### 4、@Async实现异步处理 todo
```
实现注解切面时，将写入MongoDB的操作用异步处理，不影响主线程的运行时间。
为保证切面在多线程插入log时的执行顺序，使用公平锁锁住插入操作，实现线程先进先执行。
```

<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>