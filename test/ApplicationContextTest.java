import org.junit.Test;
import org.springframework.beanz.context.ApplicationContext;
import org.springframework.beanz.factory.Exceptions.LocalNoSuchABeanException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class ApplicationContextTest {
    @Test
    public void testApplicationContext() throws IllegalAccessException, LocalNoSuchABeanException, NoSuchMethodException, InstantiationException, IOException, URISyntaxException, InvocationTargetException, ClassNotFoundException {
        ApplicationContext context = new ApplicationContext("com.gmail.qwertygoog"  );
        context.close();
    }

}
