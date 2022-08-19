package com.xiaoxiz.xiaomaibu.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdminDao {
    public boolean adminLogin();
    public boolean adminRegister();
}
