package se.magnus.api.core.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
	// 6장에서 각 필드의 final 필드 제거됨
	// Data는 final 필드에서 setter를 만들지 않는다
	private int productId;
    private String name;
    private int weight;
    private String serviceAddress;   //msa 서버 주소.

    public Product() {
        productId = 0;
        name = null;
        weight = 0;
        serviceAddress = null;
    }

//    public Product(int productId, String name, int weight, String serviceAddress) {
//        this.productId = productId;
//        this.name = name;
//        this.weight = weight;
//        this.serviceAddress = serviceAddress;
//    }
//
//    public int getProductId() {
//        return productId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getWeight() {
//        return weight;
//    }
//
//    public String getServiceAddress() {
//        return serviceAddress;
//    }
}
