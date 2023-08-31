package com.example.users.chatgpt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPTRequestBody {
    String model;

    double temperature;

    List<UserMessage> messages;

}
