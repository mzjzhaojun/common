package com.yt.app.frame.d;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Ac {
	@Bean
	public Docket b() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(c()).select().apis(RequestHandlerSelectors.basePackage("com.yt.app"))
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo c() {
		return new ApiInfoBuilder().title("API").description("").termsOfServiceUrl("").contact("zj").version("1.0").build();
	}
}