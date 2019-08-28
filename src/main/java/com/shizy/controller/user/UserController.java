package com.shizy.controller.user;

import com.baomidou.mybatisplus.plugins.Page;
import com.shizy.common.json.JsonResult;
import com.shizy.common.json.page.PageDto;
import com.shizy.entity.user.UserDto;
import com.shizy.entity.user.UserPo;
import com.shizy.entity.user.UserVo;
import com.shizy.service.user.UserService;
import com.shizy.utils.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Api(tags = "user-demo", description = "user CRUD")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value = "user add", notes = "")
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public JsonResult add(@RequestBody UserCtrParam_entityNoKey param) {
        try {
            UserPo po = BeanUtil.copyParam2Entity(param, new UserPo());
            String result = userService.add(po);
            return JsonResult.success(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail();
        }
    }

    @ApiOperation(value = "user update", notes = "")
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public JsonResult add(@RequestBody UserPo param) {
        try {
            UserPo po = BeanUtil.copyParam2Entity(param, new UserPo());
            boolean result = userService.updateById(po);
            return JsonResult.success(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail();
        }
    }

    @ApiOperation(value = "user delete", notes = "")
    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public JsonResult delete(@RequestBody UserCtrParam_key param) {
        try {
            boolean result = userService.delete(param.getUserId());
            return JsonResult.success(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail();
        }
    }

    @ApiOperation(value = "user query detail", notes = "")
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public JsonResult queryDetail(@PathVariable String userId) {
        try {
            UserVo userVo = userService.queryDetailVo(userId);
            return JsonResult.success(userVo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail();
        }
    }

    @ApiOperation(value = "user query list", notes = "")
    @RequestMapping(value = "/user/list", method = RequestMethod.POST)
    public JsonResult queryList(@RequestBody UserDto userDto, @ModelAttribute PageDto pageDto) {
        try {
            Page pageList = userService.queryList(userDto, pageDto.getPageOrDefalt());
            return JsonResult.success(pageList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail();
        }
    }

}

@Data
class UserCtrParam_key {
    private String userId;
}

@Data
class UserCtrParam_entityNoKey {
    private String userAccount;
    private String userName;
}

























