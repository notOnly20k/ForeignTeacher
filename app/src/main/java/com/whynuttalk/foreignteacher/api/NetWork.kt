package com.whynuttalk.foreignteacher.api


import com.whynuttalk.foreignteacher.AppComponent
import com.whynuttalk.foreignteacher.api.dto.*
import com.whynuttalk.foreignteacher.ext.e
import com.whynuttalk.foreignteacher.ext.logErrorAndForget
import com.whynuttalk.foreignteacher.ext.toNetWork
import com.whynuttalk.foreignteacher.ext.toast
import com.whynuttalk.foreignteacher.util.DES
import io.reactivex.Maybe
import org.slf4j.LoggerFactory

/**
 * Created by cz on 3/28/18.
 */
class NetWork(val appComponent: AppComponent, val api: AppApi) {
    val logger = LoggerFactory.getLogger("NetWork")
    val Passtype = "Japanese,Korean,Russian,German,Hindi,Arabic,Portuguese,Spanish,Bengali"


    fun getNationality(): MutableList<Nationality> {
        val Nationality = "English,French,Japanese,Korean,Russian,German,Hindi,Arabic,Portuguese,Spanish,Bengali. "
        val list = mutableListOf<Nationality>()
        Nationality.split(",").map { list.add(Nationality(it)) }
        return list
    }

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
                    contactInformation: String, chineseLevel: Int? = null, nationality: String? = null, languagesId: String? = null,
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

