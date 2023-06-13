package com.yejh.jcode.base.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2021-11-09
 * @since 1.0.0
 */
@Slf4j
public class HttpClientTest {

    public static String get(String url, Map<String, String> entityMap, String charset) throws IOException {
        String result = "";

        StringBuilder sb = new StringBuilder();

        entityMap.forEach((k, v) -> {
            if (sb.length() == 0) {
                sb.append(url).append("?");
            } else {
                sb.append("&");
            }
            try {
                sb.append(k).append("=").append(URLEncoder.encode(v, charset));
            } catch (UnsupportedEncodingException e) {
                log.error("exception: {}", e.getMessage(), e);
            }
        });
        // 创建 httpGet.
        HttpGet httpget = new HttpGet(sb.toString());
        log.info("request url: {}", httpget.getURI());

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 执行get请求
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                log.info("------------------------------------");
                // 打印响应状态
                log.info("{}", response.getStatusLine());
                if (Objects.nonNull(entity)) {
                    result = EntityUtils.toString(entity, charset);
                    // 打印响应内容长度
                    log.info("response content length: {}", entity.getContentLength());
                    // 打印响应内容
                    log.info("response content: {}", result);
                }
                log.info("------------------------------------");
            }
        }
        return result;
    }

    public static String post(String url, Map<String, String> entityMap, String charset) throws IOException {
        String result = "";
        // 创建httpPost
        HttpPost httppost = new HttpPost(url);
        log.info("request url: {}", httppost.getURI());
        // 创建参数队列
        List<NameValuePair> formParams = new ArrayList<>();

        entityMap.forEach((k, v) -> formParams.add(new BasicNameValuePair(k, v)));
        log.info("request body: {}", formParams);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            httppost.setEntity(new UrlEncodedFormEntity(formParams, charset));
            // 执行post请求
            try (CloseableHttpResponse response = httpClient.execute(httppost)) {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                log.info("--------------------------------------");
                // 打印响应状态
                log.info("{}", response.getStatusLine());
                if (Objects.nonNull(entity)) {
                    result = EntityUtils.toString(entity);
                    // 打印响应内容长度
                    log.info("response content length: {}", entity.getContentLength());
                    // 打印响应内容
                    log.info("response content: {}", result);
                }
                log.info("--------------------------------------");
            }
        }
        return result;
    }

}
