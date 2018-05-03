package com.xld.foreignteacher.api.dto

import com.xld.foreignteacher.ext.ServerException
import org.slf4j.LoggerFactory
import java.io.Serializable

/**
 * Created by cz on 3/28/18.
 */

private val logger = LoggerFactory.getLogger("Dto")

data class Dto<out T>(val sys: Long, val code: Int, val msg: String, val data: T? = null) {
    fun check(): T {

        if (code != 0) {
            throw ServerException("unknown", msg)
        }
        return data!!
    }
}


data class User(var imgUrl: String? = null, var id: Int = -1, var phone: String? = null, var sex: Int = 1,
                var birthDay: String? = null,
                var identCode: String? = null, var nickName: String = "", var inviteCode: String? = null,
                var lat: Double? = null, var lon: Double? = null)

data class Teacher(var id: Int, var phone: String? = null, var sex: Int, var contactInformation: String? = null, var nickName: String = "",
                   var birthDay: String? = null, var passWord: Boolean = false, var albumList: List<AlbumListBean>? = null, var personalProfile: String?,
                   var isHot: Boolean?, var hotSort: Int?, var openCityId: Int?, var languagesId: String?, var nationality: String?, var chineseLevel: Int? = 0,
                   var age: Int?)

data class AlbumListBean(var imgUrl: String? = null, var sort: Int)

interface SelectData : Serializable {
    var enName: String
    fun firstLetter(): String {
        val letter = enName!!.substring(0, 1).toUpperCase()
        return if (letter.matches("[A-Z]".toRegex())) {
            letter
        } else {
            "#"
        }
    }
}

data class Language(var eName: String?, var cName: String?, var id: Int, var abName: String?) : SelectData, Serializable {
    override var enName: String
        get() = eName!!
        set(value) {
            enName = value
        }


}

data class City(var code: String, var name: String, var id: Int) : SelectData, Serializable {
    override var enName: String
        get() = "Beijin"
        set(value) {
            enName = value
        }

}

data class Bill(var id: Int, var userId: Int, var money: Int, var remark: String? = null,
                var createTime: Long, var status: Int, var teacherId: Int)

data class OrderMessage(var id: Int, var title: String? = null, var content: String? = null,
                        var addtime: Long, var userId: Int, var type: Int)

data class UnReadMessageCount(var messNum: Int, var noticeNum: Int,
                              var messTitle: String? = null, var noticeTitle: String? = null)

data class SystemMessage(var id: Int, var title: String? = null, var img: String? = null,
                         var url: String? = null, var addtime: Long)

data class TeacherSchedule(var sortField: Any? = null, var id: Int, var teacherId: Int,
                           var day: Long, var startTime: Int, var endTime: Int, var discount: Double = 1.0,
                           var reservable: Int, var addtime: Long)

data class TeacherRecord(private var id: Int, var userId: Int, var money: Int, var remark: String? = null,
                         var createTime: Long, var status: Int, var teacherId: Int)

data class SquareDate(var commentNum: Int, var content: String? = null, var id: Int, var createTime: Long,
                      var giveThum: Boolean = false, var nickName: String? = null, var giveThumNum: Int, var contents: String? = null,
                      var address: String? = null, var teacherImgUrl: String? = null, var teacherId: Int, var squareCommentList: List<SquareCommentListBean>? = null,
                      var imgUrl: List<ImgUrlBean>? = null) {

    class SquareCommentListBean(var content: String? = null, var id: Int, var createTime: Long,
                                var nickName1: String? = null, var nickName: String? = null, var type1: Int,
                                var userId: Int, var userId1: Int, var type: Int, var commentId: Int)

    class ImgUrlBean(var imgUrl: String? = null)
}

data class SquareDetail(var commentNum: Int, var content: String? = null, var id: Int, var createTime: Long, var giveThum: Boolean = false,
                        var nickName: String? = null, var giveThumNum: Int, var contents: String? = null, var address: String? = null, var teacherImgUrl: String? = null,
                        var teacherId: Int, var squareCommentList: List<SquareCommentListBean>? = null, var imgUrl: List<ImgUrlBean>? = null) {


    class SquareCommentListBean(var content: String? = null, var imgUrl: String? = null, var id: Int, var createTime: Long,
                                var sex: Int, var nickName: String? = null, var userId: Int, var type: Int,
                                var squareCommentListNum: Int, var squareCommentLists: List<SquareCommentListsBean>? = null) {


        class SquareCommentListsBean(var content: String? = null, var imgUrl: String? = null, var id: Int, var createTime: Long,
                                     var sex: Int, var nickName: String? = null, var userId: Int, var type: Int)
    }

    class ImgUrlBean(var imgUrl: String? = null, var sort: Int? = null)
}

