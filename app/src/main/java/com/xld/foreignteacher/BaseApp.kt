package com.xld.foreignteacher

import android.util.Log
import cn.sinata.xldutils.BaseApplication
import com.umeng.commonsdk.UMConfigure
import com.xld.foreignteacher.Service.OssHandler
import com.xld.foreignteacher.api.AppApi
import com.xld.foreignteacher.api.NetWork
import com.xld.foreignteacher.api.NetWork.Companion.BaseUrl
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by cz on 3/27/18.
 */
class BaseApp : BaseApplication(), AppComponent {
    override lateinit var ossHandler: OssHandler
    override lateinit var appApi: AppApi
    override lateinit var netWork: NetWork


    override fun setSharedPreferencesName(): String {
        return "pref"
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.e("AppApi", message) })
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(16000, TimeUnit.MILLISECONDS)
                .connectTimeout(16000, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build()

        appApi = Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BaseUrl)
                .build()
                .create(AppApi::class.java)
        netWork = NetWork(this, appApi)


        ossHandler = OssHandler(applicationContext)
        UMConfigure.init(this,"5acec5e6b27b0a7ab900001d","umeng",UMConfigure.DEVICE_TYPE_PHONE,"")
    }


    companion object {
        private val logger = LoggerFactory.getLogger("BaseApp")

        @JvmStatic lateinit var instance: BaseApp
            private set
    }
}
