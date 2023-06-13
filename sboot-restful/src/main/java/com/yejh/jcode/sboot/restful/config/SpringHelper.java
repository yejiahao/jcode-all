package com.yejh.jcode.sboot.restful.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取 Spring 上下文
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2021-11-27
 * @since 1.1.0
 */
@Component
public class SpringHelper implements ApplicationContextAware {

    private static ApplicationContext context;

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringHelper.context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }
}
