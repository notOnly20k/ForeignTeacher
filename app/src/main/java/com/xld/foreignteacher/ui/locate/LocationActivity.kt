package com.xld.foreignteacher.ui.locate

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.LocationSource
import com.amap.api.maps2d.model.*
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_locate.*


/**
 * Created by cz on 4/12/18.
 */
class LocationActivity : BaseTranslateStatusActivity(), LocationSource, PoiSearch.OnPoiSearchListener, AMapLocationListener,
        GeocodeSearch.OnGeocodeSearchListener {


    override val contentViewResId: Int
        get() = R.layout.activity_locate
    override val changeTitleBar: Boolean
        get() = false
    private lateinit var aMap: AMap
    private lateinit var inputQuery: InputtipsQuery
    private var autoTips = mutableListOf<Tip>()
    private var isfirstinput = true
    private var firstItem: PoiItem? = null
    private var locationMarker: Marker? = null
    private var searchLatlonPoint: LatLonPoint? = null
    private var resultData = mutableListOf<PoiItem>()
    private var isInputKeySearch: Boolean = false
    private var inputSearchKey: String? = null
    private lateinit var adapter: LocationAdapter
    private var isItemClickAction: Boolean = false
    private var geocoderSearch: GeocodeSearch? = null

    private var currentPage = 0// 当前页面，从0开始计数
    private var query: PoiSearch.Query? = null// Poi查询条件类
    private var poiSearch: PoiSearch? = null
    private var poiItems: List<PoiItem>? = null// poi数据
    private val searchKey = ""

    private var mListener: LocationSource.OnLocationChangedListener? = null
    private var mlocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null


    override fun initView() {
        tv_back.setOnClickListener { finish() }
        tv_search.setOnClickListener {
            ll_search_bar.visibility = View.VISIBLE
            rl_title.visibility = View.GONE
        }
        tv_cancel.setOnClickListener {
            rl_title.visibility = View.VISIBLE
            ll_search_bar.visibility = View.GONE
        }

        adapter = LocationAdapter(this)
        adapter.setLocationClickedListener(object : LocationAdapter.LocationClickedListener {
            override fun onLocationClickedListener(poiItem: PoiItem) {
                setResult(Activity.RESULT_OK, intent.putExtra("address", poiItem))
                finish()
            }

        })
        rec_address.layoutManager = LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL }
        rec_address.adapter = adapter
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newText = s.toString().trim { it <= ' ' }
                if (newText.isNotEmpty()) {
                    val inputquery = InputtipsQuery(newText, "")
                    val inputTips = Inputtips(this@LocationActivity, inputquery)
                    inputquery.cityLimit = true
                    inputTips.setInputtipsListener(inputtipsListener)
                    inputTips.requestInputtipsAsyn()
                }
            }

        })

        et_search.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Log.i("MY", "setOnItemClickListener")
            if (autoTips != null && autoTips.size > position) {
                val tip = autoTips[position]
                searchPoi(tip)
            }
        }
    }


    private var inputtipsListener: Inputtips.InputtipsListener = Inputtips.InputtipsListener { list, rCode ->
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            autoTips = list
            val listString = list.indices.map { list[it].name }
            val aAdapter = ArrayAdapter(
                    applicationContext,
                    R.layout.route_inputs, listString)
            et_search.setAdapter(aAdapter)
            aAdapter.notifyDataSetChanged()
            if (isfirstinput) {
                isfirstinput = false
                et_search.showDropDown()
            }
        } else {
            logger.e { "erroCode " + rCode }
            Toast.makeText(this@LocationActivity, "locate error Code " + rCode, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapview.onCreate(savedInstanceState)
        mapview.map.apply {
            setMapLanguage(AMap.ENGLISH)
            uiSettings.isZoomControlsEnabled = false
            uiSettings.isMyLocationButtonEnabled = false
        }
        aMap = mapview.map
        setUpMap()
        geocoderSearch = GeocodeSearch(this)
        geocoderSearch!!.setOnGeocodeSearchListener(this)

        aMap.isMyLocationEnabled = true
        aMap.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(cameraPosition: CameraPosition) {

            }

            override fun onCameraChangeFinish(cameraPosition: CameraPosition) {
                if (!isItemClickAction && !isInputKeySearch) {
                    geoAddress()
                }
                searchLatlonPoint = LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude)
                isInputKeySearch = false
                isItemClickAction = false
            }
        })

        aMap.setOnMapLoadedListener { addMarkerInScreenCenter(null) }
        locateSelf()
    }


    private fun addMarkerInScreenCenter(locationLatLng: LatLng?) {
        val latLng = aMap.cameraPosition.target
        val screenPosition = aMap.projection.toScreenLocation(latLng)
        locationMarker = aMap.addMarker(MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)))
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker?.setPositionByPixels(screenPosition.x, screenPosition.y)
        locationMarker?.zIndex = 1F

    }


    /**
     * 响应逆地理编码
     */
    fun geoAddress() {
        //        Log.i("MY", "geoAddress"+ searchLatlonPoint.toString());
        showProgress(true)
        et_search.setText("")
        if (searchLatlonPoint != null) {
            val query = RegeocodeQuery(searchLatlonPoint, 200f, GeocodeSearch.AMAP)// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            geocoderSearch!!.getFromLocationAsyn(query)
        } else {
            showProgress(false)
        }
    }


    /**
     * 设置一些amap的属性
     */
    private fun setUpMap() {
        aMap.uiSettings.isZoomControlsEnabled = false
        aMap.setLocationSource(this)// 设置定位监听
        aMap.uiSettings.isMyLocationButtonEnabled = true// 设置默认定位按钮是否显示
        aMap.isMyLocationEnabled = true// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }


    private fun searchPoi(result: Tip) {
        isInputKeySearch = true
        inputSearchKey = result.name//getAddress(); // + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
        searchLatlonPoint = result.point
        firstItem = PoiItem("tip", searchLatlonPoint, inputSearchKey, result.address)
        firstItem?.cityName = result.district
        firstItem?.adName = ""
        resultData.clear()

        if (searchLatlonPoint != null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(searchLatlonPoint!!.latitude, searchLatlonPoint!!.longitude), 16f))

            hideSoftKey(et_search)
            doSearchQuery()
        }
    }

    private fun hideSoftKey(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 开始进行poi搜索
     */
    /**
     * 开始进行poi搜索
     */
    fun doSearchQuery() {
        //        Log.i("MY", "doSearchQuery");
        currentPage = 0
        query = PoiSearch.Query(searchKey, null, "")// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query?.cityLimit = true
        query?.pageSize = 20
        query?.pageNum = currentPage

        if (searchLatlonPoint != null) {
            poiSearch = PoiSearch(this, query)
            poiSearch?.setOnPoiSearchListener(this)
            poiSearch?.bound = PoiSearch.SearchBound(searchLatlonPoint, 1000, true)//
            poiSearch?.searchPOIAsyn()
        }
    }


    private fun locateSelf() {
        appComponent.locationHandler.start()
        appComponent.locationHandler.locationSubject
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { showProgress(it) }
                .subscribe {
                    searchLatlonPoint = LatLonPoint(it.latitude, it.longitude)
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(searchLatlonPoint!!.latitude, searchLatlonPoint!!.longitude), 16f))
                }
