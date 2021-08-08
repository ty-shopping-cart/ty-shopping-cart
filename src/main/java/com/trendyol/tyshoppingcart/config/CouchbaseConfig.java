package com.trendyol.tyshoppingcart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {
    @Override
    public String getConnectionString() {
        return "localhost";
    }

    @Override
    public String getUserName() {
        return "couchuser";
    }

    @Override
    public String getPassword() {
        return "pass123";
    }

    @Override
    public String getBucketName() {
        return "cart";
    }
}
