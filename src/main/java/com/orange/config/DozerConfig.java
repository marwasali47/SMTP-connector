package com.orange.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed_waleed on 19/10/17.
 */
@Configuration
public class DozerConfig {

    @Bean
    public DozerBeanMapper dozerBeanMapper(){
        DozerBeanMapper dozerBeanMapper= new DozerBeanMapper();
        List<String> mappingFiles = new ArrayList<>();
        mappingFiles.add("dozerBeanMapping.xml");
        dozerBeanMapper.setMappingFiles(mappingFiles);
        return dozerBeanMapper;
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}