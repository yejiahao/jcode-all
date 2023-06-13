package com.yejh.jcode.base.http;

import com.yejh.jcode.base.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2021-12-14
 * @since 2.0.0
 */
@Slf4j
public class OkHttpUtil {

    private static OkHttpClient client = new OkHttpClient().newBuilder().build();

    private static OkHttpClient client11 = new OkHttpClient().newBuilder()
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .connectTimeout(15L, TimeUnit.SECONDS)
            .readTimeout(15L, TimeUnit.SECONDS)
            .build();

    private OkHttpUtil() {
        throw new AssertionError();
    }

    public static Tuple3<Integer, Headers, String> get(String url, Map<String, String> headers) {
        return get(url, headers, StringUtils.EMPTY, false);
    }

    public static Tuple3<Integer, Headers, String> get(String url, Map<String, String> headers, boolean async) {
        return get(url, headers, StringUtils.EMPTY, async);
    }

    public static Tuple3<Integer, Headers, String> get(String url, Map<String, String> headers, String reqParams, boolean async) {
        log.debug("request url: {}", url);

        try {
            URIBuilder ub = new URIBuilder(url);
            if (StringUtils.isNotBlank(reqParams)) {
                Map<String, Object> paramsMap = JsonUtil.deserialize2Object(reqParams, Map.class);
                for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                    String k = entry.getKey();
                    Object v = entry.getValue();
                    ub.addParameter(k, v instanceof String ? (String) v : JsonUtil.serialize2String(v));
                }
            }
            URL url0 = ub.build().toURL();

            Headers.Builder headerBuilder = new Headers.Builder();
            headers.forEach(headerBuilder::add);

            Request request = new Request.Builder()
                    .url(url0)
                    .headers(headerBuilder.build())
                    .get()
                    .build();

            log.debug("request header: {}", request.headers());

            if (async) {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        log.error("exception: {}", e.getMessage(), e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        printResp(response);
                    }
                });
                return Tuples.of(-1, Headers.of(), StringUtils.EMPTY);
            }

            try (Response response = client.newCall(request).execute()) {
                return printResp(response);
            }

        } catch (Exception e) {
            log.error("exception: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static Tuple3<Integer, Headers, String> post(String url, Map<String, String> headers, String reqEntity) {
        return post(url, headers, reqEntity, "application/json; charset=UTF-8", false);
    }

    public static Tuple3<Integer, Headers, String> post(String url, Map<String, String> headers, String reqEntity, String mediaType) {
        return post(url, headers, reqEntity, mediaType, false);
    }

    public static Tuple3<Integer, Headers, String> post(String url, Map<String, String> headers, String reqEntity, boolean async) {
        return post(url, headers, reqEntity, "application/json; charset=UTF-8", async);
    }

    public static Tuple3<Integer, Headers, String> post(String url, Map<String, String> headers, String reqEntity, String mediaType, boolean async) {
        log.debug("request url: {}", url);

        try {
            Headers.Builder headerBuilder = new Headers.Builder();
            headers.forEach(headerBuilder::add);

            RequestBody jsonBody = RequestBody.create(MediaType.parse(mediaType), reqEntity);
            Request request = new Request.Builder()
                    .url(url)
                    .headers(headerBuilder.build())
                    .post(jsonBody)
                    .build();

            log.debug("request header: {}", request.headers());
            log.debug("request body: {}", reqEntity);

            if (async) {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        log.error("exception: {}", e.getMessage(), e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        printResp(response);
                    }
                });
                return Tuples.of(-1, Headers.of(), StringUtils.EMPTY);
            }

            try (Response response = client.newCall(request).execute()) {
                return printResp(response);
            }

        } catch (Exception e) {
            log.error("exception: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private static Tuple3<Integer, Headers, String> printResp(Response response) throws IOException {
        int code = response.code();
        Headers headers = response.headers();
        log.debug("--------------------------------------");
        log.debug("response protocol: {}, code: {}", response.protocol(), code);
        log.debug("response headers: {}", headers);
        String result = Objects.isNull(response.body()) ? StringUtils.EMPTY : response.body().string();
        log.debug("response body: {}", result);
        log.debug("--------------------------------------");
        return Tuples.of(code, headers, result);
    }
}
