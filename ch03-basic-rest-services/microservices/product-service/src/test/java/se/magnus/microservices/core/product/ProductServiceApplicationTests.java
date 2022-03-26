package se.magnus.microservices.core.product;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)

public class ProductServiceApplicationTests {

    @Autowired
    private WebTestClient client;

	@SuppressWarnings("deprecation")
	@Test
	public void getProductById() {

		int productId = 1;

        client.get()
            .uri("/product/" + productId)
            .accept(APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.productId").isEqualTo(productId);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getProductInvalidParameterString() {

        client.get()
            .uri("/product/no-integer")
            .accept(APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isEqualTo(BAD_REQUEST)
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.path").isEqualTo("/product/no-integer")
            .jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getProductNotFound() {

		int productIdNotFound = 13;

        client.get()
            .uri("/product/" + productIdNotFound)
            .accept(APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isNotFound()
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.path").isEqualTo("/product/" + productIdNotFound)
            .jsonPath("$.message").isEqualTo("No product found for productId: " + productIdNotFound);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getProductInvalidParameterNegativeValue() {

        int productIdInvalid = -1;

        client.get()
            .uri("/product/" + productIdInvalid)
            .accept(APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.path").isEqualTo("/product/" + productIdInvalid)
            .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
	}
}