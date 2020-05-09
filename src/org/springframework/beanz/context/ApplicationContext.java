package org.springframework.beanz.context;

import org.springframework.beanz.factory.BeanFactory;
import org.springframework.beanz.factory.Exceptions.LocalNoSuchABeanException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URISyntaxException;

public class ApplicationContext {
    private BeanFactory beanFactory = new BeanFactory();
    public ApplicationContext(String basePackage) throws URISyntaxException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, IOException,
            InvocationTargetException, NoSuchMethodException, LocalNoSuchABeanException {
        System.out.println("---Context is under construction..---");

        beanFactory.instantiate(basePackage);
        beanFactory.populateAutowiredProperties();
        beanFactory.populatePropertiesByName();
        beanFactory.injectBeanNames();
        beanFactory.initializeBeans();
    }
    public void close() throws IllegalAccessException, LocalNoSuchABeanException, NoSuchMethodException, InstantiationException, IOException, URISyntaxException, InvocationTargetException, ClassNotFoundException {
        beanFactory.close();
        for(Object bean : beanFactory.getSingletons().values()) {
            if (bean instanceof ApplicationListener) {
                for (Type type: bean.getClass().getGenericInterfaces()){
                    if(type instanceof ParameterizedType){
                        ParameterizedType parameterizedType = (ParameterizedType) type;
                        Type firstParameter = parameterizedType.getActualTypeArguments()[0];
                        if(firstParameter.equals(ContextClosedEvent.class)){
                            Method method = bean.getClass().getMethod("onApplicationEvent", ContextClosedEvent.class);
                            method.invoke(bean, new ContextClosedEvent());
                        }
                    }
                }
            }
        }
    }
}
