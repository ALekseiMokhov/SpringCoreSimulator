package com.gmail.qwertygoog;

import org.springframework.beanz.factory.BeanNameAware;
import org.springframework.beanz.factory.Stereotype.Component;

@Component
public class PromotionService implements BeanNameAware {
    private String beanName;

    public void setBeanName(String name) {
        beanName = name;
    }

    public String getBeanName() {
        return beanName;
    }
}