    //
    fun addSquareComment(userId: Int, squareId: Int, content: String, commentId: Int?, type: Int = 2): Maybe<UserComment> {
        val commid = if (commentId == null) {
            ""
        } else {
            "&commentId=$commentId"
        }

        val key = DES.encryptDES("server=/app/userSquare/addSquareComment?userId=$userId&squareId=$squareId&content=$content$commid&type=$type")
        logger.e { "server=/app/userSquare/addSquareComment?userId=$userId&squareId=$squareId&content=$content$commid&type=$type" }
        return appComponent.appApi
                .addSquareComment(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取广场评论列表
    fun getSquareCommentList(id: Int, page: Int, rows: Int): Maybe<CommentReply> {

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
    fun addOffer(teachersId: Int, title: String, languagesId: Int, classificationId: Int, price: Int, id: Int?): Maybe<Any> {
        val id = if (id == null) {
            ""
        } else {
            "&id=$id"
        }
        val key = DES.encryptDES("server=/app/userFight/addCurriculum?teachersId=$teachersId&languagesId=$languagesId&title=$title" +
                "&classificationId=$classificationId&price=$price$id")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取地区均价
    fun getBenchmarkPrice(id: Int): Maybe<BenchmarkPrice> {
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
    fun getTeacherInviteCode(id: Int): Maybe<UserInviteCode> {
        val key = DES.encryptDES("server=/app/teacher/getTeacherInviteCode?&id=$id")
        return appComponent.appApi
                .getUserInviteCode(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //接单
    fun takeOrder(orderId: Int): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/takeOrder?&orderId=$orderId")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //拒单
    fun refuseOrder(orderId: Int, refuseReason: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/refuseOrder?&orderId=$orderId&refuseReason=$refuseReason")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }


    //评价订单
    fun evlauateUser(orderId: Int, userId: Int, teacherId: Int, listening: Double, grammar: Double, vocabulary: Double, pronunciation: Double, fluency: Double): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/evlauateUser?&orderId=$orderId&userId=$userId&teacherId=$teacherId" +
                "&listening=$listening&grammar=$grammar&vocabulary=$vocabulary&pronunciation=$pronunciation&fluency=$fluency")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取私教子订单
    fun getMyPersonalTrainingChildOrder(orderId: Int): Maybe<TrainingChildOrder> {
        val key = DES.encryptDES("server=/app/teacher/getMyPersonalTrainingChildOrder?orderId=$orderId")
        return appComponent.appApi
                .getMyPersonalTrainingChildOrder(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取个人订单列表
    fun getMyPersonalTrainingOrder(teacherId: Int, state: Int, page: Int, rows: Int): Maybe<PersonalTrainingOrder> {
        val key = DES.encryptDES("server=/app/teacher/getMyPersonalTrainingOrder?teacherId=$teacherId&state=$state&page=$page&rows=$rows")
        return appComponent.appApi
                .getMyPersonalTrainingOrder(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取个人订单详情
    fun getPersonalTrainingOrderDetail(orderId: Int): Maybe<SingleOrderDetail> {
        val key = DES.encryptDES("server=/app/teacher/getPersonalTrainingOrderDetail?orderId=$orderId")
        return appComponent.appApi
                .getSingleOrderDetail(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }


    //删除私教订单
    fun deletePersonalTrainingOrder(orderId: Int, bookingUserId: Int? = null): Maybe<Any> {
        val bookingUserId = if (bookingUserId == null) {
            ""
        } else {
            "&bookingUserId=$bookingUserId"
        }
        val key = DES.encryptDES("server=/app/user/deletePersonalTrainingOrder?orderId=$orderId$bookingUserId")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //取消主订单
    fun userCancelMainOrder(orderId: Int, calcelReason: String, reasonDescribe: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/user/teacherCancelMainOrder?orderId=$orderId&calcelReason=$calcelReason&reasonDescribe=$reasonDescribe")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //取消单个订单
    fun userCancelSingleOrder(orderId: Int, bookingTeacherId: Int, calcelReason: String, reasonDescribe: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/user/teacherCancelSingleOrder?orderId=$orderId&bookingTeacherId=$bookingTeacherId&calcelReason=$calcelReason&reasonDescribe=$reasonDescribe")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取个人拼单列表
    fun getTeacherFightList(id: Int, status: Int, page: Int, rows: Int): Maybe<List<GroupOrder>> {
        val key = DES.encryptDES("server=/app/userFight/getTeacherFightList?id=$id&status=$status&page=$page&rows=$rows")
        return appComponent.appApi
                .getTeacherFightList(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //添加拼拼订单
    fun addFight(teacherId: Int, languagesId: Int, enrolment: Int, classesNumber: Int, price: Int, title: String, phone: String, address: String,
                 lat: String, lon: String, backgroundCourse: String, introduce: String, introduceImgs: String, deadlineRegistration: String,
                 reservationDeadline: String, openingTime: String, endTime: String): Maybe<Any> {
        logger.e {
            "server=/app/userFight/addFight?teacherId=$teacherId&languagesId=$languagesId&enrolment=$enrolment&classesNumber=$classesNumber" +
                    "&price=$price&title=$title&phone=$phone&address=$address&lat=$lat&lon=$lon&backgroundCourse=$backgroundCourse&introduce=$introduce&introduceImgs=$introduceImgs" +
                    "&deadlineRegistration=$deadlineRegistration&reservationDeadline=$reservationDeadline&openingTime=$openingTime&endTime=$endTime"
        }
        val key = DES.encryptDES("server=/app/userFight/addFight?teacherId=$teacherId&languagesId=$languagesId&enrolment=$enrolment&classesNumber=$classesNumber" +
                "&price=$price&title=$title&phone=$phone&address=$address&lat=$lat&lon=$lon&backgroundCourse=$backgroundCourse&introduce=$introduce&introduceImgs=$introduceImgs" +
                "&deadlineRegistration=$deadlineRegistration&reservationDeadline=$reservationDeadline&openingTime=$openingTime&endTime=$endTime")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取个人拼单详情
    fun getFightDetail(id: Int, lat: Double, lon: Double): Maybe<GroupOrderDetail> {
        val key = DES.encryptDES("server=/app/userFight/getFightDetail?id=$id&lat=$lat&lon=$lon")
        return appComponent.appApi
                .getTeacherFightDetail(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }


    //删除拼单
    fun delFigh(id: Int): Maybe<Any> {
        val key = DES.encryptDES("server=/app/userFight/delFight?id=$id")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //订单数
    fun getOrderNum(id: Int, type: Int): Maybe<Any> {
        val key = DES.encryptDES("server=/app/userFight/getOrderNum?id=$id&type=$type")
        return appComponent.appApi
                .getOrderNum(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //取消拼单
    fun cancelFigh(id: Int, cancelRemark: String, remark: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/userFight/cancleFight?id=$id&cancelRemark=$cancelRemark&remark=$remark")
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

    //获取学生详情
    fun getUserHomePage(id: Int): Maybe<UserHomeData> {
        val key = DES.encryptDES("server=/app/public/getUserHomePage?id=$id")
        return appComponent.appApi
                .getUserHomePage(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取学生详情
    fun getUserInfo(id: Int): Maybe<Student> {
        val key = DES.encryptDES("server=/app/user/getUserInfo?id=" + id)
        return appComponent.appApi
                .getUserInfo(key)
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
    }

    //获取私教银行卡详情
    fun queryTeacherBankAccout(foreignId: Int): Maybe<List<TeacherBankAccout>> {
        val key = DES.encryptDES("server=/app/teacher/queryTeacherBankAccout?foreignId=$foreignId")
        return appComponent.appApi
                .queryTeacherBankAccout(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }

    }

    //获取我的钱包
    fun getMyWallet(teacherId: Int): Maybe<WalletDetail> {
        val key = DES.encryptDES("server=/app/teacher/myWallet?teacherId=$teacherId")
        return appComponent.appApi
                .getMyWallet(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取提现详情
    fun withdrawDetails(teacherId: Int, page: Int, rows: Int): Maybe<List<WithDrawDetail>> {
        val key = DES.encryptDES("server=/app/teacher/withdrawDetails?teacherId=$teacherId&page=$page&rows=$rows")
        return appComponent.appApi
                .withdrawDetails(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //添加银行卡
    fun addBankAccount(foreignId: Int, name: String, account: String, role: Int): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/addBankAccount?foreignId=$foreignId&name=$name&account=$account&role=$role")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //申请认证
    fun applyAuthentication(teacherId: Int, certificateName: String?, credentialsNumber: String, credentialsImg: String, type: Int): Maybe<Any> {
        val certificateName = if (certificateName != null) {
            "&certificateName=$certificateName"
        } else {
            ""
        }

        val key = DES.encryptDES("server=/app/teacher/applyAuthentication?teacherId=$teacherId$certificateName&credentialsNumber=$credentialsNumber&credentialsImg=$credentialsImg&type=$type")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取认证详情
    fun getAuthenticationDetail(teacherId: Int, state: Int?, type: Int): Maybe<List<AuthenticationDetail>> {
        val state = if (state == null) {
            ""
        } else {
            "&state=$state"
        }
        val key = DES.encryptDES("server=/app/teacher/authenticationDetail?teacherId=$teacherId$state&type=$type")
        return appComponent.appApi
                .getAuthenticationDetail(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取认证
    fun myAuthentication(teacherId: Int): Maybe<MyAuthentication> {
        val key = DES.encryptDES("server=/app/teacher/myAuthentication?teacherId=$teacherId")
        return appComponent.appApi
                .getMyAuthentication(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //申请提现
    fun applyWithdraw(foreignId: Int, bankAccountId: Int, withdrawMoney: Double, role: Int = 2): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/applyWithdraw?foreignId=$foreignId&bankAccountId=$bankAccountId&withdrawMoney=$withdrawMoney&role=$role")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //举报用户
    fun addComplaint(teacherId: Int, userId: Int, reason: String, content: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/public/addComplaint?teacherId=$teacherId&userId=$userId&reason=$reason&content=$content")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }


    //拉黑用户
    fun addBlacklist(teacherId: Int, userId: Int): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/addBlacklist?teacherId=$teacherId&userId=$userId")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //取消拉黑用户
    fun removeBlacklist(id: Int): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/removeBlacklist?id=$id")
        return appComponent.appApi
                .response(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //黑名单列表
    fun getBlacklist(teacherId: Int, page: Int, rows: Int): Maybe<List<BlockUser>> {
        val key = DES.encryptDES("server=/app/teacher/getBlacklist?teacherId=$teacherId&page=$page&rows=$rows")
        return appComponent.appApi
                .getBlacklist(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取个人私教列表
    fun getTeacherCurricuumFirstList(id: Int, page: Int, rows: Int): Maybe<List<TeacherCurriculum>> {
        val key = DES.encryptDES("server=/app/userFight/getTeacherCurriculumFirstList?id=$id&page=$page&rows=$rows")
        return appComponent.appApi
                .getTeacherCurricuumFirstList(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    //获取个人私教详情
    fun getCurriculumDetail(id: Int): Maybe<TeacherCurriculum> {
        val key = DES.encryptDES("server=/app/privateEducationOrder/getCurriculumDetail?id=$id")
        return appComponent.appApi
                .getCurriculumDetail(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }


    //获取个人拼单列表
    fun getTeacherFightFirstList(id: Int, rows: Int, page: Int): Maybe<List<TeacherFight>> {
        val key = DES.encryptDES("server=/app/userFight/getTeacherFightFirstList?id=$id&rows=$rows&page=$page")
        return appComponent.appApi
                .getTeacherFightFirstList(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    fun getTeacherInfoSurvey(teacherId: Int): Maybe<TeacherInfo> {
        val key = DES.encryptDES("server=/app/teacher/teacherInfoSurvey?teacherId=$teacherId")
        return appComponent.appApi
                .getTeacherInfoSurvey(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }


    fun getH5Url(type: Int): String {
        return "${BaseUrl}ForeignTeachers/app/public/getAppText?type=$type"
    }

    fun ShareTeacher(id: Int): String {

        return "http://47.93.219.253/ForeignTeachers/resources/share/teacher.html?id=$id"
    }

    val DownLoadWeb = "http://47.93.219.253/ForeignTeachers/resources/share/downU.html"
    val TeacherInvite = "http://47.93.219.253/ForeignTeachers/resources/share/code.html?id=${appComponent.userHandler.getUser().id}"


    companion object {
        val BaseUrl = "http://www.whynuttalk.com/"
        val TestUrl = "http://192.168.3.139:8080/"

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
