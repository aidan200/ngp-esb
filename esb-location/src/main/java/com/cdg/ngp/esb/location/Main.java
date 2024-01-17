package com.cdg.ngp.esb.location;

import com.cdg.ngp.esb.location.config.properties.CommonProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @ClassName ${NAME}
 * @Description Entry class
 * @Author siy
 * @Date 2024/1/16 10:26
 * @Version 1.0
 */

@EnableConfigurationProperties({CommonProperties.class})
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}