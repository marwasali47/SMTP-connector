package com.orange.verticals;


import com.orange.commons.service.MappingService;
import com.orange.i18n.MessageUtility;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;

public class OrangeVertical extends AbstractVerticle {

    @Autowired
    private MappingService mappingService;

    @Autowired
    private MessageUtility messageUtility;

    private Router router;

    public OrangeVertical(Router router) {
        this.router = router;
    }

    @Override
    public void start() throws Exception {

    }

}
