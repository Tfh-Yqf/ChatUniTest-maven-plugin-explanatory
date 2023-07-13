package zju.cst.aces.utils;

import zju.cst.aces.parser.ErrorParser;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author volunze
 * @date 2023/6/26 13:15
 * @className: ProcessError
 * @description: Delete unnecessary error messages
 * @version 1.0
 */
public class ErrorProcesser {
    public static String processErrorMessage(List<String> msg, int allowedTokens) throws IOException {
        if(allowedTokens<=0)
            return "";
        ErrorParser errorParser = new ErrorParser();
        TestMessage testMessage = errorParser.loadMessage(msg);
        TokenCounter tokenCounter = new TokenCounter();
        List<String> errors = testMessage.getErrorMessage();
        String errorMessage = String.join(" ",errors);
        while(tokenCounter.countToken(errorMessage) > allowedTokens){
            if(errorMessage.length()>50){
                errorMessage=errorMessage.substring(0,errorMessage.length()-50);
            }else{
                break;
            }
        }
        return errorMessage;
    }
    public static String processErrorMessage1(List<String> msg, int allowedTokens) throws IOException {
        if(allowedTokens<=0)
            return "";
        ErrorParser errorParser = new ErrorParser();
        TestMessage testMessage = errorParser.loadMessage(msg);
        TokenCounter tokenCounter = new TokenCounter();
        List<String> errors = testMessage.getErrorMessage();
        String errorMessage = String.join(" ",errors);

        List<String> save = new ArrayList<>();
        List<String> hashResult = new ArrayList<>();
        for(String error:errors){
            // 查看是否有相同的哈希值
            String tempHash = calculateHash(error);
            boolean haveSame = false;
            for(String hash:hashResult){
                if(tempHash.equals(hash)){
                    haveSame = true;
                    break;
                }
            }
            if(!haveSame) {
                save.add(error);
                hashResult.add(tempHash);
            }else{
                log.info("重复加1")
            }
        }

        return processErrorMessage(save,allowedTokens);
    }

    public static String calculateHash(String input) {
        try {
            // 创建MessageDigest对象，指定使用的哈希算法，如MD5、SHA-1、SHA-256等
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 将输入字符串转换为字节数组
            byte[] inputBytes = input.getBytes();

            // 计算哈希值
            byte[] hashBytes = md.digest(inputBytes);

            // 将哈希值转换为十六进制字符串
            StringBuilder hashBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hashBuilder.append(String.format("%02x", b));
            }
            return hashBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
