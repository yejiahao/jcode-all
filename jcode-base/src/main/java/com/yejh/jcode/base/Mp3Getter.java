package com.yejh.jcode.base;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mp3歌曲爬取
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2020-06-06
 * @since 1.1.0
 */
@Slf4j
public class Mp3Getter {

    private static final String URI = "http://mp3.jiuku.9ku.com/mp3/199/";

    private static final File DIRECTORY = new File("C:\\Users\\Ye Jiahao\\Desktop\\song");

    @SuppressWarnings("FieldMayBeFinal")
    private static ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4, new ThreadFactory() {
        private final AtomicInteger cnt = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "t-" + cnt.incrementAndGet());
        }
    });

    static {
        if (!DIRECTORY.isDirectory()) {
            DIRECTORY.mkdirs();
        }
    }

    public static void main(String[] args) {
        int from = 198_000;
        int size = 1000;
        for (int i = from; i < from + size; i++) {
            int finalI = i;
            es.execute(() -> sendMp3Get(finalI));
        }
        es.shutdown();
    }

    private static void sendMp3Get(int id) {
        long start = System.currentTimeMillis();
        HttpURLConnection urlConnection;
        try {
            // 打开和URL之间的连接
            urlConnection = (HttpURLConnection) new URL(URI + id + ".mp3").openConnection();
            // 设置请求属性
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("accept", "*/*");
            urlConnection.setRequestProperty("connection", "Keep-Alive");
            urlConnection.setRequestProperty("Content-Type", "UTF-8");
            // 建立实际的连接
            urlConnection.connect();
            // 获取返回状态码
            int respCode = urlConnection.getResponseCode();
            if (respCode / 100 != 2) {
                long end = System.currentTimeMillis();
                log.error("abnormal respCode: [{}], id: [{}], cost: [{}] sec(s)", respCode, id, (end - start) / 1000F);
                return;
            }
            // 遍历所有的响应头字段
            if (log.isDebugEnabled()) {
                urlConnection.getHeaderFields().forEach((k, v) -> log.debug("resp header: {} ---> {}", k, v));
            }
        } catch (IOException e) {
            log.error("getConnection exception: {}", e.getMessage(), e);
            return;
        }
        try (InputStream is = urlConnection.getInputStream();
             FileOutputStream fos = new FileOutputStream(new File(DIRECTORY, String.format("%s.mp3", id)))) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error("read write stream exception: {}", e.getMessage(), e);
            return;
        }
        long end = System.currentTimeMillis();
        log.info("download completed, id: [{}], cost: [{}] sec(s)", id, (end - start) / 1000F);
    }

}
