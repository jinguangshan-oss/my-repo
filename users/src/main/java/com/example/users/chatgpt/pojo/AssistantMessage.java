package com.example.users.chatgpt.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssistantMessage extends Message{

    public AssistantMessage(String content){
        this.role = "assistant";
        this.content = content;
    }

}
