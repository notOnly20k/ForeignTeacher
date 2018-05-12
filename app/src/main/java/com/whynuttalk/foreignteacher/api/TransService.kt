package com.whynuttalk.foreignteacher.api

import com.whynuttalk.foreignteacher.api.dto.TransResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by cz on 5/10/18.
 */
interface TransService {
    @GET("api/trans/vip/translate")
    fun getResult(
            @Query("q") key: String, @Query("from") from: String, @Query("to") to: String,
            @Query("appid") appid: String, @Query("salt") salt: String, @Query("sign") sign: String): Single<TransResult>
}
