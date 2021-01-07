package com.sso.config;

import com.sso.store.SsoLoginStore;
import com.sso.util.JedisUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by nings on 2020/12/6.
 */
@Configuration
//@ImportResource(value = {"classpath:resources/cache.xml"}) // 导入xml文件
//@Import(Object.class) // 导入class文件
@PropertySource(value = {"classpath:cache.properties"}) // resources是classapth的别名，
public class SsoConfig implements InitializingBean,DisposableBean{

    @Value("${sso.redis.address}")
    private String redisAddr;

    @Value("${sso.redis.expire.minite}")
    private int redisExpireMinite;

    @Override
    public void destroy() throws Exception {
        JedisUtil.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 设置值
        SsoLoginStore.setRedisExpireMinite(redisExpireMinite);
        // 初始化
        JedisUtil.init(redisAddr);
    }
}
