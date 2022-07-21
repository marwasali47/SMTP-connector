package com.orange.verticals.config;

import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed_waleed on 17/10/17.
 */
@Component
public class VerticalConfigurer {


    private List<Pair<String, JsonObject>> verticals = new ArrayList<>();


    @PostConstruct
    public void init() {
        verticals.add(getOrangeVerticalConfig());
    }

    private Pair<String, JsonObject> getOrangeVerticalConfig() {
        String verticalName = "OrangeVertical";
        return Pair.of(verticalName, null);
    }

    public List<Pair<String, JsonObject>> getVerticals() {
        return verticals;
    }
}
