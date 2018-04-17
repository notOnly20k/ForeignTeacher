package com.xld.foreignteacher.api.dto

import com.xld.foreignteacher.ext.ServerException
import org.slf4j.LoggerFactory
import java.io.Serializable

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


data class User(var imgUrl: String? = null, var id: Int = 0, var phone: String? = null, var sex: Int = 0,
                var identCode: String? = null, var nickName: String? = null, var inviteCode: String? = null)

data class Teacher(var id: Int = 0, var phone: String? = null, var sex: Int = 0, var contactInformation: String? = null, var nickName: String? = null,
                   var birthDay: String? = null, var isPassWord: Boolean = false, var albumList: List<AlbumListBean>? = null)

data class AlbumListBean(var imgUrl: String? = null, var sort: Int = 0)

interface SelectData : Serializable {
    abstract var Name: String
    abstract fun firstLetter(): String
}

data class Language(var eName: String?, var cName: String?, var id: Int, var abName: String?) : SelectData, Serializable {
    override var Name: String
        get() = eName!!
        set(value) {
            Name = value
        }

    override fun firstLetter(): String {
        val letter = eName!!.substring(0, 1).toUpperCase()
        return if (letter.matches("[A-Z]".toRegex())) {
            letter
        } else {
            "#"
        }
    }

}

data class City(var code: String, var name: String, var id: Int = 0)

data class Bill(var id: Int = 0, var userId: Int = 0, var money: Int = 0, var remark: String? = null,
                var createTime: Long = 0, var status: Int = 0, var teacherId: Int = 0)

data class OrderMessage(var id: Int = 0, var title: String? = null, var content: String? = null,
                        var addtime: Long = 0, var userId: Int = 0, var type: Int = 0)

data class UnReadMessageCount(var messNum: Int = 0, var noticeNum: Int = 0,
                              var messTitle: String? = null, var noticeTitle: String? = null)

data class SystemMessage(var id: Int = 0, var title: String? = null, var img: String? = null,
                         var url: Any? = null, var addtime: Long = 0)

data class TeacherSchedule(var sortField: Any? = null, var id: Int = 0, var teacherId: Int = 0,
                           var day: Long = 0, var startTime: Int = 0, var endTime: Int = 0, var discount: Double = 1.0,
                           var reservable: Int = 0, var addtime: Long = 0)

data class TeacherRecord(private var id: Int = 0, var userId: Int = 0, var money: Int = 0, var remark: String? = null,
                         var createTime: Long = 0, var status: Int = 0, var teacherId: Int = 0)

data class SquareDate(var commentNum: Int = 0, var content: String? = null, var id: Int = 0, var createTime: Long = 0,
                 var isGiveThum: Boolean = false, var nickName: String? = null, var giveThumNum: Int = 0, var contents: String? = null,
                 var address: String? = null, var teacherImgUrl: String? = null, var teacherId: Int = 0, var squareCommentList: List<SquareCommentListBean>? = null,
                 var imgUrl: List<ImgUrlBean>? = null) {

    class SquareCommentListBean(var content: String? = null, var id: Int = 0, var createTime: Long = 0,
                                var nickName1: String? = null, var nickName: String? = null, var type1: Int = 0,
                                var userId: Int = 0, var userId1: Int = 0, var type: Int = 0, var commentId: Int = 0)

    class ImgUrlBean(var imgUrl: String? = null)
}
