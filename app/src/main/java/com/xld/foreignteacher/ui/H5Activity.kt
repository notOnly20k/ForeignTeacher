package com.xld.foreignteacher.ui

import android.os.Bundle
import cn.sinata.xldutils.activitys.WebViewActivity
import com.xld.foreignteacher.R

/**
 * Created by cz on 3/29/18.
 */
class H5Activity : WebViewActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleBackground(R.color.black_00)
        titleView.setBackgroundResource(R.color.black_00)
        titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        val backDra=resources.getDrawable(R.mipmap.back_yellow)
        backDra.setBounds( 0, 0, backDra.minimumWidth,backDra.minimumHeight)
        backView.setCompoundDrawables(backDra,null,null,null)
        titleColor = R.color.yellow_ffcc00
        titlelayout.setBackgroundResource(R.color.black_00)
//        val title = intent.getStringExtra("title")
//        setTitle(title)
    }
}