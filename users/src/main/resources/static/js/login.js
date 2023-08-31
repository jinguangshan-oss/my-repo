import { setCookie } from "./common.js";
// 获取按钮元素
var registerButton = document.getElementById("registerButton");

// 添加点击事件监听器
registerButton.addEventListener("click", function() {
    window.location.href = "/html/register.html";
});

// 获取表单元素
var form = document.getElementById("myForm");

// 添加表单提交事件监听器
form.addEventListener("submit", function(event) {
    event.preventDefault(); // 阻止表单的默认提交行为

    // 获取用户名/密码输入框的值
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    // 准备要发送的数据，可以是一个对象
    var data = {
        userName: username,
        password: password
    };

    // 请求jwt
    fetch("http://localhost:9003/api/login/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "omit",
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);
        if(data.code == "200"){
            // 设置Cookie
            setCookie(username, data.data);
            // 跳转到控制面板页
            window.location.href = "../html/dashboard.html?username=" + encodeURIComponent(username);
        }else {
            alert(JSON.stringify(data.msg))
        }
    })
    .catch(error => {
        console.error("Error:", error);
    });

});



