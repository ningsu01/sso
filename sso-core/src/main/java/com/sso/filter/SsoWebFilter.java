package com.sso.filter;

import com.sso.conf.Conf;
import com.sso.entity.AdminUser;
import com.sso.login.SsoWebLoginHelper;
import com.sso.path.impl.AntPathMatcher;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by nings on 2020/12/5.
 *
 * 则执行下列代码后打印出如下结果：
 1、System.out.println(request.getRequestURI());
 打印结果：/test/login.jsp
 2、System.out.println(request.getRequestURL());
 打印结果：http://localhost:8080/test/login.jsp
 3、 System.out.println(request.getContextPath());
 打印结果：/test
 4、System.out.println(request.getServletPath());
 打印结果：/login.jsp
 */
@Slf4j
public class SsoWebFilter implements Filter{

    /**验证地址的一致性**/
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    /**sso的server地址**/
    private String serverPath;
    /**退出登陆的地址**/
    private String logoutPath;
    /**排除不被拦截的地址**/
    private String excludedPaths;

    /**
     * 子系统引入filter过后，进行参数传递，初始化
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        serverPath = filterConfig.getInitParameter(Conf.SSO_SERVER);
        logoutPath = filterConfig.getInitParameter(Conf.SSO_LOGOUT_PATH);
        excludedPaths = filterConfig.getInitParameter(Conf.SSO_EXCLUDED_PATHS);

        log.info("------>>>系统初始化sso-filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取客户端的请求的地址
        String  servletPath = request.getServletPath();

        // 判断请求的路径是否在排除路径之内
        if (excludedPaths!=null && excludedPaths.trim().length()>0){
             String[] excludedPathStr = excludedPaths.split(",");
             for (String excludedPath : excludedPathStr){
                 String path = excludedPath.trim();
                 // 判断uri和请求的地址
                 if (antPathMatcher.match(path,servletPath)){ // 满足的化，不拦截
                     filterChain.doFilter(servletRequest,servletResponse);
                     return;
                 }
             }
        }

        // 不满足上面的条件的化，就检测其它路径
        if (logoutPath!=null
                && logoutPath.trim().length()>0
                && logoutPath.equals(servletPath)){
            // 删除cookie中登陆信息
            SsoWebLoginHelper.removeSessionIdFromCookie(request,response);
            // 跳转指定地址
            String redirectUrl = serverPath.concat(Conf.SSO_LOGOUT);
            response.sendRedirect(redirectUrl);
            return;
        }

        // 判断用户是否在登陆状态
        AdminUser adminUser = SsoWebLoginHelper.ssoLoginCheck(request, response);
        // 登陆无效，或不在登陆状态，登陆
        if (adminUser==null) {
            String contentType = request.getHeader("content-type");
            // 判断是json格式还是页面跳转
            if (contentType!=null && contentType.contains("json")){ // json格式
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().print("{code:"+Conf.ConfEnum.LOGIN_FAIL.getCode()+",msg:"+Conf.ConfEnum.LOGIN_FAIL.getMsg()+"}");
            } else { // 页面跳转
                String redirectUrl = request.getRequestURL().toString();
                String logoutPath = serverPath.concat(Conf.SSO_LOGIN)+"?"+Conf.REDIRECT_URL+"="+redirectUrl;
                response.sendRedirect(logoutPath);
                return;
            }
        }
        //登陆成功
        request.setAttribute(Conf.SSO_USER,adminUser);

        filterChain.doFilter(servletRequest,servletResponse);

    }

    @Override
    public void destroy() {
        // 可做资源的销毁工作
    }
}
