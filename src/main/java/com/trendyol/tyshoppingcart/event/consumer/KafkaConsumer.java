package com.trendyol.tyshoppingcart.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.tyshoppingcart.domain.UserNotificationType;
import com.trendyol.tyshoppingcart.dto.UpdateProductPriceAndStockDto;
import com.trendyol.tyshoppingcart.dto.UserDto;
import com.trendyol.tyshoppingcart.dto.UserMessageNotificationDto;
import com.trendyol.tyshoppingcart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
public class KafkaConsumer {
    private CartService cartService;

    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String USER_NOTIFICATION_MESSAGE = "UserNotificationMessage";

    @Autowired
    public KafkaConsumer(CartService cartService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.cartService = cartService;
        this.kafkaTemplate = kafkaTemplate;
    }

  /* @KafkaListener(topics = "AddedProductBySupplier", groupId = "trendyol",
            containerFactory = "productKafkaListenerContainerFactory")
    public void consume(@Payload UpdateProductPriceAndStockDto message) throws JsonProcessingException {
        if(Objects.nonNull(message.getNewPrice()) && message.getNewPrice() < message.getOldPrice()) {

            List<Integer> userIdList = cartService.getProductUserIdList(message.getProductId());

            List<UserDto> userDtoList = getUser(userIdList);

            UserMessageNotificationDto userMessageNotificationDto = new UserMessageNotificationDto();
            userMessageNotificationDto.setUserList(userDtoList);
            userMessageNotificationDto.setUserNotificationType(UserNotificationType.PRICECHANGED);

            kafkaTemplate.send(USER_NOTIFICATION_MESSAGE,userMessageNotificationDto);
        }


    }

    private List<UserDto> getUser(List<Integer> userIdList) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject("http://localhost:8082/users/getUserList",userIdList,String.class);

        ObjectMapper jsonMapper = new ObjectMapper();

        List<UserDto> userDtoList = jsonMapper.readValue(result, new TypeReference<List<UserDto>>(){});

        return userDtoList;
    }*/
}
