package com.xiaoxiz.xiaomaibu.controller;

import com.xiaoxiz.xiaomaibu.bean.User;
import com.xiaoxiz.xiaomaibu.service.Impl.RedisTemplateServiceImpl;
import com.xiaoxiz.xiaomaibu.service.RedisTemplateService;
import com.xiaoxiz.xiaomaibu.service.UserService;
import com.xiaoxiz.xiaomaibu.util.EmaillUtil;
import com.xiaoxiz.xiaomaibu.util.JWTUtils;
import com.xiaoxiz.xiaomaibu.util.dataresult.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplateService redisTemplateService;

    /*
    *
    * 获取所有用户信息接口
    * */
    @RequestMapping("/getuser")
    public DataResult getUser(){
        List<User> userList=userService.getUser();
        return DataResult.success(userList);
    }

    /**
     *
     * 用户注册接口
     * */
    @PostMapping ("/login")
    public DataResult userLogin(@RequestBody User user){
        boolean flag=userService.Login(user);
        if (flag){
            Map payload = new HashMap<String,String>();
            String uuid_token= UUID.randomUUID().toString();
            String user_id = user.getId()+"";
            payload.put("user_id",user_id);
            payload.put("uuid_token", uuid_token);
            String token= JWTUtils.getTokenRsa(payload);//返回一个token
            //redis缓存用户id和token
            redisTemplateService.hashPutValue("USER_LOGIN_TOKEN",user_id,uuid_token);//将用户登录状态存放到redis
            return DataResult.success(token);
        }
        return DataResult.ERROR();
    }
    /**
     *
     * 用户登录接口
     * */
    @PostMapping("/register")
    public DataResult userRegister(@RequestBody User user){
        boolean flag = userService.Register(user);
        String Subject ="注册成功";
        String text ="您已经注册成功，账号为："+user.getId();
        if (flag){
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        EmaillUtil.send_mail(user.getEmail(),text,Subject);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            return DataResult.success();

        }
        return DataResult.ERROR();
    }
    @PostMapping("/confirm")
    public DataResult userConfirmCode(@RequestBody Map<String,String> map) {

        String toMail = map.get("email");
        System.out.println(toMail);
        String Subject ="验证码";
        String confirmCode = String.valueOf((int)(Math.random() * 888888 + 10000));
        String text ="您的验证码为："+confirmCode+" 提供给他人会导致财产损失";
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    EmaillUtil.send_mail(toMail,text,Subject);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return DataResult.success(confirmCode);
    }
}
