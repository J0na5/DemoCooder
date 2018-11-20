package com.processbyte.landing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class MongoConfiguration {

    @Autowired
    public MongoConfiguration(MongoDbFactory mongoDbFactory, MappingMongoConverter mappingMongoConverter) {
        this.mongoDbFactory = mongoDbFactory;
        this.mappingMongoConverter = mappingMongoConverter;
    }

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory, mappingMongoConverter);
    }

    private MongoDbFactory mongoDbFactory;
    private MappingMongoConverter mappingMongoConverter;
}
