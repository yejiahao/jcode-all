package com.yejh.jcode.base.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * xml字符串与实体互转工具类
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2019-09-04
 * @since 1.0.0
 */
public class XStreamUtil {

    private static XStream stream1 = new XStream(new DomDriver());
    // 解决下划线问题
    private static XStream stream2 = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));

    private XStreamUtil() {
        throw new AssertionError();
    }

    public static <T> T xml2Object(String xml, Class<T> cls) {
        stream1.processAnnotations(cls);
        return (T) stream1.fromXML(xml);
    }

    public static String object2Xml(Object obj) {
        stream2.processAnnotations(obj.getClass());
        stream2.autodetectAnnotations(true); // 自动检测模式，默认
        return stream2.toXML(obj);
    }
}
