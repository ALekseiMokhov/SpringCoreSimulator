package org.springframework.beanz.factory.PostProcessors;

import org.springframework.beanz.factory.BeanPostProcessor;

public class PostConstructPostProcessor implements BeanPostProcessor{
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("Postconstructing bean:" + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return null;
    }
}
