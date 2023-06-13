package com.yejh.jcode.base.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 调用 cmd 工具命令（windows 系统）
 * <a href="https://eternallybored.org/misc/netcat/netcat-win32-1.12.zip">netcat</a> 解压后，把 nc64.exe -> nc.exe 复制到 C:\Windows\System32 目录
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2023-05-08
 * @since x.y.z
 */
@Slf4j
public class CmdUtil {

    private CmdUtil() {
        throw new AssertionError();
    }

    /**
     * 执行查询
     */
    // echo appid:1081087509, uid:9887, token:upush:ApKakDvkn7q4lSjnit7LZUtu2GCCwNwMrswlj2zJ2ieg, pushid:9887, date:2023-05-08, | nc vm0.yejh.cn 9090
    private static List<String> execute(String cmd) throws IOException, InterruptedException {
        String[] cmdArray = {"cmd.exe", "/C", cmd};
        String lineMsg;
        List<String> list = new ArrayList<>();
        Process process = Runtime.getRuntime().exec(cmdArray);
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(process.getInputStream(), "GB18030"))) {
            while (Objects.nonNull(lineMsg = bf.readLine())) {
                list.add(lineMsg);
            }
        }
        int value = process.waitFor();
        log.info("process return: {}", value);
        process.destroy();
        return list;
    }
}
