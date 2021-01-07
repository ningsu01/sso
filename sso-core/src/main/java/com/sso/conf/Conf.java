package com.sso.conf;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nings on 2020/12/5.
 * 定义的一些url路径参数
 */
public class Conf {
    /**
     * sessionid，浏览器用户信息的key（cookie）
     **/
    public static final String SSO_SESSIONID = "user_sessionid";


    /**
     * sso user, request attribute (web client)
     * 子系统传递的用户信息
     */
    public static final String SSO_USER = "sso_user";

    /*********************************下面的是子系统传递的参数**************************************/
    /**
     * 子系统跳转的路径
     **/
    public static final String REDIRECT_URL = "redirect_url";
    /**
     * sso server address (web + token client)
     */
    public static final String SSO_SERVER = "sso_server";

    /**
     * logout path, client relatice path
     * 子系统传递的路径
     */
    public static final String SSO_LOGOUT_PATH = "SSO_LOGOUT_PATH";

    /**
     * excluded paths, client relatice path, include path can be set by "filter-mapping"
     * 子系统传递的路径
     */
    public static final String SSO_EXCLUDED_PATHS = "SSO_EXCLUDED_PATHS";

    /*****************************上面的是子系统传递的参数，下面对应的是sso-server***********************************/

    /**
     * login url, server relative path (web client)
     * sso-server的页面路径
     */
    public static final String SSO_LOGIN = "/login";
    /**
     * logout url, server relative path (web client)
     * sso-server的页面路径
     */
    public static final String SSO_LOGOUT = "/logout";


    /**
     * 使用匿名内部类，初始化
     */
    public static final Map<String,Object> LOGIN_FAIL = new HashMap<String,Object>(){
        {
            put("-999","请登陆");
        }
    };

    /**
     * 枚举类型的使用
     */
    public static enum  ConfEnum{
        // 要加多个的话，自己添加
        // 登陆失败
        LOGIN_FAIL(-999,"请登陆");

        private int code;
        private String msg;

        ConfEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
