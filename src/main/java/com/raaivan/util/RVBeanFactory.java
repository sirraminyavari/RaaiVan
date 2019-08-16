package com.raaivan.util;

import org.springframework.context.ApplicationContext;

public class RVBeanFactory {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        if(RVBeanFactory.applicationContext == null) RVBeanFactory.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> tClass) {
        try {
            return applicationContext.getBean(tClass);
        }catch (Exception ex){
            return null;
        }
    }
}
