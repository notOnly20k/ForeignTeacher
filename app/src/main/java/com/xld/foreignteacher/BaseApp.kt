package com.xld.foreignteacher

import android.util.Log
import android.widget.Toast
import cn.sinata.xldutils.BaseApplication
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.umeng.commonsdk.UMConfigure
import com.umeng.facebook.FacebookSdk
import com.umeng.socialize.PlatformConfig
import com.xld.foreignteacher.Service.LocationHandler
import com.xld.foreignteacher.Service.UserHandler
import com.xld.foreignteacher.api.AppApi
import com.xld.foreignteacher.api.NetWork
import com.xld.foreignteacher.api.NetWork.Companion.BaseUrl
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.im.Constant
import com.xld.foreignteacher.im.HxEaseuiHelper
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
    override lateinit var scheduleSubject: BehaviorSubject<Pair<Int, ScheduleDateTextView>>
    override lateinit var userHandler: UserHandler
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
        userHandler = UserHandler()
        locationHandler = LocationHandler()
        scheduleSubject = BehaviorSubject.create()
        UMConfigure.init(this, "5ae031eeb27b0a1e5a000115", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "")
        PlatformConfig.setQQZone("1106795530", "BilxVZ8Bq2yH3eqU")
        PlatformConfig.setWeixin("wxc2174142c64026de", "cc3f6045c3315da59558d3b9019220b4")
        PlatformConfig.setSinaWeibo("1200352795", "c0bf44af0e2387808d66a56aaba3216e", "http://www.whynuttalk.com")
        initHuanXin()
    }

    private fun initHuanXin() {
        HxEaseuiHelper.getInstance().init(this.applicationContext)
        //设置全局监听
        setGlobalListeners()
    }

    internal lateinit var connectionListener: EMConnectionListener
    /**
     * 设置一个全局的监听
     */
    private fun setGlobalListeners() {

        // create the global connection listener
        connectionListener = object : EMConnectionListener {
            override fun onDisconnected(error: Int) {
                Log.e("global listener", "onDisconnect" + error)
                when (error) {
                    EMError.NETWORK_ERROR->{
                        logger.e { "NETWORK ERROR" }
                    }
                    EMError.USER_REMOVED -> // 显示帐号已经被移除
                        onUserException(Constant.ACCOUNT_REMOVED)
                    EMError.USER_LOGIN_ANOTHER_DEVICE -> { // 显示帐号在其他设备登录
                        onUserException(Constant.ACCOUNT_CONFLICT)
                        EMClient.getInstance().logout(true)//退出登录
                        Toast.makeText(applicationContext, "您的账户在其他设备上登录", Toast.LENGTH_SHORT).show()
                        //                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    EMError.SERVER_SERVICE_RESTRICTED -> //
                        onUserException(Constant.ACCOUNT_FORBIDDEN)
                }
            }

            override fun onConnected() {
                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events

            }
        }

        //register connection listener
        try {
            if (connectionListener != null) {
                EMClient.getInstance().addConnectionListener(connectionListener)
            }
        } catch (e: Exception) {
            Log.e("hx", "setGlobalListeners: 用户全局监听错误")
        }

    }

    /**
     * user met some exception: conflict, removed or forbidden
     */
    protected fun onUserException(exception: String) {
        Log.e("msg", "onUserException: " + exception)
        Toast.makeText(applicationContext, exception, Toast.LENGTH_LONG).show()
        //        Intent intent = new Intent(getBaseContext(), UserQrCodeActivity.class);
        //        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        intent.putExtra(exception, true);
        //        this.startActivity(intent);
    }


    companion object {
        private val logger = LoggerFactory.getLogger("BaseApp")

        @JvmStatic lateinit var instance: BaseApp
            private set
    }
}
