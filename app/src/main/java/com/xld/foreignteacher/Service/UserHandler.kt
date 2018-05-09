package com.xld.foreignteacher.Service

import cn.sinata.xldutils.utils.SPUtils
import com.google.gson.Gson
import com.xld.foreignteacher.api.dto.User

/**
 * Created by cz on 4/19/18.
 */
class UserHandler {

    fun getUser(): User{
        val user= SPUtils.getString("user")
        if (user.isNotEmpty()){
            return Gson().fromJson(user, User::class.java).copy(id = 5)
        }
        return User(id = -1,sex = 1)
    }

    fun saveUser(user: User){
        SPUtils.save("user", Gson().toJson(user))
    }
}