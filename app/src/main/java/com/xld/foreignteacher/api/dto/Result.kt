package com.xld.foreignteacher.api.dto

import com.xld.foreignteacher.ext.ServerException
import org.slf4j.LoggerFactory

/**
 * Created by cz on 3/28/18.
 */

private val logger = LoggerFactory.getLogger("Dto")

data class Dto<out T>(val sys: Long, val code: Int = 0, val msg: String, val data: T? = null) {
    fun check(): T {

        if (code != 0) {
            throw ServerException("unknown", msg)
        }
        return data!!
    }
}


class User(var imgUrl: String? = null, var id: Int = 0, var phone: String? = null, var sex: Int = 0,
           var identCode: String? = null, var nickName: String? = null, var inviteCode: String? = null)

class Teacher(var id: Int = 0, var phone: String? = null, var sex: Int = 0, var contactInformation: String? = null, var nickName: String? = null,
              var birthDay: String? = null, var isPassWord: Boolean = false, var albumList: List<AlbumListBean>? = null)

class AlbumListBean(var imgUrl: String? = null, var sort: Int = 0)

class Language(var eName: String?, var cName: String?, var id: Int, var abName: String?) {

    fun firstLetter():String{
                    val letter = eName!!.substring(0, 1).toUpperCase()
        return if (letter!!.matches("[A-Z]".toRegex())) {
            letter
        } else {
            "#"
        }
    }
}
