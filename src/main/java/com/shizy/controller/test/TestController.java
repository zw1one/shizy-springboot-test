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
    public String add(@RequestBody Map param) {

        return JSON.toJSONString(param);
    }


}






















