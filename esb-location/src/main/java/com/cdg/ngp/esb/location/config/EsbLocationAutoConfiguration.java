package com.cdg.ngp.esb.location.config;

import com.cdg.ngp.esb.location.utils.HttpClientUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName EsbLocationAutoConfiguration
 * @Description EsbLocation Configuration class
 * @Author siy
 * @Date 2024/1/16 19:54
 * @Version 1.0
 */

@Configuration
public class EsbLocationAutoConfiguration {


    @Bean
    public HttpClientUtil httpClientUtil(){
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        httpClientUtil.init();
        return httpClientUtil;
    }

}
