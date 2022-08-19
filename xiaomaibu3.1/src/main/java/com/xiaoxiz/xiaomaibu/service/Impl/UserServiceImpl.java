package com.xiaoxiz.xiaomaibu.service.Impl;

import com.xiaoxiz.xiaomaibu.bean.User;
import com.xiaoxiz.xiaomaibu.dao.UserDao;
import com.xiaoxiz.xiaomaibu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public List<User> getUser() {
        return userDao.getUser();
    }

    @Override
    public boolean Login(User user) {
       User db_user = userDao.Login(user);
       if (db_user!=null){
           if (db_user.getPassword().equals(user.getPassword())){
               return true;
           }
       }
       return false;
    }

    @Override
    public boolean Register(User user) {
        userDao.Register(user);
        return true;
    }
}
