package com.whynuttalk.foreignteacher.api

import cn.sinata.xldutils.utils.Md5
import com.whynuttalk.foreignteacher.api.dto.TransResult
import com.whynuttalk.foreignteacher.ext.logErrorAndForget
import com.whynuttalk.foreignteacher.ext.toNetWork
import com.whynuttalk.foreignteacher.ext.toast
import io.reactivex.Maybe
import java.util.*

/**
 * Created by cz on 5/10/18.
 */
class TransApi {
    private val appid = "20180507000154555"
    private val securityKey = "o_U6rUsj6N7FAkaDFMU_"

    fun getTransResult(query: String, from: String, to: String): Maybe<TransResult> {
        val params = buildParams(query, from, to)
        return TransApiService.instance.creatTransService()
                .getResult(params["q"]!!, params["from"]!!,
                        params["to"]!!, params["appid"]!!,
                        params["salt"]!!, params["sign"]!!)
                .toMaybe()
                .toNetWork()
                .logErrorAndForget(Throwable::toast)
    }

    private fun buildParams(query: String, from: String, to: String): Map<String, String> {
        val params = HashMap<String, String>()
        params.put("q", query)
        params.put("from", from)
        params.put("to", to)

        params.put("appid", appid)

        // 随机数
        val salt = System.currentTimeMillis().toString()
        params.put("salt", salt)

        // 签名
        val src = appid + query + salt + securityKey // 加密前的原文
        params.put("sign", Md5.getMd5Value(src))

        return params
    }

}
