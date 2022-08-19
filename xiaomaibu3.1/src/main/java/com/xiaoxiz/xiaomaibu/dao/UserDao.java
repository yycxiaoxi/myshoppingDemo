package com.xiaoxiz.xiaomaibu.dao;

import com.xiaoxiz.xiaomaibu.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao {
    public void Register(User user);
    public User Login(User user);
    public List<User> getUser();
}
