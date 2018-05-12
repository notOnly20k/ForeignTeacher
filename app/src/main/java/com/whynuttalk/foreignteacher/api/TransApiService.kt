package com.whynuttalk.foreignteacher.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by cz on 5/10/18.
 */
class TransApiService//构造方法私有
private constructor() {
    private val retrofit: Retrofit

    init {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.e("ApiService", message) })
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(16000, TimeUnit.MILLISECONDS)
                .connectTimeout(16000, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build()

        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build()
    }

    //在访问ApiService时创建单例
    private object SingletonHolder {
         val INSTANCE = TransApiService()
    }

    fun creatTransService(): TransService {
        return retrofit.create<TransService>(TransService::class.java!!)
    }

    companion object {
        private val BASE_URL = "http://api.fanyi.baidu.com/"

        //获取单例
        val instance: TransApiService
            get() = SingletonHolder.INSTANCE
    }
}
