package com.orange.config;


import com.orange.verticals.OrangeVertical;
import io.vertx.ext.web.Router;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class VerticalsConfig {

    @Bean(name = "OrangeVertical")
    @Scope(value = "prototype")
    @Lazy(value = true)
    public OrangeVertical orangeVertical(Router router) {
        return new OrangeVertical(router);
    }

}
