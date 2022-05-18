package redpanda.lu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableJpaAuditing
@SpringBootApplication
public class LuApplication {


	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.properties"
			+ ", classpath:application-amazons3.properties"
			+",classpath:application-oauth2.properties";


	public static void main(String[] args) {
		new SpringApplicationBuilder(LuApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}



}
