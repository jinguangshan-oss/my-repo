package com.example.users.controller;

import com.example.users.chatgpt.pojo.AssistantMessage;
import com.example.users.chatgpt.pojo.UserMessage;
import com.example.users.pojo.Message;
import com.example.users.pojo.Response;
import com.example.users.utils.ChatGptUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatGptController {

    @Resource
    RedisTemplate redisTemplate;

    @RequestMapping("/sendMessage")
    public Response sendMessage(@RequestBody Message message){

        //获取聊天历史
        List<com.example.users.chatgpt.pojo.Message> chatHistory = (List<com.example.users.chatgpt.pojo.Message>) redisTemplate.opsForValue().get(message.getUserName());
        if(chatHistory == null){
            chatHistory = new ArrayList<>();
        }
        chatHistory.add(new UserMessage(message.getMessage()));

        //发起调用
        ChatGptUtil util = new ChatGptUtil();
        String aiMessage = util.chat(chatHistory);

        //缓存聊天历史
        redisTemplate.opsForValue().set(message.getUserName(), chatHistory);

        return Response.ok(aiMessage);
    }

    @RequestMapping("/cleanMessage")
    public Response cleanMessage(@RequestBody Message message){
        redisTemplate.delete(message.getUserName());
        return Response.ok("清除成功！");
    }
}
