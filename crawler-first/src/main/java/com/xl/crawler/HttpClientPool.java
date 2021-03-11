package com.xl.crawler;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientPool {

    public static void main(String[] args) {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();

        // 设置最大连接数
        manager.setMaxTotal(100);
        // 设置每个主机的最大连接数
        manager.setDefaultMaxPerRoute(10);

        doget(manager);
        doget(manager);
        doget(manager);
    }

    private static void doget(PoolingHttpClientConnectionManager manager) {
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(manager).build();

        HttpGet httpGet = new HttpGet("http://www.baidu.com");

        CloseableHttpResponse response = null;

        try {
            response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString(entity, "utf8");
                System.out.println(content.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
