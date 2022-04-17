package se.magnus.microservices.core.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;
import se.magnus.api.core.product.Product;
import se.magnus.api.event.Event;
import se.magnus.microservices.core.product.persistence.ProductRepository;

//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"eureka.client.enabled=false"})
public class ProductServiceApplicationTests {

    @Autowired
    private WebTestClient client;
    //ch06 start
	@Autowired
	private ProductRepository repository;

	private AbstractMessageChannel input = null;
	
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

//		assertTrue(repository.findByProductId(productId).isPresent());
		assertNull(repository.findByProductId(productId).block());
		assertEquals(0, (long)repository.count().block());

		sendCreateProductEvent(productId);

		assertNotNull(repository.findByProductId(productId).block());
		assertEquals(1, (long)repository.count().block());
		
		getAndVerifyProduct(productId, HttpStatus.OK)
            .jsonPath("$.productId").isEqualTo(productId);
	}

	@Test
	public void deleteProduct() {

		int productId = 1;

//ch07
//		postAndVerifyProduct(productId, HttpStatus.OK);
//		assertTrue(repository.findByProductId(productId).isPresent());
//
//		deleteAndVerifyProduct(productId, HttpStatus.OK);
//		assertFalse(repository.findByProductId(productId).isPresent());
//
//		deleteAndVerifyProduct(productId, HttpStatus.OK);
		sendCreateProductEvent(productId);
		assertNotNull(repository.findByProductId(productId).block());

		sendDeleteProductEvent(productId);
		assertNull(repository.findByProductId(productId).block());

		sendDeleteProductEvent(productId);
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

	private void sendCreateProductEvent(int productId) {
		Product product = new Product(productId, "Name " + productId, productId, "SA");
		Event<Integer, Product> event = new Event(Event.Type.CREATE, productId, product);
		input.send(new GenericMessage<>(event));
	}

	private void sendDeleteProductEvent(int productId) {
		Event<Integer, Product> event = new Event(Event.Type.CREATE, productId, null);
		input.send(new GenericMessage<>(event));
	}
	
}