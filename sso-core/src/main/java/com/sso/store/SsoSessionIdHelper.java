package com.sso.store;

import com.sso.entity.AdminUser;

/**
 * Created by nings on 2020/12/5.
 * 根据sessionid获取用户信息
 */
public class SsoSessionIdHelper {


    /**
     * make client sessionId
     *
     * @param nings
     * @return
     */
    public static String makeSessionId(AdminUser adminUser){
        String sessionId = adminUser.getUserid().concat("_").concat(adminUser.getVersion());
        return sessionId;
    }

    /**
     * 由于cookieSessionId中包含两部分，需要解析,一部分是用户id
     * @param sessionId
     * @return
     */
    public static String parseStoreKey(String sessionId) {
        if (sessionId!=null && sessionId.indexOf("_")>-1) {
            String[] sessionIdArr = sessionId.split("_");
            if (sessionIdArr.length==2
                    && sessionIdArr[0]!=null
                    && sessionIdArr[0].trim().length()>0) {
                String userId = sessionIdArr[0].trim();
                return userId;
            }
        }
        return null;
    }

    /**
     * 获取登陆的版本号
     * @param sessionId
     * @return
     */
    public static String parseVersion(String sessionId) {
        if (sessionId!=null && sessionId.indexOf("_")>-1) {
            String[] sessionIdArr = sessionId.split("_");
            if (sessionIdArr.length==2
                    && sessionIdArr[1]!=null
                    && sessionIdArr[1].trim().length()>0) {
                String version = sessionIdArr[1].trim();
                return version;
            }
        }
        return null;
    }

}
