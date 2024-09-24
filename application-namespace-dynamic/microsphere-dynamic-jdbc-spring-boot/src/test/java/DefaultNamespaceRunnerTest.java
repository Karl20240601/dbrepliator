import org.junit.jupiter.api.Test;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.test.context.support.TestPropertySourceUtils;


public class DefaultNamespaceRunnerTest {

    @Test
    public void  test1() throws Exception{
        StandardEnvironment standardEnvironment = new StandardEnvironment();
        TestPropertySourceUtils.addPropertiesFilesToEnvironment(standardEnvironment,new DefaultResourceLoader(),"classpath:/application.yml");
    }
}
