package home.hammefatal.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication	// @SpringBootApplication = @SpringBootConfiguration + @EnableAutoConfiguration + @ComponentScan
						// @SpringBootConfiguration = @Configuration = @Component
						// @EnableAutoConfiguration = @AutoConfigurationPackage + @Import(AutoConfigurationImportSelector.class)
@ServletComponentScan
public class SpringbootApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringbootApplication.class, args);
		String[] beanNames = context.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		Arrays.stream(beanNames).forEach(System.out::println);
	}

}
