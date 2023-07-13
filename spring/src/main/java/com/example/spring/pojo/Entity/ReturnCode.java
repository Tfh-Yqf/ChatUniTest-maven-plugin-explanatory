package com.example.spring.pojo.Entity;

import lombok.Data;

@Data
public class ReturnCode {

    public int code;

    public String msg;

    public Object data;

    public void excuteSuccess(Object res){
        data = res;
        code = 0;
        msg = "执行成功";
    }

    public void excuteSuccess(){
        code = 0;
        msg = "执行成功";
    }

    public void excuteFail(int code,String msg,Object res){
        this.data = res;
        this.code = code;
        this.msg = msg;
    }

    public void excuteFail(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
