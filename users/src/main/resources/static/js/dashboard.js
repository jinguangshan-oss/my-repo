import { getAuthTokenFromCookie } from "./common.js";

//跳转到此页面时，解析url中的用户名
// 获取 URL 中的查询参数部分
var queryString = window.location.search;
// 通过 URLSearchParams 对象解析查询参数
var params = new URLSearchParams(queryString);
// 获取 username 参数的值
var username = params.get("username");



// 获取按钮元素
var userMsgButton = document.getElementById("userMsgButton");

// 添加点击事件监听器
userMsgButton.addEventListener("click", function() {
    // 请求用户信息
    requestForUser(username);
});

//查看用户信息
function requestForUser(username) {
    //获取cookie中的jwt
    var jwt = getAuthTokenFromCookie(username);
    //cookie过期跳转登录页面
    if(jwt == null){
        window.location.href= "/html/login.html";
    }
    // 构造请求头对象，包含 Authorization 头部
    var headers = new Headers();
    headers.append("Authorization", "Bearer " + jwt);
    headers.append("Content-Type", "application/json");
    // 准备要发送的数据，可以是一个对象
    var data = {
        userName: username
    };

    // 使用 fetch API 发起请求
    fetch("http://localhost:9003/api/user/select", {
        method: "Post",
        headers: headers,
        body: JSON.stringify(data),
        //"omit"： 默认值。在跨域请求中不携带身份凭证，不会发送 cookie 等凭证信息。
        //"same-origin"： 仅在同源请求时携带身份凭证，对于跨域请求不发送凭证。
        //"include"： 在所有请求中都携带身份凭证，无论是否跨域。适用于需要在跨域请求中发送 cookie 或其他凭证信息的情况。
        credentials: "omit"
    })
    .then(response => response.json())
    .then(response => {
        if (response.code == "200") {
            alert(JSON.stringify(response.data)); // 获取响应的文本内容
        } else {
            alert(JSON.stringify(data.msg))
        }
    })
    .catch(error => {
        console.error("Error:", error);
    });
}