package shizy.junit.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shizy.utils.http.HttpUtil;
import org.junit.Assert;
import org.junit.Test;
import shizy.junit.TestParam;

import java.util.HashMap;
import java.util.Map;

public class UserTest {

    @Test
    public void crudTest() {
        String id = crudAdd();
        update(id);
        queryDetail(id);
        queryList();
        delete(id);
    }

    private String crudAdd() {

        String url = TestParam.host + "/api/user/add";

        Map bodyParam = new HashMap();
        bodyParam.put("userAccount", "junit test add [userAccount]");
        bodyParam.put("userName", "junit test add [userName]");

        Map headers = new HashMap();
        headers.put("content-type", "application/json");

        String result = HttpUtil.post(url, bodyParam, null, headers);
        System.out.println(result);

        JSONObject rstObj = JSON.parseObject(result);
        int code = (int) rstObj.get("code");
        Assert.assertEquals(code, 200);
        String id = (String) rstObj.get("data");
        Assert.assertNotNull(id);

        return id;
    }

    private void update(String id) {

        String url = TestParam.host + "/api/user/update";

        Map bodyParam = new HashMap();
        bodyParam.put("userAccount", "junit test add [userAccount]");
        bodyParam.put("userName", "junit test add [userName]");
        bodyParam.put("userId", id);

        Map headers = new HashMap();
        headers.put("content-type", "application/json");

        String result = HttpUtil.post(url, bodyParam, null, headers);
        System.out.println(result);

        JSONObject rstObj = JSON.parseObject(result);
        int code = (int) rstObj.get("code");
        Assert.assertEquals(code, 200);

        Boolean resultFlag = (Boolean) rstObj.get("data");
        Assert.assertTrue(resultFlag);
    }

    private void queryDetail(String id) {

        String url = TestParam.host + "/api/user/" + id;

        String result = HttpUtil.get(url, null, null);
        System.out.println(result);

        JSONObject rstObj = JSON.parseObject(result);
        int code = (int) rstObj.get("code");
        Assert.assertEquals(code, 200);
        String userName = ((JSONObject) rstObj.get("data")).get("userName").toString();
        Assert.assertNotNull(userName);
    }

    private void queryList() {

        String url = TestParam.host + "/api/user/list";

        Map bodyParam = new HashMap();
//        bodyParam.put("userName", "啊啊");

        Map urlParam = new HashMap();
        urlParam.put("page", "1");
        urlParam.put("pageSize", "5");

        Map headers = new HashMap();
        headers.put("content-type", "application/json");

        String result = HttpUtil.post(url, bodyParam, urlParam, headers);
        System.out.println(result);

        JSONObject rstObj = JSON.parseObject(result);

        int code = (int) rstObj.get("code");
        Assert.assertEquals(code, 200);
        JSONArray data = rstObj.getJSONArray("data");
        Assert.assertTrue(data.size() >= 0);
    }

    private void delete(String id) {

        String url = TestParam.host + "/api/user/delete";

        Map bodyParam = new HashMap();
        bodyParam.put("userId", id);

        Map headers = new HashMap();
        headers.put("content-type", "application/json");

        String result = HttpUtil.post(url, bodyParam, null, headers);
        System.out.println(result);

        JSONObject rstObj = JSON.parseObject(result);

        int code = (int) rstObj.get("code");
        Assert.assertEquals(code, 200);
        Boolean data = (Boolean) rstObj.get("data");
        Assert.assertTrue(data);
    }


}















