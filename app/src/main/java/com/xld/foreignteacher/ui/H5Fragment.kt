package com.xld.foreignteacher.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import cn.sinata.xldutils.fragment.BaseFragment
import com.xld.foreignteacher.R
import kotlinx.android.synthetic.main.fragment_h5.*
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/8/18.
 */
class H5Fragment : BaseFragment() {
    val logger =LoggerFactory.getLogger("H5Fragment")



    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_h5
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onFirstVisibleToUser() {
        val url=arguments!!.getString(URL)
        webView.isVerticalScrollBarEnabled = false
        webView.webChromeClient = WebChromeClient();
        webView.webViewClient = WebViewClient();
        webView.settings.javaScriptEnabled = true;
        webView.loadUrl(url)
    }

    override fun onVisibleToUser() {

    }

    override fun onInvisibleToUser() {

    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        super.onDestroy()
        if (webView!=null) run {
            webView.destroy()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO: inflate a fragment view
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        userVisibleHint = true
        return rootView
    }


    companion object {
        const val URL = "url"

        @JvmStatic
        fun createInstance(url: String): H5Fragment {
            return H5Fragment().apply {
                arguments = Bundle(1).apply {
                    putString(URL, url)
                }
            }
        }
    }

}
