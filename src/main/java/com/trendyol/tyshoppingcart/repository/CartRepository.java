package com.trendyol.tyshoppingcart.repository;

import com.trendyol.tyshoppingcart.model.Cart;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends CouchbaseRepository<Cart,Long> {
    Cart findByUserIdAndIsActiveTrue(Integer userId);

    Cart findById(String cartId);
}
