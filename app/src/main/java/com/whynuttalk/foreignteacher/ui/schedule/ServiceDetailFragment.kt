package com.whynuttalk.foreignteacher.ui.schedule

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import cn.sinata.xldutils.fragment.BaseFragment
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ui.schedule.adapter.ImgAdapter
import kotlinx.android.synthetic.main.fragment_service_detail.*

/**
 * Created by cz on 5/7/18.
 */
class ServiceDetailFragment: BaseFragment() {
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_service_detail
    }

    override fun onFirstVisibleToUser() {
        tv_content.text=arguments!!.getString("content")
        val list=arguments!!.getStringArrayList("pics")
        rec_img.layoutManager=LinearLayoutManager(context).apply { orientation=LinearLayout.VERTICAL }
        rec_img.adapter=ImgAdapter(list)
    }

    override fun onVisibleToUser() {
    }

    override fun onInvisibleToUser() {
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO: inflate a fragment view
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        userVisibleHint = true
        return rootView
    }

    companion object {

        @JvmStatic
        fun createInstance(content: String,list: ArrayList<String>): ServiceDetailFragment {
            return ServiceDetailFragment().apply {
                arguments = Bundle(2).apply {
                    putString("content",content )
                    putStringArrayList("pics",list)
                }
            }
        }
    }
}