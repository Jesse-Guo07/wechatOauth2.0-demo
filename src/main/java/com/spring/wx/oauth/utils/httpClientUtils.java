package com.spring.wx.oauth.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class httpClientUtils {

    /**
     * 发起一个GET请求，返回数据是以JSON形式返回
     * @param url
     * @return
     * @throws IOException
     */
    public static JSONObject doGet(String url) throws IOException {
        JSONObject jsonObject = null;

        //CloseableHttpClient是在HttpClient的基础上修改更新而来的
        // 创建客户端连接对象
        CloseableHttpClient client = HttpClients.createDefault();

        //构建get请求对象
        HttpGet httpGet = new HttpGet(url);

        //获取返回对象
        HttpResponse response = client.execute(httpGet);

        //整理返回值，获取JSON里的实例内容
        HttpEntity entity = response.getEntity();
        if (entity != null){
            String result = EntityUtils.toString(entity, "UTF-8");

            //JSON.parseObject(String text, Class clazz)，作用就是将指定的JSON字符串转换成自己的实体类的对象
            jsonObject = JSONObject.parseObject(result);
        }

        //释放httpget的链接
        httpGet.releaseConnection();
        return jsonObject;
    }
}
