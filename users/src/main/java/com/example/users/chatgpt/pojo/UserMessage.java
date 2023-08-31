package com.example.users.chatgpt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserMessage extends Message{

    public UserMessage(String content){
        this.role = "user";
        this.content = content;
    }
}
