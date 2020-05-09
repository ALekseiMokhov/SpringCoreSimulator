package org.springframework.beanz.factory;

import org.springframework.beanz.factory.Annotations.Autowired;
import org.springframework.beanz.factory.Annotations.PostConstruct;
import org.springframework.beanz.factory.Annotations.PreDestroy;
import org.springframework.beanz.factory.Annotations.Resource;
import org.springframework.beanz.factory.Exceptions.LocalNoSuchABeanException;
import org.springframework.beanz.factory.PostProcessors.PostConstructPostProcessor;
import org.springframework.beanz.factory.Stereotype.Component;
import org.springframework.beanz.factory.Stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class BeanFactory {

    private Map <String, Object> singletons = new HashMap <>();
    private List <BeanPostProcessor> postProcessors = new ArrayList <>();

    public void addPostProcessor(BeanPostProcessor postProcessor) {
        postProcessors.add( postProcessor );
    }

    public Object getBean(String beanName) {
        return singletons.get( beanName );
    }
    public Map <String, Object> getSingletons() {
        return singletons;
    }


    public void instantiate(String basePackage) throws IOException, URISyntaxException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        String path = basePackage.replace( '.', '/' );
        Enumeration <URL> resources = classLoader.getResources( path );
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File( resource.toURI() );
            for (File f : file.listFiles()) {
                String fileName = f.getName();
                if (fileName.endsWith( ".class" )) {
                    String className = fileName.substring( 0, fileName.lastIndexOf( "." ) );
                    Class classObject = Class.forName( basePackage + "." + className );
                    if (classObject.isAnnotationPresent( Component.class ) ||
                            classObject.isAnnotationPresent( Service.class )) {
                        System.out.println( "Bean: " + classObject );

                        Object instance = classObject.newInstance();//=new CustomClass()
                        String beanName = className.substring( 0, 1 ).toLowerCase() + className.substring( 1 );
                        singletons.put( beanName, instance );
                        System.out.println( singletons.entrySet() );
                    }
                }
            }
        }

    }

    public void populateAutowiredProperties() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        System.out.println( "==populateAutowiredProperties==" );
        for (Object object : singletons.values()) {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent( Autowired.class )) {
                    for (Object dependency : singletons.values()) {
                        if (dependency.getClass().equals( field.getType() )) {
                            String setterName = "set" + field.getName().substring( 0, 1 ).toUpperCase() +
                                    field.getName().substring( 1 );//setPromotionsService
                            System.out.println( "Setter name = " + setterName );
                            Method setter = object.getClass().getMethod( setterName, dependency.getClass() );
                            setter.invoke( object, dependency );
                        }
                    }
                }
            }
        }
    }

    public void populatePropertiesByName() throws LocalNoSuchABeanException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println( "==populateNamedProperties==" );
        for (Object object : singletons.values()) {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent( Resource.class )) {
                    String annotatedName = field.getAnnotation( Resource.class ).name();
                    for (String beanName : singletons.keySet()) {
                        if (beanName.equals( annotatedName )) {
                            String setterName = "set" + beanName.substring( 0, 1 ).toUpperCase() +
                                    beanName.substring( 1 );//setPromotionsService
                            System.out.println( "Setter name = " + setterName );
                            Method setter = object.getClass().getMethod( setterName, singletons.get( beanName ).getClass() );
                            setter.invoke( object, singletons.get( beanName ) );
                        }
                    }
                }
            }
        }
    }

    public void injectBeanNames() {
        for (String name : singletons.keySet()) {
            Object bean = singletons.get( name );
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName( name );
            }
        }
    }

    public void postConstructingPostProcessorInit() {
        {
            for (Object bean: singletons.values()){
                for (Method method : bean.getClass().getMethods()) {
                    if (method.isAnnotationPresent( PostConstruct.class )) {
                        try {
                            method.invoke(bean);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void initializeBeans() {
        for (String name : singletons.keySet()) {
            Object bean = singletons.get( name );
            for (BeanPostProcessor postProcessor : postProcessors) {
                postProcessor.postProcessBeforeInitialization( bean, name );
            }
            if (bean instanceof InitializingBean) {
                ((InitializingBean) bean).afterPropertiesSet();
            }
            for (BeanPostProcessor postProcessor : postProcessors) {
                postProcessor.postProcessAfterInitialization( bean, name );
            }
        }
    }
    public void close(){
        for (Object bean: singletons.values()) {
            for (Method method : bean.getClass().getMethods()) {
                if (method.isAnnotationPresent( PreDestroy.class )) {
                    try {
                        method.invoke(bean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                if (bean instanceof DisposableBean) {
                    ((DisposableBean) bean).destroy();
                }
            }
        }
    }
}



