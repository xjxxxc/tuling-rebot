package com.xjxxxc.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author: Jason Xu
 * @Date: 2019年3月18日
 * @description: 图灵机器人请求工具类
 */
public class TuLingApiUtil {
    //自己的apikey，注意不是密钥（请替换成自己的）
    @Value("${tuling.api.apiKey}")
    private static String key ;

    @Value("${tuling.api.apiUrl}")
    private static String apiUrl = "";

    /**
     * @param content .
     * @return String
     */
    public static String getResult(String content) {
        //图灵api接口
        String apiUrl = "http://www.tuling123.com/openapi/api?key=" + key + "&info=";

        try {
            //内容需要utf-8编码(官方文档中有说明)
            content = URLEncoder.encode(content, "utf-8");
            //拼接url
            apiUrl = apiUrl + content;

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        //封装请求头
        HttpGet request = new HttpGet(apiUrl);

        String result = "";

        try {
            //发送http请求
            HttpResponse response = HttpClients.createDefault().execute(request);

            //获取响应码
            int code = response.getStatusLine().getStatusCode();

            if (code == 200) {
                //获取返回的结果
                result = EntityUtils.toString(response.getEntity());
                System.out.println(result);
            } else {
                System.out.println("code=" + code);
                System.out.println("该功能等待开发...");
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }

}
 