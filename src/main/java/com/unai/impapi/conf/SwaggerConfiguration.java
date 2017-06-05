package com.unai.impapi.conf;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.google.common.base.Predicates;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Controller
public class SwaggerConfiguration {
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.unai.impapi.rest"))
				.paths(Predicates.not(PathSelectors.regex("/error")))
				.build()
				.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
		Contact contact = new Contact("Unai P. Mendizabal", "https://github.com/unaipme", "unaipme@gmail.com");
		return new ApiInfo("imPAPI",
					"Stands for Internet Movie Public API, which is what it is: A public API for the IMDb, based on web scraping.",
					"",
					"",
					contact,
					"",
					"",
					Arrays.asList());
	}
	
	@GetMapping("/docs")
	public RedirectView redirectToDocs() {
		return new RedirectView("/swagger-ui.html");
	}
	
}
