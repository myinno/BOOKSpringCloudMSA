package se.magnus.microservices.core.product;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;
import se.magnus.api.core.product.Product;
import se.magnus.microservices.core.product.persistence.ProductRepository;

//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)

public class ProductServiceApplicationTests {

    @Autowired
    private WebTestClient client;
    //ch06 start
	@Autowired
	private ProductRepository repository;
	
	@BeforeEach
	public void setupDb() {
		repository.deleteAll();
	}
    //ch06 end
    
	@Test
	public void getProductById() {

		int productId = 1;

//        client.get()
//            .uri("/product/" + productId)
//            .accept(APPLICATION_JSON_UTF8)
//            .exchange()
//            .expectStatus().isOk()
//            .expectHeader().contentType(APPLICATION_JSON_UTF8)
//            .expectBody()
//            .jsonPath("$.productId").isEqualTo(productId);
		postAndVerifyProduct(productId, HttpStatus.OK);

		assertTrue(repository.findByProductId(productId).isPresent());

		getAndVerifyProduct(productId, HttpStatus.OK)
            .jsonPath("$.productId").isEqualTo(productId);
	}

	@Test
	public void deleteProduct() {

		int productId = 1;

		postAndVerifyProduct(productId, HttpStatus.OK);
		assertTrue(repository.findByProductId(productId).isPresent());

		deleteAndVerifyProduct(productId, HttpStatus.OK);
		assertFalse(repository.findByProductId(productId).isPresent());

		deleteAndVerifyProduct(productId, HttpStatus.OK);
	}
	
	@Test
	public void getProductInvalidParameterString() {

//        client.get()
//            .uri("/product/no-integer")
//            .accept(APPLICATION_JSON_UTF8)
//            .exchange()
//            .expectStatus().isEqualTo(BAD_REQUEST)
//            .expectHeader().contentType(APPLICATION_JSON_UTF8)
//            .expectBody()
//            .jsonPath("$.path").isEqualTo("/product/no-integer")
//            .jsonPath("$.message").isEqualTo("Type mismatch.");
		getAndVerifyProduct("/no-integer", HttpStatus.BAD_REQUEST)
        .jsonPath("$.path").isEqualTo("/product/no-integer")
        .jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getProductNotFound() {

		int productIdNotFound = 13;

//        client.get()
//            .uri("/product/" + productIdNotFound)
//            .accept(APPLICATION_JSON_UTF8)
//            .exchange()
//            .expectStatus().isNotFound()
//            .expectHeader().contentType(APPLICATION_JSON_UTF8)
//            .expectBody()
//            .jsonPath("$.path").isEqualTo("/product/" + productIdNotFound)
//            .jsonPath("$.message").isEqualTo("No product found for productId: " + productIdNotFound);
		getAndVerifyProduct(productIdNotFound, HttpStatus.NOT_FOUND)
        .jsonPath("$.path").isEqualTo("/product/" + productIdNotFound)
        .jsonPath("$.message").isEqualTo("No product found for productId: " + productIdNotFound);

	}

	@Test
	public void getProductInvalidParameterNegativeValue() {

        int productIdInvalid = -1;

//        client.get()
//            .uri("/product/" + productIdInvalid)
//            .accept(APPLICATION_JSON_UTF8)
//            .exchange()
//            .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
//            .expectHeader().contentType(APPLICATION_JSON_UTF8)
//            .expectBody()
//            .jsonPath("$.path").isEqualTo("/product/" + productIdInvalid)
//            .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
		getAndVerifyProduct(productIdInvalid, HttpStatus.UNPROCESSABLE_ENTITY)
        .jsonPath("$.path").isEqualTo("/product/" + productIdInvalid)
        .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);        
	}

	private WebTestClient.BodyContentSpec getAndVerifyProduct(int productId, HttpStatus expectedStatus) {
		return getAndVerifyProduct("/" + productId, expectedStatus);
	}

	@SuppressWarnings("deprecation")
	private WebTestClient.BodyContentSpec getAndVerifyProduct(String productIdPath, HttpStatus expectedStatus) {
		return client.get()
			.uri("/product" + productIdPath)
			.accept(APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectHeader().contentType(APPLICATION_JSON_UTF8)
			.expectBody();
	}

	@SuppressWarnings("deprecation")
	private WebTestClient.BodyContentSpec postAndVerifyProduct(int productId, HttpStatus expectedStatus) {
		Product product = new Product(productId, "Name " + productId, productId, "SA");
		return client.post()
			.uri("/product")
			.body(Mono.just(product), Product.class)
			.accept(APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectHeader().contentType(APPLICATION_JSON_UTF8)
			.expectBody();
	}

	@SuppressWarnings("deprecation")
	private WebTestClient.BodyContentSpec deleteAndVerifyProduct(int productId, HttpStatus expectedStatus) {
		return client.delete()
			.uri("/product/" + productId)
			.accept(APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectBody();
	}
}