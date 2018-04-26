package com.xld.foreignteacher.ext

import java.util.regex.Pattern

/**
 * Created by cz on 3/29/18.
 */
fun String.toMD5(): String {
    val md = java.security.MessageDigest.getInstance("MD5")
    val array = md.digest(toByteArray())
    val sb = StringBuilder()
    for (b in array) {
        sb.append(Integer.toHexString((b.toInt() and 255) or 256).substring(1, 3))
    }
    return sb.toString()
}

/**
 * 判断邮箱格式是否有效
 * @param email 邮箱地址
 * @return true 是正确格式
 */
fun String.isEmailValid(): Boolean {
    var isValid = false
    val email = toString()
    //        String expression = "^([a-z0-9A-Z]+[-|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$";
    val pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")
    val matcher = pattern.matcher(email)
    if (matcher.matches()) {
        isValid = true
    }
    return isValid
}

/**
 * 检查字符串是否为电话号码的方法,并返回true or false的判断值
 */
fun String.isPhoneNumberValid(): Boolean {
    var isValid = false
    if (isEmpty() ||length != 11) {
        return false
    }
    val expression = "(^(13|14|15|17|16|18)[0-9]{9}$)"
    val pattern = Pattern.compile(expression)
    val matcher = pattern.matcher(toString())
    if (matcher.matches()) {
        isValid = true
    }
    return isValid
}

fun String.formateToTel():String{
    return if (isPhoneNumberValid()){
        StringBuffer().append(toString()).insert(3,"-").insert(8,"-").toString()
    }else{
        ""
    }
}

fun String.formateToNum():String{
    return replace("-", "")
}