class CommentReply {

    var squareComment: SquareCommentBean? = null
    var squareCommentLists: List<SquareCommentListsBean>? = null

    class SquareCommentBean {


        var id: Int = 0
        var squareId: Int = 0
        var userId: Int = 0
        var content: String? = null
        var commentId: Any? = null
        var createTime: Long = 0
        var type: Int = 0
        var imgUrl: String? = null
            get() = if (field == null) "" else field
        var nickName: String? = null
            get() = if (field == null) "" else field
    }

    class SquareCommentListsBean {


        var content: String? = null
        var imgUrl: String? = null
        var id: Int = 0
        var createTime: Long = 0
        var sex: Int = 0
        var nickName: String? = null
        var userId: Int = 0
        var type: Int = 0
    }
}


data class SquareComment(var squareComment: SquareCommentBean? = null, var squareCommentLists: List<SquareCommentListsBean>? = null) {


    class SquareCommentBean(var id: Int, var squareId: Int, var userId: Int, var content: String? = null,
                            var commentId: Any? = null, var nickName: String? = null, var imgUrl: String? = null, var createTime: Long,
                            var type: Int)

    class SquareCommentListsBean(var content: String? = null, var imgUrl: String? = null, var id: Int, var createTime: Long,
                                 var sex: Int, var nickName: String? = null, var userId: Int, var type: Int)
}

class UserComment {
    var imgUrl: String? = null
    var content: String? = null
    var id: Int = 0
    var createTime: Long = 0
    var nickName: String? = null
    var userId: Int = 0
    var type: Int = 0
}


data class BenchmarkPrice(var id: Int, var benchmarkPrice: Double, var superBaseRatio: Double)

data class Classification(var id: Int, var createTime: Long, var name: String, var state: Int)

data class UserInviteCode(var identCode: String, var isAddInviteCode: Boolean, var name: String, var inviteNum: Int, var money: Int)


data class TeacherDetail(var squareNum: Int = 0, var sex: Int = 0, var curriculumNum: Int = 0, var isCollected: Boolean = false, var score: String? = null,
                         var chineseLevel: Int? = null, var cName: String? = null, var km: String? = null, var eName: String? = null, var commentNum: Int = 0,
                         var id: Int = 0, var imgUrl: String? = null, var personalProfile: String? = null, var personalProfiles: String? = null, var nationality: String? = null,
                         var nickName: String? = null, var squareList: List<SquareListBean>? = null, var curriculumList: List<CurriculumListBean>? = null, var cnacleOrderNum: Int,
                         var commentList: List<CommentListBean>? = null, var albumList: List<AlbumListBean>? = null, var authentication: List<AuthenticationBean>? = null) {


    class SquareListBean(var id: Int = 0, var createTime: Long = 0, var teacherId: Int = 0, var content: String? = null, var contents: String? = null,
                         var imgUrl: Any? = null, var address: String? = null, var browsingVolume: Int = 0, var status: Int = 0, var giveThumNum: Int = 0,
                         var commentNum: Int = 0, var imgList: List<ImgListBean>? = null) {

        class ImgListBean(var imgUrl: String? = null, var sort: Int? = null)
    }

    class CurriculumListBean(var id: Int = 0, var title: String? = null, var languagesId: Int = 0, var classificationId: Int = 0,
                             var price: Int = 0, var status: Int = 0, var teachersId: Int = 0, var craeteTime: Long = 0, var name: String? = null,
                             var cName: String? = null, var eName: String? = null)

    class CommentListBean(var createTime: Long = 0, var userImgUrl: String? = null, var nickName: String? = null,
                          var remark: String? = null, var userId: Int = 0, var score: Int = 0, var imgUrl: List<ImgUrlBean>? = null)

    class AlbumListBean(var imgUrl: String? = null, var sort: Int = 0)

    class AuthenticationBean(var name: String? = null, var status: Int = 0)
}

data class SquareListBean(var teacherImgUrl: String? = null, var address: String? = null, var giveThumNum: Int = 0,
                          var nickName: String? = null, var giveThum: Boolean = false, var content: String? = null,
                          var commentNum: Int = 0, var teacherId: Int = 0, var createTime: Long = 0,
                          var contents: String? = null, var id: Int = 0, var imgUrl: List<ImgUrlBean>? = null, var squareCommentList: List<*>? = null)

class ImgUrlBean(var imgUrl: String? = null, var sort: Int? = null)
