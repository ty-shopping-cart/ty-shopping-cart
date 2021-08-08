package com.trendyol.tyshoppingcart.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.tyshoppingcart.dto.*;
import com.trendyol.tyshoppingcart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Mock
    CartService cartService;

    @BeforeEach
    void beforeEach(){
        if (mvc != null) {
            return;
        }

        this.mvc = MockMvcBuilders.standaloneSetup(new CartController(cartService))
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addItem_returnSuccess_whenEverythingIsOkay() throws Exception {
        AddToCartDto request = new AddToCartDto();
        request.setProductId(1);
        request.setQuantity(5);
        request.setUserId(10);

        String expected = "Added to basket";

        Mockito.when(cartService.addItem(request)).thenReturn(expected);

        MvcResult mvcResult = mvc.perform(post("/cart/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo(expected);

    }

    @Test
    void getCart_returnSuccess_whenEverythingIsOkay() throws Exception {
        Integer userId = 10;

        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setCategoryId(1);
        cartProductDto.setProductId(3);
        cartProductDto.setTitle("product1");
        cartProductDto.setProductQuantity(10);

        List<CartProductDto> cartProductDtoList = new ArrayList<>();
        cartProductDtoList.add(cartProductDto);

        CartDto cartDto = new CartDto();
        cartDto.setUserId(10);
        cartDto.setTotalAmount(100.10);
        cartDto.setCartProductList(cartProductDtoList);

        Mockito.when(cartService.getCart(userId)).thenReturn(cartDto);

        MvcResult mvcResult = mvc.perform(get("/cart/?userId=" + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartDto response = objectMapper
                .readValue(mvcResult.getResponse().getContentAsByteArray(), CartDto.class);

        assertThat(response.getUserId()).isEqualTo(10);
        assertThat(response.getTotalAmount()).isEqualTo(100.10);
        assertThat(response.getCartProductList().get(0).getCategoryId()).isEqualTo(1);
        assertThat(response.getCartProductList().get(0).getProductId()).isEqualTo(3);
    }

    @Test
    void getCartDiscount_returnSuccess_whenEverythingIsOkay() throws Exception {
        Integer userId = 10;

        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setCategoryId(1);
        cartProductDto.setProductId(3);
        cartProductDto.setTitle("product1");
        cartProductDto.setProductQuantity(10);

        List<CartProductDto> cartProductDtoList = new ArrayList<>();
        cartProductDtoList.add(cartProductDto);

        DiscountDto discountDto = new DiscountDto();
        discountDto.setDiscount(50.0);
        discountDto.setDiscountId(1L);
        discountDto.setDiscountName("Campaing 1");

        List<DiscountDto> campaignList = new ArrayList<>();
        campaignList.add(discountDto);

        DiscountCartDto discountCartDto = new DiscountCartDto();
        discountCartDto.setUserId(10);
        discountCartDto.setTotalAmount(100.10);
        discountCartDto.setCartProductList(cartProductDtoList);
        discountCartDto.setShippingFee(11.99);
        discountCartDto.setCampaignList(campaignList);

        Mockito.when(cartService.getCartDiscount(userId,null)).thenReturn(discountCartDto);

        MvcResult mvcResult = mvc.perform(get("/cart/cartDiscount?userId=" + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        DiscountCartDto response = objectMapper
                .readValue(mvcResult.getResponse().getContentAsByteArray(), DiscountCartDto.class);

        assertThat(response.getUserId()).isEqualTo(10);
        assertThat(response.getTotalAmount()).isEqualTo(100.10);
        assertThat(response.getCartProductList().get(0).getCategoryId()).isEqualTo(1);
        assertThat(response.getCartProductList().get(0).getProductId()).isEqualTo(3);
        assertThat(response.getShippingFee()).isEqualTo(11.99);
        assertThat(response.getCampaignList().get(0).getDiscount()).isEqualTo(50.0);

    }

    @Test
    void getProductUserIdList_returnSuccess_whenEverythingIsOkay() throws Exception {
        Integer productId = 15;

        List<Integer> userList = new ArrayList<>();
        userList.add(10);

        Mockito.when(cartService.getProductUserIdList(productId)).thenReturn(userList);

        MvcResult mvcResult = mvc.perform(get("/cart/fetchUsersWithProductsInCart?productId=" + productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Integer> response = objectMapper
                .readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<List<Integer>>() {});

        assertThat(response.get(0)).isEqualTo(10);

    }


    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
