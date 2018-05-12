package com.whynuttalk.foreignteacher.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ui.login.LoginActivity

/**
 * Created by cz on 5/10/18.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        object : Handler() {
            override fun handleMessage(msg: Message) {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }.sendEmptyMessageDelayed(0, 3000)
    }
}
