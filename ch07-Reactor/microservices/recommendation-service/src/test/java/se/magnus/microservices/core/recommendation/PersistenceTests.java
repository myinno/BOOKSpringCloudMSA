package se.magnus.microservices.core.recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import se.magnus.microservices.core.recommendation.persistence.RecommendationEntity;
import se.magnus.microservices.core.recommendation.persistence.RecommendationRepository;

//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class PersistenceTests {

    @Autowired
    private RecommendationRepository repository;

    private RecommendationEntity savedEntity;

    @BeforeEach
   	public void setupDb() {
   		repository.deleteAll().block();

        RecommendationEntity entity = new RecommendationEntity(1, 2, "a", 3, "c");
        savedEntity = repository.save(entity).block();

        assertEqualsRecommendation(entity, savedEntity);
    }


    @Test
   	public void create() {

        RecommendationEntity newEntity = new RecommendationEntity(1, 3, "a", 3, "c");
        repository.save(newEntity).block();

        RecommendationEntity foundEntity = repository.findById(newEntity.getId()).block();
        assertEqualsRecommendation(newEntity, foundEntity);

        assertEquals(2, (long)repository.count().block());
    }

    @Test
   	public void update() {
        savedEntity.setAuthor("a2");
        repository.save(savedEntity).block();

        RecommendationEntity foundEntity = repository.findById(savedEntity.getId()).block();
        assertEquals(1, (long)foundEntity.getVersion());
        assertEquals("a2", foundEntity.getAuthor());
    }

    @Test
   	public void delete() {
        repository.delete(savedEntity).block();
        assertFalse(repository.existsById(savedEntity.getId()).block());
    }

    @Test
   	public void getByProductId() {
        List<RecommendationEntity> entityList = repository.findByProductId(savedEntity.getProductId()).collectList().block();

        Matchers.hasSize(1);
        //ch07????????? ???????????? ??????
//        assertThat(entityList, Matchers.hasSize(1));
        assertEqualsRecommendation(savedEntity, entityList.get(0));
    }
// ch07  DuplicateKeyException??? junit5 ???????????? ?????? ??????
//    @Test(expected = DuplicateKeyException.class)
//   	public void duplicateError() {
//        RecommendationEntity entity = new RecommendationEntity(1, 2, "a", 3, "c");
//        repository.save(entity).block();
//    }

    @Test
   	public void optimisticLockError() {

        // Store the saved entity in two separate entity objects
        RecommendationEntity entity1 = repository.findById(savedEntity.getId()).block();
        RecommendationEntity entity2 = repository.findById(savedEntity.getId()).block();

        // Update the entity using the first entity object
        entity1.setAuthor("a1");
        repository.save(entity1).block();

        //  Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e. a Optimistic Lock Error
        try {
            entity2.setAuthor("a2");
            repository.save(entity2).block();

            fail("Expected an OptimisticLockingFailureException");
        } catch (OptimisticLockingFailureException e) {}

        // Get the updated entity from the database and verify its new sate
        RecommendationEntity updatedEntity = repository.findById(savedEntity.getId()).block();
        assertEquals(1, (int)updatedEntity.getVersion());
        assertEquals("a1", updatedEntity.getAuthor());
    }

    private void assertEqualsRecommendation(RecommendationEntity expectedEntity, RecommendationEntity actualEntity) {
        assertEquals(expectedEntity.getId(),               actualEntity.getId());
        assertEquals(expectedEntity.getVersion(),          actualEntity.getVersion());
        assertEquals(expectedEntity.getProductId(),        actualEntity.getProductId());
        assertEquals(expectedEntity.getRecommendationId(), actualEntity.getRecommendationId());
        assertEquals(expectedEntity.getAuthor(),           actualEntity.getAuthor());
        assertEquals(expectedEntity.getRating(),           actualEntity.getRating());
        assertEquals(expectedEntity.getContent(),          actualEntity.getContent());
    }
}
