package com.trendyol.tyshoppingcart.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@Document
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field
    private Integer userId;

    @Field
    private Boolean isActive;

    @Field
    private Double total;

    @Field
    private Date createdDate;


}
