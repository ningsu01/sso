package com.sso.login;

import com.sso.conf.Conf;
import com.sso.entity.AdminUser;
import com.sso.store.SsoLoginStore;
import com.sso.store.SsoSessionIdHelper;
import com.sso.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.sso.util.CookieUtil.getValue;

/**
 * Created by nings on 2020/12/5.
 * web 系统登陆
 */
public class SsoWebLoginHelper {


    /**
     * 登陆方法
     * @param response
     * @param makeSessionId
     * @param adminUser
     * @param ifRem
     */
    public static void login(HttpServletResponse response, String makeSessionId,
                             AdminUser adminUser, boolean ifRem) {
        String storeKey = SsoSessionIdHelper.parseStoreKey(makeSessionId);
        if (storeKey==null){
            throw new RuntimeException("storekey不符合要求，请检查");
        }
        SsoLoginStore.put(storeKey,adminUser);
        CookieUtil.set(response,Conf.SSO_SESSIONID,makeSessionId,ifRem);
    }



    public static AdminUser ssoLoginCheck(HttpServletRequest request,HttpServletResponse response){
        String cookieSessionId = getValue(request,Conf.SSO_SESSIONID);
        // 查询redis中，是否包含此用户的信息，userId
        String parseStoreKey = SsoSessionIdHelper.parseStoreKey(cookieSessionId);
        if (parseStoreKey==null){
            return null;
        }
        // redis中获取信息
        AdminUser adminUser = loginTokenCheck(cookieSessionId);
        if (adminUser!=null){
            return adminUser;
        }
        // 删除旧的cookie
        CookieUtil.remove(request,response,Conf.SSO_SESSIONID);
        // 获取传递的sessionid参数
        String paramSessionId = request.getParameter(Conf.SSO_SESSIONID);
        AdminUser adminUser2 = loginTokenCheck(paramSessionId);
        if (adminUser2!=null){
            CookieUtil.set(response,Conf.SSO_SESSIONID,paramSessionId,false);
        }
        return null;
    }


    public static AdminUser loginTokenCheck(String sessionId){

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return null;
        }

        AdminUser adminUser = SsoLoginStore.get(storeKey);
        if (adminUser != null) {
            String version = SsoSessionIdHelper.parseVersion(sessionId);
            if (adminUser.getVersion().equals(version)) {

                // After the expiration time has passed half, Auto refresh
                if ((System.currentTimeMillis() - adminUser.getExpireFreshTime()) > adminUser.getExpireMinite()/2) {
                    adminUser.setExpireFreshTime(System.currentTimeMillis());
                    SsoLoginStore.put(storeKey, adminUser);
                }

                return adminUser;
            }
        }
        return null;
    }


    /**
     * 用户退出，删除cookie
     * @param request
     * @param response
     */
    public static void removeSessionIdFromCookie(HttpServletRequest request,
                                                 HttpServletResponse response){
        CookieUtil.remove(request,response, Conf.SSO_SESSIONID);
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {
        String cookieSessionId = getValue(request, Conf.SSO_SESSIONID);
        if (cookieSessionId==null){
            return;
        }
        String storeKey = SsoSessionIdHelper.parseStoreKey(cookieSessionId);
        SsoLoginStore.remove(storeKey);

        CookieUtil.remove(request,response,Conf.SSO_SESSIONID);
    }

    public static String getSessionIdByCookie(HttpServletRequest request) {
        String cookieSessionId = CookieUtil.getValue(request,Conf.SSO_SESSIONID);
        return cookieSessionId;
    }
}
