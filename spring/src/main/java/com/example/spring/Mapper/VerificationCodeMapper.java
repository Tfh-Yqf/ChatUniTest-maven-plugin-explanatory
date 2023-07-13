package com.example.spring.Mapper;

import com.example.spring.pojo.Entity.VerificationCode;
import org.apache.ibatis.annotations.*;

@Mapper
public interface VerificationCodeMapper {

    @Insert("INSERT INTO verification_code (email, code) VALUES (#{email}, #{code})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertVerificationCode(VerificationCode verificationCode);

    @Delete("DELETE FROM verification_code WHERE id = #{id}")
    void deleteVerificationCodeById(int id);

    @Delete("DELETE FROM verification_code WHERE email = #{email}")
    void deleteVerificationCodeByEmail(String email);

    @Update("UPDATE verification_code SET email = #{email}, code = #{code} WHERE id = #{id}")
    void updateVerificationCode(VerificationCode verificationCode);

    @Select("SELECT * FROM verification_code WHERE id = #{id}")
    VerificationCode getVerificationCodeById(int id);

    @Select("SELECT * FROM verification_code WHERE email = #{email}")
    VerificationCode getVerificationCodeByEmail(String email);
}
