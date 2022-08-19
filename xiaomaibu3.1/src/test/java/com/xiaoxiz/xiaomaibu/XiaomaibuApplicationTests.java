package com.xiaoxiz.xiaomaibu;

import com.auth0.jwt.JWT;
import com.xiaoxiz.xiaomaibu.bean.User;

import com.xiaoxiz.xiaomaibu.service.RedisTemplateService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@RunWith(SpringRunner.class)
class XiaomaibuApplicationTests {
    @Autowired
    DataSource dataSource;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisTemplateService redisTemplateService;
    @Test
    void contextLoads() throws SQLException {
        String email="123@qq.com";
        System.out.println(isEmail(email));

    }
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
