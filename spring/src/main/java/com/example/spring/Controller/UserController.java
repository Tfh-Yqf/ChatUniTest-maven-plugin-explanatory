package com.example.spring.Controller;

import com.example.spring.Service.UserService;
import com.example.spring.pojo.Entity.ReturnCode;
import com.example.spring.pojo.Entity.UserEntity;
import com.example.spring.pojo.Entity.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 登录注册模块
 */
@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ReturnCode register(@RequestBody UserEntity user) throws IOException {
        ReturnCode res  = new ReturnCode();

        if(user.email==null){
            res.excuteFail(1,"Email not found");
        }else if(user.code==null||user.code.length()!=6){
            res.excuteFail(2,"Code not legal");
        }else if(user.password==null||user.password.length()<=6){
            res.excuteFail(2,"password not legal");
        }else{
            return userService.registerService(user);
        }

        return res;
    }

    @PostMapping("/login")
    public ReturnCode loginByPassword(@RequestBody UserEntity user) throws IOException {
        ReturnCode res  = new ReturnCode();

        if(user.email==null){
            res.excuteFail(1,"Email not found");
        }else if(user.password==null||user.password.length()<=6){
            res.excuteFail(2,"password not legal");
        }else{
            return userService.loginByPassword(user);
        }

        return res;
    }
    @PostMapping("/checktoken")
    public ReturnCode checkToken(@RequestBody UserEntity user) throws IOException {
        ReturnCode res  = new ReturnCode();

        if(user.token==null){
            res.excuteFail(8,"token not found");
        }else{
            return userService.checkToken(user);
        }

        return res;
    }

    @PostMapping("/sendEmail")
    public ReturnCode sendEmail(@RequestBody VerificationCode verificationCode) throws IOException {
        ReturnCode res  = new ReturnCode();

        if(verificationCode.getEmail()==null){
            res.excuteFail(1,"Email not found");
        }else{
            return userService.sendEmail(verificationCode);
        }

        return res;
    }
}
