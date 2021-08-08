package com.trendyol.tyshoppingcart.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.tyshoppingcart.dto.*;
import com.trendyol.tyshoppingcart.model.Cart;
import com.trendyol.tyshoppingcart.model.CartItem;
import com.trendyol.tyshoppingcart.repository.CartItemRepository;
import com.trendyol.tyshoppingcart.repository.CartRepository;
import com.trendyol.tyshoppingcart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {

    private CartItemRepository cartItemRepository;
    private CartRepository cartRepository;
    private RestTemplate restTemplate;
    private Environment environment;

    @Autowired
    public CartServiceImpl(CartItemRepository cartItemRepository,
                           CartRepository cartRepository,
                           RestTemplate restTemplate,
                           Environment environment) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    @Override
    public String addItem(AddToCartDto addToCartDto) {

        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(addToCartDto.getUserId());
        if (Objects.isNull(cart)) {
            cart = new Cart();
            cart.setUserId(addToCartDto.getUserId());
            cart.setIsActive(true);
            cart.setTotal(0.0);
            cartRepository.save(cart);
        }

        CartItem cartItemRepo = cartItemRepository.findByCartIdAndProductId(cart.getId(), addToCartDto.getProductId());

        ProductDto productDto = getProduct(addToCartDto.getProductId());

        if (cartItemRepo != null) {

            cartItemRepo.setQuantity(cartItemRepo.getQuantity() + addToCartDto.getQuantity());
            cartItemRepository.save(cartItemRepo);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(addToCartDto.getQuantity());
            cartItem.setProductId(addToCartDto.getProductId());
            cartItem.setCartId(cart.getId());
            cartItemRepository.save(cartItem);
        }

        Double total = cart.getTotal();
        total += productDto.getPrice() * addToCartDto.getQuantity();

        cart.setTotal(total.doubleValue());
        cartRepository.save(cart);

        return "Added to basket";
    }

    @Override
    public CartDto getCart(Integer userId) {

        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(userId);

        List<CartItem> cartItemList = cartItemRepository.findByCartId(cart.getId());

        CartDto cartDto = new CartDto();

        List<CartProductDto> cartProductDtoList = new ArrayList<>();

        for (int i = 0; i < cartItemList.size(); i++) {
            ProductDto productDto = getProduct(cartItemList.get(i).getProductId());
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setProductId(cartItemList.get(i).getProductId());
            cartProductDto.setTitle(productDto.getTitle());
            cartProductDto.setImageURL(productDto.getImageURL());
            cartProductDto.setBarcode(productDto.getBarcode());
            cartProductDto.setCategoryId(productDto.getCategoryId());
            cartProductDto.setProductQuantity(cartItemList.get(i).getQuantity());
            cartProductDto.setPrice(productDto.getPrice());
            cartProductDtoList.add(cartProductDto);
        }

        cartDto.setCartProductList(cartProductDtoList);

        cartDto.setTotalAmount(cart.getTotal());
        cartDto.setUserId(cart.getUserId());

        return cartDto;
    }

    @Override
    public DiscountCartDto getCartDiscount(Integer userId, Integer couponId) {

        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(userId);

        List<CartItem> cartItemList = cartItemRepository.findByCartId(cart.getId());

        List<CartProductDto> cartProductDtoList = new ArrayList<>();

        for (int i = 0; i < cartItemList.size(); i++) {
            ProductDto productDto = getProduct(cartItemList.get(i).getProductId());
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setProductId(cartItemList.get(i).getProductId());
            cartProductDto.setTitle(productDto.getTitle());
            cartProductDto.setImageURL(productDto.getImageURL());
            cartProductDto.setBarcode(productDto.getBarcode());
            cartProductDto.setCategoryId(productDto.getCategoryId());
            cartProductDto.setProductQuantity(cartItemList.get(i).getQuantity());
            cartProductDtoList.add(cartProductDto);
        }

        DiscountCartDto discountCartDto = new DiscountCartDto();

        discountCartDto.setCartProductList(cartProductDtoList);
        discountCartDto.setUserId(cart.getUserId());
        discountCartDto.setSubtotal(cart.getTotal());
        discountCartDto.setShippingFee(getShippingFee());

        CampaignCartDto campaignCartDto = new CampaignCartDto();

        List<CampaignProductDto> campaignProductList = new ArrayList<>();

        for (int i = 0; i < cartItemList.size(); i++) {
            ProductDto productDto = getProduct(cartItemList.get(i).getProductId());
            CampaignProductDto cartProductDto = new CampaignProductDto();
            cartProductDto.setProductId(cartItemList.get(i).getProductId());
            cartProductDto.setCategoryId(productDto.getCategoryId());
            cartProductDto.setProductQuantity(cartItemList.get(i).getQuantity());
            cartProductDto.setPrice(productDto.getPrice());
            campaignProductList.add(cartProductDto);
        }

        campaignCartDto.setCampaignCartList(campaignProductList);

        campaignCartDto.setTotalAmount(cart.getTotal());
        campaignCartDto.setUserId(cart.getUserId());

        List<DiscountDto> campaignDiscountDtoList = getCampaign(campaignCartDto);

        discountCartDto.setCampaignList(campaignDiscountDtoList);

        Double totalCampaignDiscount = campaignDiscountDtoList.stream().mapToDouble(x -> x.getDiscount()).sum();

        Double cartTotal = cart.getTotal() + discountCartDto.getShippingFee() - totalCampaignDiscount;

        if (Objects.nonNull(couponId)) {

            CouponCartDto couponCartDto = new CouponCartDto();
            couponCartDto.setTotalAmount(cartTotal);
            couponCartDto.setCouponId(couponId.longValue());

            DiscountDto couponDiscountDto = getCoupon(couponCartDto);
            discountCartDto.setCoupon(couponDiscountDto);

            cartTotal = cartTotal - couponDiscountDto.getDiscount();
        }
        DiscountDto deliveryDiscount = getDeliveryCampaign(cart.getTotal());

        discountCartDto.setDelivery(deliveryDiscount);

        discountCartDto.setTotalAmount(cartTotal - deliveryDiscount.getDiscount());

        return discountCartDto;
    }

    @Override
    public List<Integer> getProductUserIdList(Integer productId) {
        List<CartItem> cartItemList = cartItemRepository.findByProductId(productId);
        List<Integer> userIdList = new ArrayList<>();
        cartItemList.stream().forEach(x -> {
            Cart cart = cartRepository.findById(x.getCartId());
            userIdList.add(cart.getUserId());
        });

        return userIdList;
    }

    private DiscountDto getDeliveryCampaign(Double totalAmount) {

        String url = environment.getProperty("campaign.getDeliveryCampaign");

        String result = restTemplate.getForObject(url + totalAmount, String.class);

        ObjectMapper jsonMapper = new ObjectMapper();
        DiscountDto discountDto;

        try {
            discountDto = jsonMapper.readValue(result, DiscountDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return discountDto;
    }

    private Double getShippingFee() {

        String url = environment.getProperty("delivery.getShippingFee");

        String result = restTemplate.getForObject(url, String.class);

        ObjectMapper jsonMapper = new ObjectMapper();

        Double shippingFee;

        try {
            shippingFee = jsonMapper.readValue(result, Double.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return shippingFee;
    }

    private List<DiscountDto> getCampaign(CampaignCartDto campaignCartDto) {

        String url = environment.getProperty("campaign.getCampaign");

        String result = restTemplate.postForObject(url, campaignCartDto, String.class);

        ObjectMapper jsonMapper = new ObjectMapper();

        List<DiscountDto> discountDtoList;

        try {
            discountDtoList = jsonMapper.readValue(result, new TypeReference<List<DiscountDto>>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return discountDtoList;
    }

    private DiscountDto getCoupon(CouponCartDto couponCartDto) {

        String url = environment.getProperty("coupon.getCoupon");

        String result = restTemplate.postForObject(url, couponCartDto, String.class);

        ObjectMapper jsonMapper = new ObjectMapper();

        DiscountDto discountDto;

        try {
            discountDto = jsonMapper.readValue(result, DiscountDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return discountDto;
    }

    private ProductDto getProduct(Integer productId) {

        String url = environment.getProperty("product.getProduct");

        String result = restTemplate.getForObject(url + productId, String.class);

        ObjectMapper jsonMapper = new ObjectMapper();

        ProductDto productDto;

        try {
            productDto = jsonMapper.readValue(result, ProductDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return productDto;
    }
}
