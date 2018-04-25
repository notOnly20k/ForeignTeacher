package com.xld.foreignteacher

import android.util.Log
import cn.sinata.xldutils.BaseApplication
import com.umeng.commonsdk.UMConfigure
import com.umeng.facebook.FacebookSdk
import com.umeng.socialize.PlatformConfig
import com.xld.foreignteacher.Service.LocationHandler
import com.xld.foreignteacher.Service.OssHandler
import com.xld.foreignteacher.Service.UserHandler
import com.xld.foreignteacher.api.AppApi
import com.xld.foreignteacher.api.NetWork
import com.xld.foreignteacher.api.NetWork.Companion.BaseUrl
import com.xld.foreignteacher.views.ScheduleDateTextView
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
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
    override lateinit var locationHandler: LocationHandler
    override lateinit var scheduleSubject: BehaviorSubject<Pair<Int,ScheduleDateTextView>>
    override lateinit var userHandler: UserHandler
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

        FacebookSdk.sdkInitialize(this)
        ossHandler = OssHandler(applicationContext)
        userHandler=UserHandler()
        locationHandler = LocationHandler()
        scheduleSubject = BehaviorSubject.create()
        UMConfigure.init(this,"5ae031eeb27b0a1e5a000115","umeng",UMConfigure.DEVICE_TYPE_PHONE,"")
        PlatformConfig.setQQZone("1106795530","BilxVZ8Bq2yH3eqU")
        PlatformConfig.setWeixin("wxc2174142c64026de","cc3f6045c3315da59558d3b9019220b4")
        PlatformConfig.setSinaWeibo("1200352795","c0bf44af0e2387808d66a56aaba3216e","http://sns.whalecloud.com")
    }


    companion object {
        private val logger = LoggerFactory.getLogger("BaseApp")

        @JvmStatic lateinit var instance: BaseApp
            private set
    }
}
