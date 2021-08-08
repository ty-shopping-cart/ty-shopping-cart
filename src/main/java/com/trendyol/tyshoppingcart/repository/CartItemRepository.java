package com.trendyol.tyshoppingcart.repository;

import com.trendyol.tyshoppingcart.model.Cart;
import com.trendyol.tyshoppingcart.model.CartItem;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends CouchbaseRepository<CartItem,Long> {
    //CartItem findByCartIdAndProduct_Id(Long cartId, Long product);

    /*CartItem findByUserIdAndProduct_Id(String userId, Long product);

    List<CartItem> findByUserId(Long cartId);

    List<CartItem> findByProduct_Id(Long product);*/

    CartItem findByCartIdAndProductId(String cartId,Integer productId);

    List<CartItem> findByProductId(Integer productId);

    List<CartItem> findByCartId(String cartId);
}
