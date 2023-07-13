package com.example.spring.Service;

import com.example.spring.Mapper.UserMapper;
import com.example.spring.pojo.Entity.ReturnCode;
import com.example.spring.pojo.Entity.UserEntity;
import com.example.spring.pojo.Entity.VerificationCode;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public interface UserService {


    public ReturnCode registerService(UserEntity user) throws IOException ;
    public ReturnCode loginByPassword(UserEntity user) throws IOException ;
    public ReturnCode checkToken(UserEntity user) throws IOException ;

    public ReturnCode sendEmail(VerificationCode verificationCode) throws IOException;
}
