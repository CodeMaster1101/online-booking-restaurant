package com.mile.pc.mile.restoraunt.app.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mile.pc.mile.restoraunt.app.constants.CONSTANTS;

@Configuration
public class TimeModuleConfig {

	@Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializers(CONSTANTS.LOCAL_DATETIME_SERIALIZER);
    }
}