package se.magnus.microservices.composite.product;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.WebClient;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@ComponentScan("se.magnus")
public class ProductCompositeServiceApplication {
    @Value("${api.common.version}")           String apiVersion;
    @Value("${api.common.title}")             String apiTitle;
    @Value("${api.common.description}")       String apiDescription;
    @Value("${api.common.termsOfServiceUrl}") String apiTermsOfServiceUrl;
    @Value("${api.common.license}")           String apiLicense;
    @Value("${api.common.licenseUrl}")        String apiLicenseUrl;
    @Value("${api.common.contact.name}")      String apiContactName;
    @Value("${api.common.contact.url}")       String apiContactUrl;
    @Value("${api.common.contact.email}")     String apiContactEmail;

	/**
	 * Will exposed on $HOST:$PORT/swagger-ui.html
	 *
	 * @return
	 */
	@Bean
	public Docket apiDocumentation() {

		return new Docket(SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("se.magnus.microservices.composite.product"))
			.paths(PathSelectors.any())
			.build()
                .globalResponseMessage(RequestMethod.GET, Collections.emptyList())
				.apiInfo(new ApiInfo(
                    apiTitle,
                    apiDescription,
                    apiVersion,
                    apiTermsOfServiceUrl,
                    new Contact(apiContactName, apiContactUrl, apiContactEmail),
                    apiLicense,
                    apiLicenseUrl,
                    Collections.emptyList()
                ));
    }
// ch09	
	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
		final WebClient.Builder builder = WebClient.builder();
		return builder;
	}
// ch09	
	
//CH 07 start	
//	@Bean
//	RestTemplate restTemplate() {
//		return new RestTemplate();
//	}

	public static void main(String[] args) {
		SpringApplication.run(ProductCompositeServiceApplication.class, args);
	}
	
    
//	@Autowired
//	HealthAggregator healthAggregator;
//
//	@Autowired
//	ProductCompositeIntegration integration;
//
//	@Bean
//	ReactiveHealthIndicator coreServices() {
//
//		ReactiveHealthIndicatorRegistry registry = new DefaultReactiveHealthIndicatorRegistry(new LinkedHashMap<>());
//
//		registry.register("product", () -> integration.getProductHealth());
//		registry.register("recommendation", () -> integration.getRecommendationHealth());
//		registry.register("review", () -> integration.getReviewHealth());
//
//		return new CompositeReactiveHealthIndicator(healthAggregator, registry);
//	}	
	//	CH 07 END
}
