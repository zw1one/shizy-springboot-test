package com.shizy.utils.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shizy.utils.format.FormatUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class HttpUtil {

    /**
     * @param url
     * @param bodyParam
     * @param urlParam
     * @param headers
     * @return
     */
    public static String post(String url, Map bodyParam, Map urlParam, Map<String, String> headers) {

        CloseableHttpClient httpClient = null;
        try {
            StringBuilder uri = new StringBuilder(url);

            if (urlParam != null && !urlParam.isEmpty()) {
                uri.append("?");
                for (Map.Entry entry : (Set<Map.Entry>) urlParam.entrySet()) {
                    uri.append(URLEncoder.encode(entry.getKey().toString(), "UTF-8"));
                    uri.append("=");
                    if (entry.getValue() != null) {
                        uri.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                    }
                    uri.append("&");
                }
            }

            HttpPost httpPost = new HttpPost(uri.toString());
            UrlEncodedFormEntity formEntity = null;

            httpPost.setEntity(new StringEntity(JSON.toJSONString(bodyParam), ContentType.APPLICATION_JSON));

            for (Map.Entry entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey().toString(), entry.getValue().toString());
            }

            httpClient = HttpClients.custom()
                    .evictExpiredConnections()
                    .evictIdleConnections(30, TimeUnit.SECONDS)
                    .build();

            HttpResponse httpresponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpresponse.getEntity();
            return EntityUtils.toString(httpEntity, "utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static String get(String url, Map urlParam, Map<String, String> headers) {

        CloseableHttpClient httpClient = null;
        try {
            StringBuilder uri = new StringBuilder(url);

            if (urlParam != null && !urlParam.isEmpty()) {
                uri.append("?");
                for (Map.Entry entry : (Set<Map.Entry>) urlParam.entrySet()) {
                    uri.append(URLEncoder.encode(entry.getKey().toString(), "UTF-8"));
                    uri.append("=");
                    if (entry.getValue() != null) {
                        uri.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                    }
                    uri.append("&");
                }
            }

            HttpGet httpGet = new HttpGet(uri.toString());

            if(headers != null){
                for (Map.Entry entry : headers.entrySet()) {
                    httpGet.addHeader(entry.getKey().toString(), entry.getValue().toString());
                }
            }

            httpClient = HttpClients.custom()
                    .evictExpiredConnections()
                    .evictIdleConnections(30, TimeUnit.SECONDS)
                    .build();

            HttpResponse httpresponse = httpClient.execute(httpGet);

            HttpEntity httpEntity = httpresponse.getEntity();
            return EntityUtils.toString(httpEntity, "utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 获得request的请求头信息string，用于展示
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getHeadersInfo(HttpServletRequest request) throws IOException {

        Map<String, String> headerMap = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            headerMap.put(key, value);
        }

        Map<String, String> paramMap = new HashMap<String, String>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String key = (String) paramNames.nextElement();
            String value = request.getParameter(key);
            paramMap.put(key, value);
        }

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("headers", headerMap);
        jsonMap.put("RequestURI", request.getRequestURI());
        jsonMap.put("RequestMethod", request.getMethod());
        jsonMap.put("QueryString", request.getQueryString());
        jsonMap.put("Parameters", paramMap);
//        jsonMap.put("InputStreamData", StreamUtil.read(request.getInputStream()));//这个流只能读一次

        return "\r\n" + FormatUtil.formatJson(JSONObject.toJSON(jsonMap).toString());
    }

}
