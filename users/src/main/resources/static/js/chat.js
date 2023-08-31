import { getAuthTokenFromCookie } from "./common.js";

//跳转到此页面时，解析url中的用户名
// 获取 URL 中的查询参数部分
var queryString = window.location.search;
// 通过 URLSearchParams 对象解析查询参数
var params = new URLSearchParams(queryString);
// 获取 username 参数的值
var username = params.get("username");

const messageInput = document.getElementById('messageInput');
const sendButton = document.getElementById('sendButton');
const chatBox = document.getElementById('chatBox');
const clearButton = document.getElementById('clearButton');

//监听点击事件
sendButton.addEventListener('click', sendMessage);

// 监听输入框键盘事件
messageInput.addEventListener("keydown", function(event) {
    if (event.key === "Enter") {
        sendMessage();
    }
});

// 监听清除按钮点击事件
clearButton.addEventListener("click", clearChat);

function clearChat() {
    chatBox.innerHTML = ""; // 清空聊天框内容

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
    // 请求后台服务
    fetch('http://localhost:9003/api/chat/cleanMessage', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(response => {
            if (response.code == "200") {
                alert(JSON.stringify(response.data))
            }else {
                alert(JSON.stringify(response.msg))
            }
        })
        .catch(error => console.error('Error:', error));

}

function appendMessage(sender, text) {
    const messageElement = document.createElement('div');
    messageElement.classList.add('message');
    messageElement.innerHTML = `<strong>${sender}:</strong> ${text}`;
    chatBox.appendChild(messageElement);
    chatBox.scrollTop = chatBox.scrollHeight;
}


function sendMessage() {
    const message = messageInput.value;
    if (message.trim() !== '') {
        appendMessage('You', message);

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
            userName: username,
            message: message
        };
        // 发送用户消息到后台服务
        fetch('http://localhost:9003/api/chat/sendMessage', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(response => {
            if (response.code == "200") {
                const serverMessage = response.data;
                appendMessage('Server', serverMessage);
            }else {
                alert(JSON.stringify(response.msg))
            }
        })
        .catch(error => console.error('Error:', error));

        messageInput.value = '';
    }
}