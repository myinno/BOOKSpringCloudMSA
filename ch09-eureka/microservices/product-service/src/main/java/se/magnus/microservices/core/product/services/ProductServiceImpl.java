package se.magnus.microservices.core.product.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import se.magnus.api.core.product.Product;
import se.magnus.api.core.product.ProductService;
import se.magnus.microservices.core.product.persistence.ProductEntity;
import se.magnus.microservices.core.product.persistence.ProductRepository;
import se.magnus.util.exceptions.InvalidInputException;
import se.magnus.util.exceptions.NotFoundException;
import se.magnus.util.http.ServiceUtil;

@RestController
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ServiceUtil serviceUtil;
    //CH07 START
    private final ProductRepository repository;
    private final ProductMapper mapper;
//    @Autowired
//    public ProductServiceImpl(ServiceUtil serviceUtil) {
//        this.serviceUtil = serviceUtil;
//    }

    @Autowired
    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }
    //CH07 END

    @Override
//    public Product getProduct(int productId) {
//        LOG.debug("/product return the found product for productId={}", productId);
//
//        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);
////        if (productId == 13) throw new NotFoundException("No product found for productId: " + productId);
////        return new Product(productId, "name-" + productId, 123, serviceUtil.getServiceAddress());
//        
//        ProductEntity entity = repository.findByProductId(productId)
//            .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));
//
//        Product response = mapper.entityToApi(entity);
//        response.setServiceAddress(serviceUtil.getServiceAddress());
//
//        LOG.debug("getProduct: found productId: {}", response.getProductId());
//
//        return response;
//    }
    public Mono<Product> getProduct(int productId) {
    	LOG.debug("getProduct start:", productId);
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);
        
        Mono<ProductEntity> obj = repository.findByProductId(productId);
        obj.log();
    	LOG.debug("getProduct start:2222222222222:" + obj.toString());
        
        
        return repository.findByProductId(productId)
            .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
            .log()
            .map(e -> mapper.entityToApi(e))
            .map(e -> {e.setServiceAddress(serviceUtil.getServiceAddress()); return e;});
    }    
    

    //CH07 START
    @Override
    public Product createProduct(Product body) {
    	LOG.debug("createProduct start:", body);
        if (body.getProductId() < 1) throw new InvalidInputException("Invalid productId: " + body.getProductId());

        ProductEntity entity = mapper.apiToEntity(body);
        Mono<Product> newEntity = repository.save(entity)
            .log()
            .onErrorMap(
                DuplicateKeyException.class,
                ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId()))
            .map(e -> mapper.entityToApi(e));

        return newEntity.block();
    }

    @Override
    public void deleteProduct(int productId) {
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        LOG.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
        repository.findByProductId(productId).log().map(e -> repository.delete(e)).flatMap(e -> e).block();
    }    
    //CH07 END

}