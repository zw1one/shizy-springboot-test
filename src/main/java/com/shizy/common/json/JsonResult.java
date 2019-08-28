package com.shizy.common.json;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shizy.common.json.page.PageInfo;
import com.shizy.utils.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//@JsonInclude(JsonInclude.Include.NON_NULL)//在返回的json中，去掉为null的字段
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonResult implements Serializable {

    /******************************************/

    private static final long serialVersionUID = 1L;

    private Object data;

    private int code = 200;

    private Object msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PageInfo pageInfo;

    /******************************************/

//    JsonResult(Object data, int code, ){
//
//    }

    /******************************************/

    public static JsonResult success(Object data) {

        if (data instanceof Page) {
            Page pageObj = (Page) data;
            PageInfo pageInfo = new PageInfo();

            pageInfo = BeanUtil.copyParam2Entity(pageObj, new PageInfo());
            pageInfo.setPage(pageObj.getCurrent());
            pageInfo.setPageSize(pageObj.getSize());

            return new JsonResult(pageObj.getRecords(), 200, null, pageInfo);
        }

        return new JsonResult(data, 200, null, null);
    }

    public static JsonResult fail(Object msg) {
        return new JsonResult(null, 500, msg, null);
    }

    public static JsonResult fail() {
        return new JsonResult(null, 500, "server process error!", null);
    }

    /******************************************/

}

















