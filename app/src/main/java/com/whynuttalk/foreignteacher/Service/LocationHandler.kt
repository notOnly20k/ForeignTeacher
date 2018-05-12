package com.whynuttalk.foreignteacher.Service

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.ServiceSettings
import com.amap.api.services.geocoder.*
import com.umeng.facebook.FacebookSdk.getApplicationContext
import com.umeng.socialize.utils.DeviceConfig.context
import com.whynuttalk.foreignteacher.ext.e
import io.reactivex.subjects.BehaviorSubject
import org.slf4j.LoggerFactory


/**
 * Created by cz on 4/23/18.
 */
class LocationHandler {
    //声明AMapLocationClient类对象
    val logger=LoggerFactory.getLogger("LocationHandler")
    var mLocationClient: AMapLocationClient
    var locationSubject: BehaviorSubject<AMapLocation> = BehaviorSubject.create()
    var geocodeResultSubject: BehaviorSubject<GeocodeAddress> = BehaviorSubject.create()

    init {
        ServiceSettings.getInstance().language = ServiceSettings.ENGLISH//默认是中文ServiceSettings.CHINESE
        //声明定位回调监听器
        val mLocationListener = AMapLocationListener { aMapLocation ->
            locationSubject.onNext(aMapLocation)
        }
        //初始化定位
        mLocationClient = AMapLocationClient(getApplicationContext())
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener)

        //声明AMapLocationClientOption对象
        var mLocationOption: AMapLocationClientOption? = null
        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mLocationOption.isOnceLocation = true
        mLocationOption.geoLanguage = AMapLocationClientOption.GeoLanguage.EN
        mLocationOption.isNeedAddress = true
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //启动定位

    }


    fun getLatlon(cityName: String) {

        val geocodeSearch = GeocodeSearch(context)
        geocodeSearch.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
            override fun onRegeocodeSearched(regeocodeResult: RegeocodeResult, i: Int) {
                logger.e { "搜索中 $regeocodeResult" }
            }

            override fun onGeocodeSearched(geocodeResult: GeocodeResult?, i: Int) {

                if (i == 1000) {
                    if (geocodeResult != null && geocodeResult.geocodeAddressList != null &&
                            geocodeResult.geocodeAddressList.size > 0) {
                        val geocodeAddress = geocodeResult.geocodeAddressList[0]
                        val latitude = geocodeAddress.latLonPoint.latitude//纬度
                        val longititude = geocodeAddress.latLonPoint.longitude//经度
                        val adcode = geocodeAddress.adcode//区域编码
                        geocodeResultSubject.onNext(geocodeAddress)
                    } else {
                        logger.e { "获取失败" }
                    }
                }else{
                    logger.e { "获取失败  $i" }
                }
            }
        })

        val geocodeQuery = GeocodeQuery(cityName.trim { it <= ' ' }, null)
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery)


    }

    fun searchPoi(address:String){

    }

    fun start() {

        mLocationClient.startLocation()
    }
}
