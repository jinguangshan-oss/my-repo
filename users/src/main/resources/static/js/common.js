
/**
 * 设置Cookie，有效期为1小时
 * @param key
 * @param value
 */
export function setCookie(key, value) {
    //过期时间 = 当前时间 + 1小时
    var expires = new Date();
    expires.setTime(expires.getTime() + (1 * 60 * 60 * 1000));
    //设置cookie
    document.cookie = key + "=" + value + ";"
        + "expires" + "=" + expires.toUTCString() + ";"
        + "path=/";

    //document.cookie形如："user=example; expires=Wed, 28 Aug 2024 12:00:00 UTC; path=/"
    // key=value
    // expires=过期时间
    // path=cookie的有效路径 /表示这个Cookie将在整个域名下的所有路径下都是可见的 /dashboard 表示这个Cookie只会在路径为 /dashboard 下的页面中可见。
}


/**
 * 从 cookie 中获取认证凭证
 * @param key
 * @returns {string|null}
 */
export function getAuthTokenFromCookie(key) {
    key = key + "=";
    var cookieArray = document.cookie.split(';');

    for (var i = 0; i < cookieArray.length; i++) {
        var cookie = cookieArray[i];
        //去除首位的空格
        while (cookie.charAt(0) === ' ') {
            cookie = cookie.substring(1);
        }
        //截取jwt
        if (cookie.indexOf(key) === 0) {
            return cookie.substring(key.length, cookie.length);
        }
    }
    return null;
}

/**
 * 清除保存在 cookie 中的认证凭证
 * @param key
 */
export function clearAuthTokenFromCookie(key) {
    document.cookie = key + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}