package se.magnus.microservices.core.recommendation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("se.magnus")
@EnableMongoRepositories ("se.magnus.microservices.core.recommendation.persistence") 
@EnableJpaRepositories("seconds47.repository")
public class RecommendationServiceApplication {
	private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceApplication.class);
	
	public static void main(String[] args) {
		LOG.info("Connected to MongoDb: Start");

		ConfigurableApplicationContext ctx = SpringApplication.run(RecommendationServiceApplication.class, args);

		String mongodDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
		String mongodDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
		LOG.info("Connected to MongoDb: " + mongodDbHost + ":" + mongodDbPort);
	}
}
