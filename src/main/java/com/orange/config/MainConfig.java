package com.orange.config;

import com.orange.OrangeConnectorApp;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Created by Karim on 10/11/2017.
 */
@SpringBootApplication(exclude = {FlywayAutoConfiguration.class, DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
        RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
@Import({VerticalsConfig.class, DozerConfig.class, RedisConfig.class,EmailConfig.class, I18nConfig.class})
@ComponentScan(basePackageClasses = OrangeConnectorApp.class)
public class MainConfig {
}
