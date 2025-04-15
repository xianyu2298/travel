package org.example.Impl;

import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public boolean isExistById(Integer id) {
        return userMapper.isExistById(id);
    }
    @Override
    public Integer login(User uer){
        return userMapper.login(uer);
    }
}
