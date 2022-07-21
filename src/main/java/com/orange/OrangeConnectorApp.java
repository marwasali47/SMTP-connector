package com.orange;

import com.orange.config.MainConfig;
import org.springframework.boot.SpringApplication;

public class OrangeConnectorApp {
    public static void main(String[] args) {
        System.setProperty("isThreadContextMapInheritable", "true");
        SpringApplication.run(MainConfig.class, args);
    }
}
