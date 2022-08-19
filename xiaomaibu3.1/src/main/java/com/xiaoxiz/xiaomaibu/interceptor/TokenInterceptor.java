package com.xiaoxiz.xiaomaibu.interceptor;


import cn.hutool.json.JSONUtil;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xiaoxiz.xiaomaibu.service.Impl.RedisTemplateServiceImpl;
import com.xiaoxiz.xiaomaibu.service.RedisTemplateService;
import com.xiaoxiz.xiaomaibu.util.JWTUtils;
import com.xiaoxiz.xiaomaibu.util.dataresult.DataResult;
import org.apache.ibatis.plugin.Intercepts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    RedisTemplateService redisTemplateService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        String JWT = request.getHeader("Authorization");
        System.out.println(JWT);
        try {
            // 1.校验JWT字符串
            DecodedJWT decodedJWT = JWTUtils.decodeRsa(JWT);
            // 2.取出JWT字符串载荷中的随机token，从Redis中获取用户信息
            String user_id = decodedJWT.getClaim("user_id").asString();
            System.out.println(user_id);
            String uuid_token = decodedJWT.getClaim("uuid_token").asString();
            String redis_uuid_token=redisTemplateService.hashGetValue("USER_LOGIN_TOKEN",user_id).toString();
            if(redis_uuid_token.equals(uuid_token)){
                return true;
            }else {
                response.getWriter().println(JSONUtil.toJsonStr(DataResult.ERROR(10000,"无效签名")));
            }

        }catch (SignatureVerificationException e){
            response.getWriter().println(JSONUtil.toJsonStr(DataResult.ERROR(10000,"无效签名")));
            e.printStackTrace();
        }catch (TokenExpiredException e){
            response.getWriter().println(JSONUtil.toJsonStr(DataResult.ERROR(10000,"token已经过期")));
            e.printStackTrace();
        }catch (AlgorithmMismatchException e){
            response.getWriter().println(JSONUtil.toJsonStr(DataResult.ERROR(10000,"算法不一致")));
            e.printStackTrace();
        }catch (Exception e){
            response.getWriter().println(JSONUtil.toJsonStr(DataResult.ERROR(10000,"token无效")));
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //System.out.println("postHandle方法在控制器的处理请求方法调用之后，解析视图之前执行");


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //System.out.println("preHandle方法在控制器的处理请求方法调用之后，解析视图之前执行");
    }
}