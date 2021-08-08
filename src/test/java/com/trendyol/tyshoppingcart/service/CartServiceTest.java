package com.trendyol.tyshoppingcart.service;

import com.trendyol.tyshoppingcart.dto.*;
import com.trendyol.tyshoppingcart.model.Cart;
import com.trendyol.tyshoppingcart.model.CartItem;
import com.trendyol.tyshoppingcart.repository.CartItemRepository;
import com.trendyol.tyshoppingcart.repository.CartRepository;
import com.trendyol.tyshoppingcart.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    CartItemRepository cartItemRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    RestTemplate restTemplate;

    @Mock
    Environment environment;

    @InjectMocks
    CartServiceImpl cartService;

    @Test
    void addItem_returnSuccess_whenCartCreatedAndProductFoundInCart() {

        AddToCartDto request = new AddToCartDto();
        request.setProductId(1);
        request.setQuantity(5);
        request.setUserId(10);

        Cart cart = new Cart();
        cart.setUserId(10);
        cart.setId("123-ASD");
        cart.setTotal(1000.0);
        cart.setIsActive(true);
        when(cartRepository.findByUserIdAndIsActiveTrue(request.getUserId())).thenReturn(cart);

        CartItem cartItem = new CartItem();
        cartItem.setCartId(cart.getId());
        cartItem.setQuantity(25);
        cartItem.setProductId(1);

        when(cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId())).thenReturn(cartItem);

        restTemplateMockProductService();

        cartService.addItem(request);

        Mockito.verify(cartItemRepository, Mockito.times(1)).save(any(CartItem.class));
        Mockito.verify(cartRepository, Mockito.times(1)).save(any(Cart.class));
    }

    @Test
    void addItem_returnSuccess_whenCartNotCreatedAndProductNotFoundInCart() {

        AddToCartDto request = new AddToCartDto();
        request.setProductId(1);
        request.setQuantity(5);
        request.setUserId(10);

        when(cartRepository.findByUserIdAndIsActiveTrue(request.getUserId())).thenReturn(null);

        restTemplateMockProductService();

        cartService.addItem(request);

        Mockito.verify(cartItemRepository, Mockito.times(1)).save(any(CartItem.class));
        Mockito.verify(cartRepository, Mockito.times(2)).save(any(Cart.class));
    }

    @Test
    void getCart_returnSuccess_whenEverythingIsOkay() {
        Integer userId = 10;

        Cart cart = new Cart();
        cart.setId("A123-321");
        cart.setTotal(1000.0);
        cart.setUserId(userId);
        when(cartRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(cart);

        CartItem cartItem = new CartItem();
        cartItem.setProductId(1);
        cartItem.setQuantity(5);
        cartItem.setCartId(cart.getId());
        cartItem.setId("B123-C21");

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        when(cartItemRepository.findByCartId(cart.getId())).thenReturn(cartItemList);

        restTemplateMockProductService();

        CartDto response = cartService.getCart(userId);

        assertThat(response.getTotalAmount()).isEqualTo(1000.0);
        assertThat(response.getCartProductList().get(0).getProductId()).isEqualTo(1);
        assertThat(response.getCartProductList().get(0).getTitle()).isEqualTo("Mavi Gomlek");

    }

    @Test
    void getCartDiscount_returnSuccess_whenEverythingIsOkay() {
        Integer userId = 10;
        Integer couponId = 1;

        Cart cart = new Cart();
        cart.setId("A123-321");
        cart.setTotal(1000.0);
        cart.setUserId(userId);
        when(cartRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(cart);

        CartItem cartItem = new CartItem();
        cartItem.setProductId(1);
        cartItem.setQuantity(5);
        cartItem.setCartId(cart.getId());
        cartItem.setId("B123-C21");

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        when(cartItemRepository.findByCartId(cart.getId())).thenReturn(cartItemList);

        restTemplateMockProductService();

        restTemplateMockShippingFee();

        restTemplateMockGetCampaign();

        restTemplateMockGetCoupon();

        restTemplateMockGetDeliveryCampaign();

        DiscountCartDto response = cartService.getCartDiscount(userId,couponId);

        assertThat(response.getSubtotal()).isEqualTo(1000.0);
        assertThat(response.getCartProductList().get(0).getProductId()).isEqualTo(1);
        assertThat(response.getCartProductList().get(0).getTitle()).isEqualTo("Mavi Gomlek");
        assertThat(response.getShippingFee()).isEqualTo(11.99);
        assertThat(response.getCampaignList().get(0).getDiscount()).isEqualTo(135.0);
        assertThat(response.getCoupon().getDiscount()).isEqualTo(300);
        assertThat(response.getDelivery().getDiscount()).isEqualTo(11.99);

    }

    private void restTemplateMockProductService() {
        String url = "http://localhost:8081/products/?productId=";

        when(environment.getProperty("product.getProduct")).thenReturn(url);

        String product = "{\n" +
                "  \"productId\": 1,\n" +
                "  \"title\": \"Mavi Gomlek\",\n" +
                "  \"price\": 150,\n" +
                "  \"imageURL\": \"product1.jpg\",\n" +
                "  \"barcode\": \"A123Z\",\n" +
                "  \"stock\": 100,\n" +
                "  \"categoryId\": 1\n" +
                "}";

        when(restTemplate.getForObject(url + 1 ,String.class)).thenReturn(product);
    }

    private void restTemplateMockShippingFee() {
        String url = "http://localhost:8082/delivery/";

        when(environment.getProperty("delivery.getShippingFee")).thenReturn(url);

        String shippingFee = "11.99";

        when(restTemplate.getForObject(url ,String.class)).thenReturn(shippingFee);
    }

    private void restTemplateMockGetCampaign() {
        String url = "http://localhost:8082/campaign/getCampaignDiscount";

        when(environment.getProperty("campaign.getCampaign")).thenReturn(url);

        String campaignList = "[\n" +
                "  {\n" +
                "    \"discountId\": 1,\n" +
                "    \"discountName\": \"RateCampaign 1\",\n" +
                "    \"discount\": 135\n" +
                "  }\n" +
                "]";

        when(restTemplate.postForObject(eq(url),any(CampaignCartDto.class) ,eq(String.class))).thenReturn(campaignList);
    }

    private void restTemplateMockGetCoupon() {
        String url = "http://localhost:8082/coupon/getCouponDiscount";

        when(environment.getProperty("coupon.getCoupon")).thenReturn(url);

        String coupon = "{\n" +
                "  \"discountId\": 1,\n" +
                "  \"discountName\": \"RateCoupon1\",\n" +
                "  \"discount\": 300\n" +
                "}";

        when(restTemplate.postForObject(eq(url),any(CouponCartDto.class) ,eq(String.class))).thenReturn(coupon);
    }

    private void restTemplateMockGetDeliveryCampaign() {
        String url = "http://localhost:8082/campaign/getDeliveryCampaign?totalAmount=";

        when(environment.getProperty("campaign.getDeliveryCampaign")).thenReturn(url);

        String deliveryCampaign = "{\n" +
                "  \"discountId\": 0,\n" +
                "  \"discountName\": \"Kargo bedava kampanyası\",\n" +
                "  \"discount\": 11.99\n" +
                "}";

        when(restTemplate.getForObject(url + 1000.0,String.class)).thenReturn(deliveryCampaign);
    }
}
