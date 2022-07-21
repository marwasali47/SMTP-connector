package com.orange.verticals;

/**
 * Created by Karim on 10/8/2017.
 */

import com.orange.auth.AuthInfo;
import com.orange.auth.Oauth2Handler;
import com.orange.auth.Oauth2Provider;
import com.orange.i18n.I18nHandler;
import com.orange.verticals.config.VerticalConfigurer;
import io.vertx.core.*;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component("MainVerticle")
public class MainVerticle extends  AbstractVerticle {

    @Autowired
    private VerticalConfigurer verticalConfigurer;

    @Autowired
    private MainVerticle verticle;

    @Autowired
    private BeanFactory beanFactory;

    @Value("${oauth.client.id}")
    private String oauthClientId;

    @Value("${oauth.client.secret}")
    private String oauthClientSecret;

    @Value("${server.port}")
    private Integer serverPort;

    @Value("${oauth.server.host}")
    private String oauthHost;

    @Value("${oauth.server.port}")
    private Integer oauthPort;

    @Value("${oauth.server.checkTokenUrl}")
    private String oauthCheckTokenUrl;

    @PostConstruct
    public void init() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(verticle);
    }



    @Override
    public void start(Future<Void> fut) throws Exception {

        Router router = Router.router(vertx);

        router.route().handler(CorsHandler.create(".*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT)
                .allowedMethod(HttpMethod.PATCH)
                .allowCredentials(true)
                .allowedHeader("Access-Control-Allow-Method")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowedHeader("Authorization")
                .allowedHeader("Content-Type"));

        AuthInfo authInfo = new AuthInfo();
        authInfo.setClientId(oauthClientId);
        authInfo.setClientSecret(oauthClientSecret);
        authInfo.setHost(oauthHost);
        authInfo.setPort(oauthPort);
        authInfo.setCheckTokenUrl(oauthCheckTokenUrl);

        router.route().handler(Oauth2Handler.create(authInfo, new Oauth2Provider(vertx)));

        router.route().handler(I18nHandler.create());

        router.route().handler(LoggingHandler.create());

        vertx
            .createHttpServer()
            .requestHandler(router::accept)
            .listen(serverPort, result -> {
                if (result.succeeded()) {

                    List<Pair<String, JsonObject>> verticals =  verticalConfigurer.getVerticals();

                    for(Pair<String, JsonObject> vertical: verticals) {
                        String verticalName = vertical.getKey();
                        JsonObject verticalConfig = vertical.getValue();
                        vertx.deployVerticle(
                                    (Verticle) beanFactory.getBean(verticalName, router),
                                    new DeploymentOptions().setConfig(verticalConfig).setWorkerPoolSize(10)
                        );
                    }

                    fut.complete();
                } else {
                    fut.fail(result.cause());
                }
            });


    }


}