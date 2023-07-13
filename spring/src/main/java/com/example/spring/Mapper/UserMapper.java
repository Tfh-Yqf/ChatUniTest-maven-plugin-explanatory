package com.example.spring.Mapper;

import com.example.spring.pojo.Entity.UserEntity;
import org.apache.ibatis.annotations.*;


public interface UserMapper {
    @Select("SELECT * FROM account WHERE id = #{id}")
    UserEntity getUserById(int id);

    @Insert("INSERT INTO account (email, password, code) VALUES (#{email}, #{password}, #{code})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(UserEntity user);

    @Update("UPDATE account SET email = #{email}, password = #{password}, code = #{code} WHERE id = #{id}")
    void updateUser(UserEntity user);

    @Update("UPDATE account SET token = #{token} WHERE email = #{email}")
    void updateUserToken(UserEntity user);

    @Delete("DELETE FROM account WHERE id = #{id}")
    void deleteUser(int id);

    @Select("SELECT * FROM account WHERE email = #{email}")
    UserEntity getUserByEmail(String email);

    @Select("SELECT * FROM account WHERE email = #{email} and password = #{password}")
    UserEntity getUserByEmailAndPassword(String email,String password);
}
