package com.xiaoxiz.xiaomaibu.service;

import com.xiaoxiz.xiaomaibu.bean.User;

import java.util.List;

public interface UserService {
    public List<User> getUser();
    public boolean Login(User user);
    public boolean Register(User user);
}
