package org.example.mapper;

import org.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    boolean isExistById(Integer id);
    Integer login(User user);

}
