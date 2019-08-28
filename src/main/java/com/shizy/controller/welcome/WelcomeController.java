package com.shizy.controller.welcome;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.Map;

@ApiIgnore
@Controller
@RequestMapping("/")
@Api(tags = "welcome", description = "welcome")
public class WelcomeController {

    @Value("${application.message:Hello World}")
    private String message = null;

    @ApiOperation(value = "welcome", notes = "")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String welcome(Map<String, Object> model) {
        model.put("time", new Date());
        model.put("message", this.message);
//        return "welcome";
        return "redirect:swagger-ui.html";
    }

}



























