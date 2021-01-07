package com.sso.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sso.conf.Conf;
import com.sso.core.model.UserInfo;
import com.sso.entity.AdminUser;
import com.sso.login.SsoWebLoginHelper;
import com.sso.service.ITUserService;
import com.sso.store.SsoLoginStore;
import com.sso.store.SsoSessionIdHelper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author suning
 * @since 2020-12-06
 */
@Controller
@RequestMapping("/")
@Api(value = "单点登陆控制类",description = "包含登陆验证和登陆")
public class WebLoginController {

    @Autowired
   public ITUserService itUserService;

    @GetMapping("/")
    public String index(HttpServletRequest request,HttpServletResponse response,Model model){
        AdminUser adminUser = SsoWebLoginHelper.ssoLoginCheck(request, response);
        if (adminUser!=null){
            model.addAttribute("adminUser",adminUser);
            return "index";
        }
        return "redirect:/login";
    }


    @PostMapping(value = "/doLogin")
    public String doLogin(HttpServletRequest request, HttpServletResponse response,
                          RedirectAttributes redirectAttributes,
                          String username,
                          String password,
                          String ifRemember){
        // 记住
        boolean ifRem = (ifRemember!=null&&"on".equals(ifRemember))?true:false;
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("password",password);
        UserInfo userinfo = itUserService.getOne(queryWrapper);
        if (userinfo==null){
            redirectAttributes.addAttribute("msg","用户名或密码无效");
            // 跳转登陆页面携带跳转url
            redirectAttributes.addAttribute(Conf.REDIRECT_URL,request.getParameter(Conf.REDIRECT_URL));
            return "redirect:/login";
        }
        // 创建登陆对象
        AdminUser adminUser = new AdminUser();
        adminUser.setUserid(userinfo.getUserid().toString());
        adminUser.setUsername(username);
        adminUser.setVersion(UUID.randomUUID().toString().replace("-",""));
        adminUser.setExpireMinite(SsoLoginStore.getRedisExpireMinite());
        adminUser.setExpireFreshTime(System.currentTimeMillis());

        // 创建seesionid
        String makeSessionId = SsoSessionIdHelper.makeSessionId(adminUser);

        // 保存redis，和设置cookie
        SsoWebLoginHelper.login(response,makeSessionId,adminUser,ifRem);

        // 登陆成功跳转到指定路径
        String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
        if (redirectUrl!=null && redirectUrl.trim().length()>0){
            redirectUrl = redirectUrl+"?"+Conf.SSO_SESSIONID+"="+makeSessionId;
            return "redirect:"+redirectUrl;
        } else {
            return "redirect:/";
        }
    }

    @GetMapping(Conf.SSO_LOGIN)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model){
        AdminUser adminUser = SsoWebLoginHelper.ssoLoginCheck(request,response);
        if (adminUser!=null){
            // 已登陆，跳转到回调页
            String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
            if (redirectUrl!=null && redirectUrl.trim().length()>0) {
                String cookieSessionId = SsoWebLoginHelper.getSessionIdByCookie(request);
                redirectUrl = redirectUrl+"?"+Conf.SSO_SESSIONID+"="+cookieSessionId;
                return "redirect:"+redirectUrl;
            } else {
                return "redirect:/";
            }

        }
        model.addAttribute("err_code","-999");
        model.addAttribute(Conf.REDIRECT_URL,request.getParameter(Conf.REDIRECT_URL));
        return "login";
    }


    /**
     * 退出登陆
     * @param request
     * @param response
     * @return
     */
    @PostMapping(Conf.SSO_LOGOUT)
    public String logout(HttpServletRequest request,HttpServletResponse response,
                         RedirectAttributes redirectAttributes){
        // 退出
        SsoWebLoginHelper.logout(request,response);
        redirectAttributes.addAttribute(Conf.REDIRECT_URL,request.getParameter(Conf.REDIRECT_URL));
        return "redirect:/login";
    }

}
