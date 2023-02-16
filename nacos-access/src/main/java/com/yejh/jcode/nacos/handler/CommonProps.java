package com.yejh.jcode.nacos.handler;

import java.io.IOException;
import java.util.Properties;

public class CommonProps {

    public static final String DATA_ID_1 = "d1";
    public static final String DATA_ID_2 = "d2";
    public static final String GROUP_1 = "g1";
    public static final String GROUP_2 = "g2";

    public static String serverAddress;

    static {
        Properties props = new Properties();
        try {
            props.load(CommonProps.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        serverAddress = props.getProperty("nacos.config.server-addr");
    }
}
