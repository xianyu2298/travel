package org.example.service;


import org.example.entity.User;

public interface UserService {

    boolean isExistById(Integer id);
    Integer login(User user);

    boolean isExistByUsername(String username);
    boolean register(User user);

}
