package com.whynuttalk.foreignteacher.api

import com.whynuttalk.foreignteacher.api.dto.*
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by cz on 3/28/18.
 */
interface AppApi {

    @POST("ForeignTeachers/app/server")
    fun response(@Query("key") key: String): Single<Dto<Any>>

    @POST("ForeignTeachers/app/server")
    fun login(@Query("key") key: String): Single<Dto<User>>

    @POST("ForeignTeachers/app/server")
    fun sendMsg(@Query("key") key: String): Single<Dto<String>>

    @POST("ForeignTeachers/app/server")
    fun register(@Query("key") key: String): Single<Dto<User>>

    @POST("ForeignTeachers/app/server")
    fun getLanguage(@Query("key") key: String): Single<Dto<List<Language>>>

    @POST("ForeignTeachers/app/server")
    fun getOpenCity(@Query("key") key: String): Single<Dto<List<City>>>

    @POST("ForeignTeachers/app/server")
    fun getNotOpenCity(@Query("key") key: String): Single<Dto<List<City>>>

    @POST("ForeignTeachers/app/server")
    fun getBills(@Query("key") key: String): Single<Dto<List<Bill>>>

    @POST("ForeignTeachers/app/server")
    fun getOrderMessage(@Query("key") key: String): Single<Dto<List<OrderMessage>>>

    @POST("ForeignTeachers/app/server")
    fun getUnReadMessageCount(@Query("key") key: String): Single<Dto<UnReadMessageCount>>

    @POST("ForeignTeachers/app/server")
    fun getSystemMessage(@Query("key") key: String): Single<Dto<List<SystemMessage>>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherSchedule(@Query("key") key: String): Single<Dto<List<TeacherSchedule>>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherRecord(@Query("key") key: String): Single<Dto<List<TeacherRecord>>>

    @POST("ForeignTeachers/app/server")
    fun getSquareList(@Query("key") key: String): Single<Dto<List<SquareDate>>>

    @POST("ForeignTeachers/app/server")
    fun getSquareDetail(@Query("key") key: String): Single<Dto<SquareDetail>>

    @POST("ForeignTeachers/app/server")
    fun getCommentList(@Query("key") key: String): Single<Dto<CommentReply>>

    @POST("ForeignTeachers/app/server")
    fun getBenchmarkPrice(@Query("key") key: String): Single<Dto<BenchmarkPrice>>

    @POST("ForeignTeachers/app/server")
    fun getClassificationList(@Query("key") key: String): Single<Dto<List<Classification>>>

    @POST("ForeignTeachers/app/server")
    fun getUserInviteCode(@Query("key") key: String): Single<Dto<UserInviteCode>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherInfo(@Query("key") key: String): Single<Dto<Teacher>>

    @POST("ForeignTeachers/app/server")
    fun getMyPersonalTrainingOrder(@Query("key") key: String): Single<Dto<PersonalTrainingOrder>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherDetail(@Query("key") key: String): Single<Dto<TeacherDetail>>

    @POST("ForeignTeachers/app/server")
    fun getUserHomePage(@Query("key") key: String): Single<Dto<UserHomeData>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherFightList(@Query("key") key: String): Single<Dto<List<GroupOrder>>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherFightDetail(@Query("key") key: String): Single<Dto<GroupOrderDetail>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherCommentList(@Query("key") key: String): Single<Dto<List<TeacherDetail.CommentListBean>>>

    @POST("ForeignTeachers/app/server")
    fun getCurriculumList(@Query("key") key: String): Single<Dto<List<TeacherDetail.CurriculumListBean>>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherSquareList(@Query("key") key: String): Single<Dto<List<SquareListBean>>>

    @POST("ForeignTeachers/app/server")
    fun queryTeacherBankAccout(@Query("key") key: String): Single<Dto<List<TeacherBankAccout>>>

    @POST("ForeignTeachers/app/server")
    fun getMyWallet(@Query("key") key: String): Single<Dto<WalletDetail>>

    @POST("ForeignTeachers/app/server")
    fun getOrderNum(@Query("key") key: String): Single<Dto<Any>>


    @POST("ForeignTeachers/app/server")
    fun withdrawDetails(@Query("key") key: String): Single<Dto<List<WithDrawDetail>>>

    @POST("ForeignTeachers/app/server")
    fun getMyAuthentication(@Query("key") key: String): Single<Dto<MyAuthentication>>

    @POST("ForeignTeachers/app/server")
    fun addSquareComment(@Query("key") key: String): Single<Dto<UserComment>>

    @POST("ForeignTeachers/app/server")
    fun getUserInfo(@Query("key") key: String): Single<Dto<Student>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherInfoSurvey(@Query("key") key: String): Single<Dto<TeacherInfo>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherFightFirstList(@Query("key") key: String): Single<Dto<List<TeacherFight>>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherCurricuumFirstList(@Query("key") key: String): Single<Dto<List<TeacherCurriculum>>>

    @POST("ForeignTeachers/app/server")
    fun getCurriculumDetail(@Query("key") key: String): Single<Dto<TeacherCurriculum>>

    @POST("ForeignTeachers/app/server")
    fun getSingleOrderDetail(@Query("key") key: String): Single<Dto<SingleOrderDetail>>

    @POST("ForeignTeachers/app/server")
    fun getMyPersonalTrainingChildOrder(@Query("key") key: String): Single<Dto<TrainingChildOrder>>

    @POST("ForeignTeachers/app/server")
    fun getAuthenticationDetail(@Query("key") key: String): Single<Dto<List<AuthenticationDetail>>>

    @POST("ForeignTeachers/app/server")
    fun getBlacklist(@Query("key") key: String): Single<Dto<List<BlockUser>>>


}
