package org.j2os.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Spring {
    private Spring(){}
    private static final ApplicationContext  APPLICATION_CONTEXT
            = new ClassPathXmlApplicationContext("spring.xml");
    public static Object getBean(String beanName) {
        return APPLICATION_CONTEXT.getBean(beanName);
    }
}
