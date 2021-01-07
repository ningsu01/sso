package com.sso.store;

import com.sso.conf.Conf;
import com.sso.entity.AdminUser;
import com.sso.util.JedisUtil;

/**
 * Created by nings on 2020/12/5.
 */
public class SsoLoginStore {

    private static int redisExpireMinite = 1440;    // 1440 minite, 24 hour
    public static void setRedisExpireMinite(int redisExpireMinite) {
        if (redisExpireMinite < 30) {
            redisExpireMinite = 30;
        }
        SsoLoginStore.redisExpireMinite = redisExpireMinite;
    }
    public static int getRedisExpireMinite() {
        return redisExpireMinite;
    }

    /**
     * 获取redis中的用户
     * @param storeKey
     * @return
     */
    public static AdminUser get(String storeKey) {
        String redisKey = redisKey(storeKey);
        Object objectValue = JedisUtil.getObjectValue(redisKey);
        if (objectValue != null) {
            AdminUser xxlUser = (AdminUser) objectValue;
            return xxlUser;
        }
        return null;
    }

    public static void put(String storeKey, AdminUser xxlUser) {
        String redisKey = redisKey(storeKey);
        JedisUtil.setObjectValue(redisKey, xxlUser, redisExpireMinite * 60);  // minite to second
    }

    public static String redisKey(String userId){
        return Conf.SSO_SESSIONID.concat("#").concat(userId);
    }

    public static void remove(String storeKey) {
        if (storeKey!=null){
            JedisUtil.del(storeKey);
        }
    }
}
