package ru.mos.eirc.log.searcher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    /**
     * @return /swagger-ui/index.html
     */
    @Bean
    public Docket api() {
	return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(PathSelectors.regex("/api/.*"))
			.build()
			.apiInfo(apiInfo());
    }
    /**
     * @return Описание Swagger
     */
    private ApiInfo apiInfo() {
	return new ApiInfoBuilder().title("LogSearcher Application")
			.description("REST/SOAP application with Swagger")
			.version("1.0")
			.build();
    }
}