package com.xld.foreignteacher.api


import com.xld.foreignteacher.AppComponent
import com.xld.foreignteacher.api.dto.*
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.ext.logErrorAndForget
import com.xld.foreignteacher.ext.toNetWork
import com.xld.foreignteacher.ext.toast
import com.xld.foreignteacher.util.DES
import io.reactivex.Maybe
import org.slf4j.LoggerFactory

/**
 * Created by cz on 3/28/18.
 */
class NetWork(val appComponent: AppComponent, val api: AppApi) {
    val logger = LoggerFactory.getLogger("NetWork")

    //登录
    fun login(phone: String, pwd: String): Maybe<User> {
        val key = DES.encryptDES("server=/app/teacher/login?phone=$phone&passWord=$pwd")

        return appComponent.appApi
                .login(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)

    }

    //发送验证码
    fun sendMsg(phone: String, type: String): Maybe<String> {
        logger.e { "sendMsg==================" }
        val key = DES.encryptDES("server=/app/public/sendSms?phone=$phone&type=$type")
        logger.e { "phone=$phone type=$type  key=$key" }
        return appComponent.appApi
                .sendMsg(key!!)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //校验验证码
    fun checkMsg(phone: String, code: String, type: String): Maybe<Any> {

        val key = DES.encryptDES("server=/app/public/checkSms?phone=$phone&code=$code&type=$type")
        return appComponent.appApi
                .response(key!!)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //注册
    fun register(phone: String, pwd: String): Maybe<User> {
        logger.e { "register==========" }
        val key = DES.encryptDES("server=/app/teacher/register?phone=$phone&passWord=$pwd")
        return appComponent.appApi
                .register(key!!)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //重置密码
    fun resetPwd(phone: String, pwd: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/forgetPassword?phone=$phone&passWord=$pwd")
        logger.e { "resetPwd==========" }
        return appComponent.appApi
                .response(key!!)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //填写邀请码
    fun addInviteCode(id: Int, inviteCode: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/addInviteCode?id=$id&inviteCode=$inviteCode")

        return appComponent.appApi
                .response(key!!)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //校验密码
    fun checkPassWord(id: Int, pwd: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/checkPassword?id=$id&passWord=$pwd")

        return appComponent.appApi
                .response(key!!)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取教师信息
    fun getTeacherInfo(id: Int): Maybe<Teacher> {
        val key = DES.encryptDES("server=/app/teacher/getTeacherInfo?id=$id")

        return appComponent.appApi
                .getTeacherInfo(key!!)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //编辑教师信息
    fun editTeacher(id: Int, nickName: String, imgUrl: String? = null, sex: Int, birthDay: String,
                    contactInformation: String, chineseLevel: Int? = null, nationality: String? = null, languagesId: Int? = null,
                    openCityId: Int? = null, personalProfile: String? = null, albumImgUrl: String? = null): Maybe<Any> {
        val imgUrl = if (imgUrl != null) {
            "&imgUrl=$imgUrl"
        } else {
            ""
        }
        val chineseLevel = if (chineseLevel != null) {
            "&chineseLevel=$chineseLevel"
        } else {
            ""
        }
        val nationality = if (nationality != null) {
            "&nationality=$nationality"
        } else {
            ""
        }
        val languagesId = if (languagesId != null) {
            "&languagesId=$languagesId"
        } else {
            ""
        }
        val openCityId = if (openCityId != null) {
            "&openCityId=$openCityId"
        } else {
            ""
        }
        val personalProfile = if (personalProfile != null) {
            "&personalProfile=$personalProfile"
        } else {
            ""
        }
        val albumImgUrl = if (albumImgUrl != null) {
            "&albumImgUrl=$albumImgUrl"
        } else {
            ""
        }
        val keyCode = "id=$id&nickName=$nickName$imgUrl&sex=$sex&birthDay=$birthDay&contactInformation=$contactInformation" +
                "$chineseLevel$nationality$languagesId$openCityId$personalProfile$albumImgUrl"
        logger.e { "updateTeacherInfo==========$keyCode" }
        val key = DES.encryptDES("server=/app/teacher/updateTeacherInfo?$keyCode")
        return appComponent.appApi
                .response(key!!)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取语言种类
    fun getLanguage(): Maybe<List<Language>> {
        val key = DES.encryptDES("server=/app/userFight/getLanguagesList?")
        return appComponent.appApi
                .getLanguage(key)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取开通城市
    fun getOpenedCity(): Maybe<List<City>> {
        val key = DES.encryptDES("server=/app/userFight/getOpenCity?")
        return appComponent.appApi
                .getOpenCity(key)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取未开通城市
    fun getNotOpenCity(): Maybe<List<City>> {
        val key = DES.encryptDES("server=/app/userFight/getNotOpenCity?")
        return appComponent.appApi
                .getNotOpenCity(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取收益
    fun getBills(id: Int, type: Int, page: Int, rows: Int): Maybe<List<Bill>> {
        val key = DES.encryptDES("server=/app/public/getTransactionRecord?id=$id&type=$type&page=$page&rows=$rows")
        return appComponent.appApi
                .getBills(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取订单列表
    fun getOrderMessage(id: Int, type: Int, page: Int, rows: Int): Maybe<List<OrderMessage>> {
        val key = DES.encryptDES("server=/app/notice/getMessageList?id=$id&type=$type&page=$page&rows=$rows")
        return appComponent.appApi
                .getOrderMessage(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取系统消息
    fun getSystemMessage(id: Int, type: Int, page: Int, rows: Int): Maybe<List<SystemMessage>> {
        val key = DES.encryptDES("server=/app/notice/getNoticeList?id=$id&type=$type&page=$page&rows=$rows")
        return appComponent.appApi
                .getSystemMessage(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取未读消息
    fun getUnReadMessageCount(id: Int, type: Int): Maybe<UnReadMessageCount> {
        val key = DES.encryptDES("server=/app/notice/getUserMess?id=$id&type=$type")
        return appComponent.appApi
                .getUnReadMessageCount(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //发送反馈
    fun addFeedBack(content: String, id: Int): Maybe<Any> {
        val key = DES.encryptDES("server=/app/public/addFeedBack?id=$id&content=$content&type=2")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取时间表
    fun getTeacherSchedule(id: Int, day: String): Maybe<List<TeacherSchedule>> {
        logger.e { "getTeacherSchedule----->day==$day     id = $id" }
        val key = DES.encryptDES("server=/app/public/getTeacherBooking?teacherId=$id&day=$day")
        return appComponent.appApi
                .getTeacherSchedule(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //设置时间表状态与折扣
    fun setReservableAndDiscount(type: Int, bookingTeacherInfo: String): Maybe<Any> {

        val key = DES.encryptDES("server=/app/public/setReservableAndDiscount?type=$type&bookingTeacherInfo=$bookingTeacherInfo")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取收益
    fun getTeacherRecord(id: Int, page: Int, rows: Int, type: Int = 2): Maybe<List<TeacherRecord>> {

        val key = DES.encryptDES("server=/app/public/getTransactionRecord?id=$id&id=$id&page=$page&type=$type&rows=$rows")
        return appComponent.appApi
                .getTeacherRecord(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取广场列表
    fun getSquareList(id: Int, page: Int, rows: Int, type: Int = 2): Maybe<List<SquareDate>> {

        val key = DES.encryptDES("server=/app/userSquare/getSquareList?userId=$id&page=$page&type=$type&rows=$rows")
        return appComponent.appApi
                .getSquareList(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //广场详情
    fun getSquareDetail(id: Int, userId: Int, page: Int, rows: Int, type: Int = 2): Maybe<SquareDetail> {

        val key = DES.encryptDES("server=/app/userSquare/getSquareDetail?userId=$userId&id=$id&page=$page&type=$type&rows=$rows")
        return appComponent.appApi
                .getSquareDetail(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取广场评论列表
    fun getSquareCommentList(id: Int, page: Int, rows: Int): Maybe<SquareComment> {

        val key = DES.encryptDES("server=/app/userSquare/getSquareCommentList?id=$id&page=$page&rows=$rows")
        return appComponent.appApi
                .getCommentList(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //点赞
    fun addGiveThum(squareId: Int, userId: Int, type: Int = 2): Maybe<Any> {
        val key = DES.encryptDES("server=/app/userSquare/addGiveThum?userId=$userId&id=$squareId&type=$type")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //发布广场
    fun addSquare(teacherId: Int, imgUrl: String, address: String, content: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/userSquare/addSquare?teacherId=$teacherId&imgUrl=$imgUrl&address=$address&content=$content")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //添加私教课
    fun addOffer(teachersId: Int, title: String, languagesId: Int, classificationId: Int, price: Int): Maybe<Any> {
        val key = DES.encryptDES("server=/app/userFight/addCurriculum?teachersId=$teachersId&languagesId=$languagesId&title=$title" +
                "&classificationId=$classificationId&price=$price")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取地区均价
    fun getBenchmarkPrice(id: Int): Maybe<List<BenchmarkPrice>> {
        val key = DES.encryptDES("server=/app/userFight/getBenchmarkPrice?id=$id")
        return appComponent.appApi
                .getBenchmarkPrice(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取课程类型
    fun getClassificationList(): Maybe<List<Classification>> {
        val key = DES.encryptDES("server=/app/userTeacher/getClassificationList")
        return appComponent.appApi
                .getClassificationList(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取邀请码
    fun getUserInviteCode(id: Int): Maybe<UserInviteCode> {
        val key = DES.encryptDES("server=/app/user/getUserInviteCode?&id=$id")
        return appComponent.appApi
                .getUserInviteCode(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //接单
    fun takeOrder(orderId: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/takeOrder?&orderId=$orderId")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //拒单
    fun refuseOrder(orderId: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/refuseOrder?&orderId=$orderId")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //取消主要订单
    fun cancelMainOrder(orderId: String, calcelReason: String, reasonDescribe: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/cancelMainOrder?&orderId=$orderId&reasonDescribe=$reasonDescribe&calcelReason=$calcelReason")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //取消私教订单
    fun cancelSingleOrder(orderId: String, bookingTeacherId: Int, calcelReason: String, reasonDescribe: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/cancelMainOrder?&orderId=$orderId&bookingTeacherId=$bookingTeacherId&reasonDescribe=$reasonDescribe&calcelReason=$calcelReason")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取个人订单
    fun getMyPersonalTrainingOrder(teacherId: Int, state: Int, page: Int, rows: Int): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/getMyPersonalTrainingOrder?teacherId=$teacherId&state=$state&page=$page&rows=$rows")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取教师详情
    fun getTeacherDetail(id: Int, lat: Double, lon: Double): Maybe<TeacherDetail> {
        val key = DES.encryptDES("server=/app/userTeacher/getTeacherDetail?id=$id&lat=$lat&lon=$lon")
        return appComponent.appApi
                .getTeacherDetail(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取教师评论列表
    fun getTeacherCommentList(teacherId: Int, page: Int, rows: Int): Maybe<List<TeacherDetail.CommentListBean>> {

        val key = DES.encryptDES("server=/app/userTeacher/getCommentList?teacherId=$teacherId&page=$page&rows=$rows")
        return appComponent.appApi
                .getTeacherCommentList(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取课程详情
    fun getCurriculumList(teacherId: Int, page: Int, rows: Int): Maybe<List<TeacherDetail.CurriculumListBean>> {
        val key = DES.encryptDES("server=/app/userTeacher/getCurriculumList?teacherId=$teacherId&page=$page&rows=$rows")
        return appComponent.appApi
                .getCurriculumList(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取朋友圈详情
    fun getTeacherSquareList(teacherId: Int, page: Int, rows: Int, type: Int): Maybe<List<SquareListBean>> {
        val key = DES.encryptDES("server=/app/userTeacher/getTeacherSquareList?teacherId=$teacherId&page=$page&rows=$rows&type=$type")
        return appComponent.appApi
                .getTeacherSquareList(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }


//    fun addComplaint(teacherId:Int,imgUrl: String,address:String,content:String):Maybe<Any>{
//        val key = DES.encryptDES("server=/app/public/addComplaint?teacherId=$teacherId&imgUrl=$imgUrl&address=$address&content=$content")
//        return appComponent.appApi
//                .addSquare(key)
//                .toMaybe()
//                .toNetWork()
//                .map { it.check() }
//                .logErrorAndForget(Throwable::toast)
//    }


    fun getH5Url(type: Int): String {
        return "${BaseUrl}ForeignTeachers/app/public/getAppText?type=$type"
    }


    companion object {
        val BaseUrl = "http://www.whynuttalk.com/"

        val TYPE_CHARGE_RULES = 1
        val TYPE_AGREEMENT = 2
        val TYPE_PLANT_RULES = 3
        val TYPE_ABOUT_US = 4
        val TYPE_INVITE_RULES = 5
        val TYPE_ORDER_DECLINE_RULES = 6
        val TYPE_PINPIN_RULES = 7
        val TYPE_FOREIGN = 9

    }
}
