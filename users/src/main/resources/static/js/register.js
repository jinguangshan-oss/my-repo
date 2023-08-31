document.addEventListener("DOMContentLoaded", function() {
    var registrationForm = document.getElementById("registrationForm");

    registrationForm.addEventListener("submit", function(event) {
        event.preventDefault();

        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;
        var mail = document.getElementById("mail").value;
        var phone = document.getElementById("phone").value;

        // 创建一个对象存储用户注册信息
        var userData = {
            userName: username,
            password: password,
            mail: mail,
            phone: phone
        };

        // 注册用户
        fetch("http://localhost:9003/api/login/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "omit",
            body: JSON.stringify(userData)
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                alert("注册成功");
                if(data.code == "200"){
                    // 跳转到控制面板页
                    window.location.href = "/html/login.html";
                }else {
                    alert(JSON.stringify(data.msg))
                }
            })
            .catch(error => {
                console.error("Error:", error);
            });
    });
});