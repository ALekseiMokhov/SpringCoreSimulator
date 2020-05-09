import com.gmail.qwertygoog.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beanz.factory.BeanFactory;
import org.springframework.beanz.factory.Exceptions.LocalNoSuchABeanException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class TestBeanFactory {
    private BeanFactory beanFactory;
    private ProductService productService;
    private final String PATH = "com.gmail.qwertygoog";
    @Before
    public void init() throws URISyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        beanFactory = new BeanFactory();
        beanFactory.instantiate(PATH);
    }
    @Test
    public void testBeanFactoryInit(){
        productService = (ProductService)beanFactory.getBean( "productService" );
        Assert.assertNotEquals( null,productService );
    }
    @Test
    public void testBeanPropertyInjectByType() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        productService = (ProductService)beanFactory.getBean( "productService" );
        beanFactory.populateAutowiredProperties();
        Assert.assertNotEquals( null,productService.getPromotionService() );
    }
    @Test
    public void testBeanPropertyInjectByName() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, LocalNoSuchABeanException {
        productService = (ProductService)beanFactory.getBean( "productService" );
        beanFactory.populatePropertiesByName();
        Assert.assertNotEquals( null,productService.getPromotionService() );
    }

}
