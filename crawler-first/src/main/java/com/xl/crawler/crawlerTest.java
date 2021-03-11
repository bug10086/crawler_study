package com.xl.crawler;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class crawlerTest {
    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "http://www.baidu.com/";
//         get请求
//        HttpGet httpGet = new HttpGet("http://www.baidu.com");

//        // get请求，带参数
//        URIBuilder uriBuilder = new URIBuilder(url);
//        uriBuilder.setParameter("wd","zhongguo").setParameter("wd","1521654");
//        HttpGet httpGet = new HttpGet(uriBuilder.build());

//        // post请求
//        HttpPost httpPost = new HttpPost(url);

        // post 请求，带参数
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("wd","ssasd"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list,"utf8");
        httpPost.setEntity(formEntity);

//        // 配置请求信息
//        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000)  //设置创建连接的最长时间
//                .setConnectionRequestTimeout(500)       //设置获取连接的最长时间
//                .setSocketTimeout(10 * 1000)              //设置数据传输的最长时间
//                .build();
//        httpGet.setConfig(config);

        CloseableHttpResponse response = httpClient.execute(httpPost);
//        CloseableHttpResponse response = httpClient.execute(httpGet);

        if(response.getStatusLine().getStatusCode() == 200){
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity, "utf8");
            System.out.println(content.length());
        }
    }
}
