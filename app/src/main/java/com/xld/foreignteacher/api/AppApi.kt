package com.xld.foreignteacher.api

import com.xld.foreignteacher.api.dto.*
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
    fun getCommentList(@Query("key") key: String): Single<Dto<SquareComment>>

    @POST("ForeignTeachers/app/server")
    fun getBenchmarkPrice(@Query("key") key: String): Single<Dto<List<BenchmarkPrice>>>

    @POST("ForeignTeachers/app/server")
    fun getClassificationList(@Query("key") key: String): Single<Dto<List<Classification>>>

    @POST("ForeignTeachers/app/server")
    fun getUserInviteCode(@Query("key") key: String): Single<Dto<UserInviteCode>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherInfo(@Query("key") key: String): Single<Dto<Teacher>>

    @POST("ForeignTeachers/app/server")
    fun getMyPersonalTrainingOrder(@Query("key") key: String): Single<Dto<Any>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherDetail(@Query("key") key: String): Single<Dto<TeacherDetail>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherCommentList(@Query("key") key: String): Single<Dto<List<TeacherDetail.CommentListBean>>>

    @POST("ForeignTeachers/app/server")
    fun getCurriculumList(@Query("key") key: String): Single<Dto<List<TeacherDetail.CurriculumListBean>>>

    @POST("ForeignTeachers/app/server")
    fun getTeacherSquareList(@Query("key") key: String): Single<Dto<List<SquareListBean>>>


}
