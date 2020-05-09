package com.gmail.qwertygoog;

import org.springframework.beanz.factory.BeanFactory;
import org.springframework.beanz.factory.Exceptions.LocalNoSuchABeanException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException, URISyntaxException, InstantiationException, IllegalAccessException, NoSuchMethodException, LocalNoSuchABeanException, InvocationTargetException {
    BeanFactory beanFactory = new BeanFactory();
    beanFactory.instantiate( "com.gmail.qwertygoog" );  /* Setting classpath for scanning*/
    ProductService productService = (ProductService) beanFactory.getBean("productService");
    System.out.println(productService);
    beanFactory.populatePropertiesByName();/* Creating beans by name*/
    beanFactory.populateAutowiredProperties(); /*Creating beans by type*/
    beanFactory.injectBeanNames();
    beanFactory.postConstructingPostProcessorInit();/*Performing @PostConstruct annotated beans processing*/
    beanFactory.initializeBeans(); /*Initializing beans*/
    }
}
