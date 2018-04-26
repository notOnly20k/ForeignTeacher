package com.xld.foreignteacher.Service

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.ServiceSettings
import com.umeng.facebook.FacebookSdk.getApplicationContext
import io.reactivex.subjects.BehaviorSubject


/**
 * Created by cz on 4/23/18.
 */
class LocationHandler {
    //声明AMapLocationClient类对象
    var mLocationClient: AMapLocationClient
    var locationSubject: BehaviorSubject<AMapLocation> = BehaviorSubject.create()

    init {
        ServiceSettings.getInstance().language = ServiceSettings.ENGLISH//默认是中文ServiceSettings.CHINESE
        //声明定位回调监听器
        val mLocationListener = AMapLocationListener { aMapLocation -> locationSubject.onNext(aMapLocation) }
        //初始化定位
        mLocationClient = AMapLocationClient(getApplicationContext())
        //设置定位回调监听
        mLocationClient!!.setLocationListener(mLocationListener)

        //声明AMapLocationClientOption对象
        var mLocationOption: AMapLocationClientOption? = null
        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mLocationOption.isOnceLocation = true
        mLocationOption.isNeedAddress = true
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //启动定位
    }

    fun start() {
        mLocationClient.startLocation()
    }
}
