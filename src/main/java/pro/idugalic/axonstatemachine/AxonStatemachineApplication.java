package pro.idugalic.axonstatemachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class AxonStatemachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(AxonStatemachineApplication.class, args);
	}

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("pro.idugalic.axonstatemachine.web"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(getApiInfo());
	}

	private ApiInfo getApiInfo() {
		return new ApiInfo(
				"Axon State Machine",
				"Axon Spring Boot application - Finite State Machine",
				"1.0.0-SNAPSHOT",
				"Terms of Service",
				new Contact("Ivan Dugalic", "https://github.com/idugalic", "idugalic@gmail.com"),
				"",
				"",
				Collections.emptyList());
	}

}
