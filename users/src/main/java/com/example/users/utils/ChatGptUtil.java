package com.example.users.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.users.chatgpt.pojo.AssistantMessage;
import com.example.users.chatgpt.pojo.ChatGPTRequestBody;
import com.example.users.chatgpt.pojo.Message;
import com.example.users.chatgpt.pojo.UserMessage;
import okhttp3.*;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatGptUtil {

    String apiKey = "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";  // 替换为你的实际API密钥
    String apiUrl = "https://api.openai.com/v1/chat/completions";



    /**
     * 聊天
     */
    public String chat(List<Message> chatHistory){
        //通讯工具
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10809));
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(90, TimeUnit.SECONDS).proxy(proxy).build();

        //AI响应内容
        String assistantMessage = "";

        //创建请求
        Request request = createRequest(chatHistory);

        //发起请求
        try {
            Response response = client.newCall(request).execute();
            //打印相应
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                assistantMessage = parseModelReply(responseBody);
                chatHistory.add(new AssistantMessage(assistantMessage));
            } else {
                System.out.println("ChatGPT failed: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assistantMessage;
    }

    /**
     * 创建请求
     * @param chatHistory
     * @return
     */
    private Request createRequest(List chatHistory){
        //创建请求体
        List contentList = new ArrayList<UserMessage>();
        contentList.addAll(chatHistory);
        ChatGPTRequestBody requestBody = new ChatGPTRequestBody();
        requestBody.setModel("gpt-3.5-turbo");
        requestBody.setTemperature(0.7);
        requestBody.setMessages(contentList);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(requestBody));

        //创建请求
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        return request;
    }

    /**
     * 解析响应中ai的回复内容
     * @param response
     * @return
     */
    private String parseModelReply(String response) {
        try {
            JSONObject jsonResponse = JSONObject.parseObject(response);
            JSONArray choices = jsonResponse.getJSONArray("choices");

            if (choices.size() > 0) {
                JSONObject replyObject = choices.getJSONObject(0);
                String modelReply = replyObject.getJSONObject("message").getString("content");
                return modelReply;
            } else {
                return "No reply from model.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response.";
        }
    }
}
