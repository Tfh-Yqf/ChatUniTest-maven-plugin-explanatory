package zju.cst.aces.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import zju.cst.aces.ProjectTestMojo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AskGPT extends ProjectTestMojo {
    private static final String URL = "https://api.openai.com/v1/chat/completions";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    private static  OkHttpClient client;
    private static String hostname;
    private static String port;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    /**
     * 可以优化一下-每次都需要配置吗？
     * 首先获取并配置代理信息，设置client配置
     * 然后设置请求的一些参数，并处理相应
     * @param messages
     * @return
     */
    public Response askChatGPT(List<Message> messages) {
        setProxyStr();
        if(!hostname.equals("null") && !port.equals("-1")){
            setClinetwithProxy();
        }else {
            setClinet();
        }

        // Token限额
        int AskCostTokens = TokenCounter.countToken(messages);
        if(AskCostTokens+Config.haveAskCostTokens+Config.haveResponseCostTokens>Config.maxUseTokens){
            log.error("\n==========================\n[ChatTester] Reach the maximum Token usage");
            return null;
        }

        String apiKey = Config.getRandomKey();
        int maxTry = 5;
        while (maxTry > 0) {
            try {
                Map<String, Object> payload = new HashMap<>();
                payload.put("messages", messages);
                payload.put("model", Config.model);
                payload.put("temperature", Config.temperature);
                payload.put("top_p", Config.topP);
                payload.put("frequency_penalty", Config.frequencyPenalty);
                payload.put("presence_penalty", Config.presencePenalty);
                String jsonPayload = GSON.toJson(payload);

                RequestBody body = RequestBody.create(MEDIA_TYPE, jsonPayload);
                Request request = new Request.Builder()
                        .url(URL)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer " + apiKey)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                // 记录token使用情况
                Config.setHaveCostAskTokens(Config.haveAskCostTokens+AskCostTokens);

                return response;
            } catch (IOException e) {
                System.out.println("In AskGPT.askChatGPT: " + e);
                if (e.getMessage().contains("maximum context length is ")) {
                    break;
                }
                if (e.getMessage().contains("Rate limit reached")) {
                    try {
                        Thread.sleep(new Random().nextInt(60) + 60);
                    } catch (InterruptedException ie) {
                        throw new RuntimeException("In AskGPT.askChatGPT: " + ie);
                    }
                }
                maxTry--;
            }
        }
        log.debug("AskGPT: Failed to get response\n");
        return null;
    }

    /**
     * 分割，如127.0.0.1:7890
     */
    public static void setProxyStr(){
        hostname=Config.proxy.split(":")[0];
        port=Config.proxy.split(":")[1];
    }

    /**
     * 无代理的连接配置
     */
    public static void setClinet(){
        //System.out.println("setClinet without proxy");
        client=new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
    }

    /**
     * 有代理的连接配置
     */
    public static void setClinetwithProxy(){
        //System.out.println("hostname:"+Config.hostName+" port:"+Config.port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostname, Integer.parseInt(port)));
        client=new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .proxy(proxy)//自定义代理
                .build();
        //System.out.println("setClinet with proxy");
    }
}