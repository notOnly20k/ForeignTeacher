package com.xld.foreignteacher.api


import com.xld.foreignteacher.AppComponent
import com.xld.foreignteacher.api.dto.City
import com.xld.foreignteacher.api.dto.Language
import com.xld.foreignteacher.api.dto.User
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

    fun login(phone: String, pwd: String): Maybe<User> {
        val key = DES.encryptDES("server=/app/teacher/login?phone=$phone&passWord=$pwd")

        return appComponent.appApi
                .login(key)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)

    }

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

    fun checkMsg(phone: String, code: String, type: String): Maybe<Any> {

        val key = DES.encryptDES("server=/app/public/checkSms?phone=$phone&code=$code&type=$type")
        return appComponent.appApi
                .checkMsg(key!!)
                .toMaybe()
                .toNetWork()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

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

    fun resetPwd(phone: String, pwd: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/forgetPassword?phone=$phone&passWord=$pwd")
        logger.e { "resetPwd==========" }
        return appComponent.appApi
                .resetPwd(key!!)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    fun addInviteCode(id: Int, inviteCode: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/addInviteCode?id=$id&inviteCode=$inviteCode")

        return appComponent.appApi
                .addInviteCode(key!!)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    fun checkPassWord(id: Int, pwd: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/checkPassword?id=$id&passWord=$pwd")

        return appComponent.appApi
                .checkPassWord(key!!)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    fun getTeacher(id: Int, pwd: String): Maybe<Any> {
        val key = DES.encryptDES("server=/app/teacher/checkPassword?id=$id&passWord=$pwd")

        return appComponent.appApi
                .checkPassWord(key!!)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    fun editTeacher(id: Int, nickName: String, imgUrl: String? = null, sex: String, birthDay: String,
                    contactInformation: String, chineseLevel: Int? = null, nationality: String? = null, languagesId: Int? = null,
                    openCityId: Int? = null, personalProfile: String? = null, albumImgUrl: String? = null): Maybe<Any> {
        val imgUrl = if (imgUrl != null) {
            "imgUrl=$imgUrl"
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
                .editTeacherInfo(key!!)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    fun getLanguage(): Maybe<List<Language>> {
        val key = DES.encryptDES("server=/app/userFight/getLanguagesList?")
        return appComponent.appApi
                .getLanguage(key)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    fun getOpenedCity(): Maybe<List<City>> {
        val key = DES.encryptDES("server=/app/userFight/getOpenCity?")
        return appComponent.appApi
                .getOpenCity(key)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }

    fun getNotOpenCity(): Maybe<List<City>> {
        val key = DES.encryptDES("server=/app/userFight/getNotOpenCity?")
        return appComponent.appApi
                .getNotOpenCity(key)
                .toNetWork()
                .toMaybe()
                .map { it.check() }
                .logErrorAndForget(Throwable::toast)
    }


}
