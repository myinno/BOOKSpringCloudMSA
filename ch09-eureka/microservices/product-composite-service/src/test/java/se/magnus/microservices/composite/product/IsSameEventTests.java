package se.magnus.microservices.composite.product;

import static org.hamcrest.MatcherAssert.assertThat;
import static se.magnus.api.event.Event.Type.CREATE;
import static se.magnus.api.event.Event.Type.DELETE;
import static se.magnus.microservices.composite.product.IsSameEvent.sameEventExceptCreatedAt;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.magnus.api.core.product.Product;
import se.magnus.api.event.Event;

public class IsSameEventTests {

	ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEventObjectCompare() throws JsonProcessingException {

    	// Event #1 and #2 are the same event, but occurs as different times
		// Event #3 and #4 are different events
		Event<Integer, Product> event1 = new Event<>(CREATE, 1, new Product(1, "name", 1, null));
		Event<Integer, Product> event2 = new Event<>(CREATE, 1, new Product(1, "name", 1, null));
		Event<Integer, Product> event3 = new Event<>(DELETE, 1, null);
		Event<Integer, Product> event4 = new Event<>(CREATE, 1, new Product(2, "name", 1, null));

		String event1JSon = mapper.writeValueAsString(event1);

		assertThat(event1JSon, Matchers.is(sameEventExceptCreatedAt(event2)));
		assertThat(event1JSon, Matchers.not(sameEventExceptCreatedAt(event3)));
		assertThat(event1JSon, Matchers.not(sameEventExceptCreatedAt(event4)));
    }
}