//        val myLocationStyle: MyLocationStyle = MyLocationStyle()
//        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
//        mapview.map.setMyLocationStyle(myLocationStyle)//设置定位蓝点的Style
//        mapview.map.uiSettings.isMyLocationButtonEnabled = true//设置默认定位按钮是否显示，非必需设置。
//        mapview.map.isMyLocationEnabled = true// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    override fun deactivate() {
        mListener = null
        if (mlocationClient != null) {
            mlocationClient!!.stopLocation()
            mlocationClient!!.onDestroy()
        }
        mlocationClient = null
    }

    override fun activate(listener: LocationSource.OnLocationChangedListener?) {
        mListener = listener
        if (mlocationClient == null) {
            mlocationClient = AMapLocationClient(this)
            mLocationOption = AMapLocationClientOption()
            //设置定位监听
            mlocationClient!!.setLocationListener(this)
            //设置为高精度定位模式
            mLocationOption!!.isOnceLocation = true
            mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            //设置定位参数
            mlocationClient!!.setLocationOption(mLocationOption)
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient!!.startLocation()
        }
    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
    }

    override fun onPoiSearched(poiResult: PoiResult?, resultCode: Int) {
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult?.query != null) {
                if (poiResult.query == query) {
                    poiItems = poiResult.pois
                    if (poiItems != null && poiItems!!.isNotEmpty()) {
                        adapter.upDataList(poiItems!!)
                    } else {
                        showToast(R.string.no_search_results.toFormattedString(this))
                    }
                }
            } else {
                showToast(R.string.no_search_results.toFormattedString(this))
            }
        }
    }

    override fun onLocationChanged(p0: AMapLocation?) {
    }

    override fun onRegeocodeSearched(result: RegeocodeResult?, rCode: Int) {
        showProgress(false)
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result?.regeocodeAddress != null
                    && result.regeocodeAddress.formatAddress != null) {
                val address = result.regeocodeAddress.province + result.regeocodeAddress.city + result.regeocodeAddress.district + result.regeocodeAddress.township
                firstItem = PoiItem("regeo", searchLatlonPoint, address, address)
                doSearchQuery()
            }
        } else {
            logger.e { "error code is " + rCode }
            Toast.makeText(this@LocationActivity, "search error Code " + rCode, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mapview.onDestroy()
        if (null != mlocationClient) {
            mlocationClient!!.onDestroy()
        }
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

    companion object {
        const val ADDRESS = 101
    }
}
