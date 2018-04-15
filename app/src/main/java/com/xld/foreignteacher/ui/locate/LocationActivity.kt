package com.xld.foreignteacher.ui.locate

import android.annotation.SuppressLint
import android.os.Bundle
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.MyLocationStyle
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_locate.*


/**
 * Created by cz on 4/12/18.
 */
class LocationActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_locate
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapview.onCreate(savedInstanceState)
        mapview.map.apply {
            setMapLanguage(AMap.ENGLISH)
            uiSettings.isZoomControlsEnabled = false
            uiSettings.isMyLocationButtonEnabled =false
        }
        locateSelf()
    }


    fun locateSelf() {
        val myLocationStyle: MyLocationStyle = MyLocationStyle()
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        mapview.map.setMyLocationStyle(myLocationStyle)//设置定位蓝点的Style
        mapview.map.uiSettings.isMyLocationButtonEnabled = true//设置默认定位按钮是否显示，非必需设置。
        mapview.map.isMyLocationEnabled = true// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    override fun onDestroy() {
        super.onDestroy()
        mapview.onDestroy()
    }

    @SuppressLint("MissingSuperCall")
    override fun onResume() {
        super.onResume()
        mapview.onResume()
    }

    @SuppressLint("MissingSuperCall")
    override fun onPause() {
        super.onPause()
    }

    override fun initData() {

    }
}
