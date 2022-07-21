package com.orange.commons.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by mohamed_waleed on 19/10/17.
 */
@Service
public class MappingService {

    @Autowired
    private DozerBeanMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T map(Object source, Class<T> target) {
        return mapper.map(source, target);
    }

    public <T> void map(Object source, T target) {
        mapper.map(source, target);
    }

    public <T, O> List<O> map(List<T> objects, Class<O> target) {
        return (List<O>) internalMap(objects, target, (List<O>) Lists.newArrayList());
    }


    public <T, O> List<O> map(List<T> objects, Class<O> target, List<O> destination) {
        return (List<O>) internalMap(objects, target, destination);
    }

    public <T, O> Set<O> map(Set<T> objects, Class<O> target, Set<O> destination) {
        return (Set<O>) internalMap(objects, target, destination);
    }

    public <T, O> Set<O> map(Set<T> objects, Class<O> target) {
        return map(objects, target, (Set<O>) Sets.newHashSet());
    }

    private <T, O> Collection<O> internalMap(Collection<T> objects, Class<O> target, Collection<O> destination) {
        for (T t : objects) {
            destination.add(mapper.map(t, target));
        }
        return destination;
    }

    public String convertObjectToJSONString(Object response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(response);
    }
}