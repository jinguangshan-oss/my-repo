package com.example.users.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private String code;

    private String msg;

    private T data;

    public static Response ok(Object data){
        Response response = new Response();
        response.setCode("200");
        response.setMsg("请求成功！");
        response.setData(data);
        return response;
    }

    public static Response fail(Object data,String msg){
        Response response = new Response();
        response.setCode("500");
        response.setMsg(msg);
        response.setData(data);
        return response;
    }
}
