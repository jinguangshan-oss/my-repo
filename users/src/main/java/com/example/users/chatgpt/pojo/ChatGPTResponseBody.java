package com.example.users.chatgpt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPTResponseBody {
    String id;
    String object;
    String created;
    String model;
    String usage;
    String choices;
}
