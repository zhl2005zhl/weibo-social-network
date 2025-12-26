package com.weibo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 微博系统后端应用主入口类
 */
@SpringBootApplication
@EntityScan(basePackages = "com.weibo.entity")
@EnableJpaRepositories(basePackages = "com.weibo.repository")
public class WeiboApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiboApplication.class, args);
    }

}