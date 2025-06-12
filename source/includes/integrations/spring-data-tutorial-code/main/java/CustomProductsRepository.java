package com.mongodb.examples.springdatabulkinsert;

// start-customproductsrepo
public interface CustomProductsRepository {
    void updateProductQuantity(String name, int newQty)  ;
    int bulkInsertProducts(int count);
}
// end-customproductsrepo
