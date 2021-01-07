package com.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by nings on 2020/12/4.
 */

/**
 * 启动类
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude ={DataSourceAutoConfiguration.class})
@MapperScan(value = {"com.sso.mapper"})
public class SsoWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoWebApplication.class,args);
    }

}
