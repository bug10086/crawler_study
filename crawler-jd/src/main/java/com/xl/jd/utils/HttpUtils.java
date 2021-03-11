package com.xl.jd.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class HttpUtils {

    private PoolingHttpClientConnectionManager manager;

    public HttpUtils(){
        this.manager = new PoolingHttpClientConnectionManager();
        this.manager.setMaxTotal(100);
        this.manager.setDefaultMaxPerRoute(10);
    }

    /**
     * 根据请求地址下载页面数据
     * @param url
     * @return 页面数据
     */
    public String doGetHtml(String url){
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(this.manager).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");

        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200){
                if(response.getEntity()!=null){
                    String content = EntityUtils.toString(response.getEntity(),"utf8");
                    return content;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 下载图片
     * @param url
     * @return 图片名称
     */
    public String doGetImage(String url){
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(this.manager).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");


        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200){
                if(response.getEntity()!=null){
                    // 获取图片后缀
                    String exName = url.substring(url.lastIndexOf("."));
                    // 重命名图片名
                    String picName = UUID.randomUUID().toString() + exName;
                    // 下载图片
                    FileOutputStream outputStream = new FileOutputStream(new File("E:\\IDEAprojects\\crawler-jd\\images\\" + picName));
                    response.getEntity().writeTo(outputStream);
                    // 返回文件名
                    return picName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private RequestConfig setConfig(){
        return RequestConfig.custom().setSocketTimeout(10 * 1000)
                .setConnectionRequestTimeout(500)
                .setConnectTimeout(1000)
                .build();
    }

}
