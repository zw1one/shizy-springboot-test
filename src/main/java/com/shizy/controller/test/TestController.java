package com.shizy.controller.test;

import com.alibaba.fastjson.JSON;
import com.shizy.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
@Api(tags = "test", description = "lol")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value = "lol", notes = "{\"param-key\":\"param-value\"}")
    @RequestMapping(value = "lol", method = RequestMethod.POST)
    public String lol(@RequestBody Map param) {

        /**
         * todo
         *
         * 1、加入mongodb
         *
         * 2、编写切面 自定义注解 使用该注解 则在mongodb写入log
         *
         * 3、使用@Async异步线程池写入mongo，使用公平锁保证写入的先后顺序
         *
         *
         */

//        https://stackoverflow.com/questions/13206792/spring-async-limit-number-of-threads
//
//
//        @Configuration
//        @EnableAsync
//        public class AppConfig implements AsyncConfigurer {
//
//    [...]
//
//            @Override
//            public Executor getAsyncExecutor() {
//                ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//                executor.setCorePoolSize(2);
//                executor.setMaxPoolSize(5);
//                executor.setQueueCapacity(50);
//                executor.setThreadNamePrefix("MyExecutor-");
//                executor.initialize();
//                return executor;
//            }
//        }
//
//
//        class MyFairLock {
//            /**
//             * true 表示 ReentrantLock 的公平锁
//             */
//            private ReentrantLock lock = new ReentrantLock(true);
//
//            public void testFail() {
//                try {
//                    lock.lock();
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(Thread.currentThread().getName() + "获得了锁");
//                } finally {
//                    lock.unlock();
//                }
//            }
//
//            public static void main(String[] args) {
//                MyFairLock fairLock = new MyFairLock();
//                Runnable runnable = () -> {
//                    System.out.println(Thread.currentThread().getName() + "启动");
//                    fairLock.testFail();
//                };
//                Thread[] threadArray = new Thread[10];
//                for (int i = 0; i < 10; i++) {
//                    threadArray[i] = new Thread(runnable);
//                }
//                for (int i = 0; i < 10; i++) {
//                    threadArray[i].start();
//                }
//            }
//        }


        return JSON.toJSONString(param);
    }


}






















