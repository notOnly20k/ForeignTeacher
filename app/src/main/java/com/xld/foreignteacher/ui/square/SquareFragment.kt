package com.xld.foreignteacher.ui.square

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.ActivityUtil
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.ui.square.SquareCityActivity.Companion.CURRENT_CITY
import com.xld.foreignteacher.ui.square.SquareCityActivity.Companion.SELECTCITY
import com.xld.foreignteacher.ui.square.adapter.SquareAdapter
import kotlinx.android.synthetic.main.fragment_square.*
import org.slf4j.LoggerFactory

/**
 * Created by cz on 3/29/18.
 */
class SquareFragment : BaseFragment() {
    val logger= LoggerFactory.getLogger("SquareFragment")

    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_square
    }

    private lateinit var adapter: SquareAdapter
    private lateinit var activityUtil:ActivityUtil

    override fun onFirstVisibleToUser() {
        activityUtil = ActivityUtil.create(activity)
        adapter = SquareAdapter(activity!!, fragmentManager!!)
        rec_square.setAdapter(adapter)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rec_square.setLayoutManager(layoutManager)

        tv_choose_city.setOnClickListener {
           activityUtil.go(SquareCityActivity::class.java).put(CURRENT_CITY, "北京市市辖区").startForResult(SELECTCITY)
        }

        tv_edit.setOnClickListener {
            activityUtil.go(MomentActivity::class.java).start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        logger.e { data!!.getStringExtra("city") }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO: inflate a fragment view
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        userVisibleHint = true
        return rootView
    }

    override fun onVisibleToUser() {
    }

    override fun onInvisibleToUser() {
    }

}
