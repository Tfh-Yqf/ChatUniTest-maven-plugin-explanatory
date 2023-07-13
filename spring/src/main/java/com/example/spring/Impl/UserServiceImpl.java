package com.example.spring.Impl;

import com.example.spring.Mapper.UserMapper;
import com.example.spring.Mapper.VerificationCodeMapper;
import com.example.spring.Service.UserService;
import com.example.spring.pojo.Entity.ReturnCode;
import com.example.spring.pojo.Entity.UserEntity;
import com.example.spring.pojo.Entity.VerificationCode;
import com.example.spring.utils.EmailSend;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.spring.utils.PasswordEncoderUtil.encode;
import static com.example.spring.utils.PasswordEncoderUtil.matches;
import static com.example.spring.utils.TokenGenerator.*;


@Service
public class UserServiceImpl implements UserService {

    private static final int CODE_LENGTH = 6;
    private static final String DIGITS = "0123456789";

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

    @Autowired
    EmailSend emailSend;

    /**
     * 注册
     * @param user
     * @return
     * @throws IOException
     */
    @Override
    public ReturnCode registerService(UserEntity user) throws IOException {
        ReturnCode res = new ReturnCode();

        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        VerificationCodeMapper verificationCodeMapper = sqlSession.getMapper(VerificationCodeMapper.class);

        // 检查账户是否存在
        UserEntity existingUser = userMapper.getUserByEmail(user.email);
        if (existingUser != null) {
            res.excuteFail(3,"账户已存在");
            return res;
        }

        // 验证验证码
        VerificationCode verificationCode = verificationCodeMapper.getVerificationCodeByEmail(user.email);
        if (verificationCode==null||!validateCode(user.code,verificationCode.getCode())) {
            res.excuteFail(4,"验证码不正确");
            return res;
        }else{
            verificationCodeMapper.deleteVerificationCodeByEmail(verificationCode.getEmail());
        }

        // 密码加密
        user.password = encode(user.password);

        // 插入用户到数据库
        userMapper.insertUser(user);
        sqlSession.commit();

        res.excuteSuccess();

        return res;
    }

    /**
     * 登录
     * @param user
     * @return
     * @throws IOException
     */
    public ReturnCode loginByPassword(UserEntity user) throws IOException {
        ReturnCode res = new ReturnCode();

        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);


        // 检查账户是否存在
        UserEntity existingUser = userMapper.getUserByEmail(user.email);
        if (existingUser == null) {
            res.excuteFail(6,"密码或用户名错误");
            return res;
        }else if(!matches(user.password,existingUser.password)){
            res.excuteFail(6,"密码或用户名错误!");
            return res;
        }
        existingUser.password = "********************";
        existingUser.code = "*********";

        existingUser.token = generateToken(existingUser.email);
        userMapper.updateUserToken(existingUser);

        res.excuteSuccess(existingUser);
        return res;
    }

    /**
     * token校验
     * @param user
     * @return
     * @throws IOException
     */
    public ReturnCode checkToken(UserEntity user) throws IOException {
        ReturnCode res = new ReturnCode();

        if(!isTokenValid(user.token)){
            res.excuteFail(9,"token不合法");
        }else if(isTokenExpired(user.token)){
            res.excuteFail(10,"token过期");
        }else{
            res.excuteSuccess();
        }

        return res;
    }

    /**
     * 发送邮箱验证码
     * @param verificationCode
     * @return
     * @throws IOException
     */
    public ReturnCode sendEmail(VerificationCode verificationCode) throws IOException {
        ReturnCode res = new ReturnCode();
        if(!isValidEmail(verificationCode.getEmail())){
            res.excuteFail(5,"邮箱地址错误");
            return res;
        }

        String code = generateCode();
        verificationCode.setCode(code);
        emailSend.contextLoads(code, verificationCode.getEmail());

        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        VerificationCodeMapper verificationCodeMapper = sqlSession.getMapper(VerificationCodeMapper.class);

        verificationCodeMapper.insertVerificationCode(verificationCode);

        res.excuteSuccess();
        return res;
    }

    private static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean validateCode(String code,String rightCode) {
        // 验证验证码逻辑
        return code.equals(rightCode);
    }

    private static String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(DIGITS.length());
            code.append(DIGITS.charAt(index));
        }
        return code.toString();
    }
}
