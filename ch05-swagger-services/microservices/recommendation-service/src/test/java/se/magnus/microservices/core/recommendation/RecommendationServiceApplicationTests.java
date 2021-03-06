package se.magnus.microservices.core.recommendation;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)
public class RecommendationServiceApplicationTests {

	@Autowired
	private WebTestClient client;

	@Test
	public void getRecommendationsByProductId() {

		int productId = 1;

		client.get()
			.uri("/recommendation?productId=" + productId)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
//			.expectHeader().contentType(APPLICATION_JSON_UTF8)
			.expectBody()
			.jsonPath("$.length()").isEqualTo(3)
			.jsonPath("$[0].productId").isEqualTo(productId);
	}

	@Test
	public void getRecommendationsMissingParameter() {

		client.get()
			.uri("/recommendation")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(BAD_REQUEST)
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.path").isEqualTo("/recommendation")
			.jsonPath("$.message").isEqualTo("Required int parameter 'productId' is not present");
	}

	@Test
	public void getRecommendationsInvalidParameter() {

		client.get()
			.uri("/recommendation?productId=no-integer")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(BAD_REQUEST)
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.path").isEqualTo("/recommendation")
			.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getRecommendationsNotFound() {

		int productIdNotFound = 113;

		client.get()
			.uri("/recommendation?productId=" + productIdNotFound)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.length()").isEqualTo(0);
	}

	@Test
	public void getRecommendationsInvalidParameterNegativeValue() {

		int productIdInvalid = -1;

		client.get()
			.uri("/recommendation?productId=" + productIdInvalid)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.path").isEqualTo("/recommendation")
			.jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
	}
}
