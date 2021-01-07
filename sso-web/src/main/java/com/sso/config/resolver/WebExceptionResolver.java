package com.sso.config.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by nings on 2020/12/7.
 * 全局异常处理器
 */
@Slf4j
@Component
public class WebExceptionResolver implements HandlerExceptionResolver{


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object o, Exception e) {
        log.error("异常处理{}",e.getMessage());
        String jsonStr = null;
        if (e instanceof NullPointerException){
            jsonStr = "{\"code\":1000,msg:空指针异常}";
        }
        try {
            response.getWriter().print(jsonStr);
        } catch (IOException e1) {
            e1.printStackTrace();
            log.error("处理异常时报错，异常详情：{}",e1.getMessage());
        }
        return null;
    }
}
