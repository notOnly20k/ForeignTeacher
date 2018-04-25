package com.xld.foreignteacher

import com.xld.foreignteacher.Service.LocationHandler
import com.xld.foreignteacher.Service.OssHandler
import com.xld.foreignteacher.Service.UserHandler
import com.xld.foreignteacher.api.AppApi
import com.xld.foreignteacher.api.NetWork
import com.xld.foreignteacher.views.ScheduleDateTextView
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by cz on 3/27/18.
 */
interface  AppComponent{
    val appApi: AppApi
    val netWork:NetWork
    val ossHandler:OssHandler
    val userHandler:UserHandler
    val locationHandler:LocationHandler
    val scheduleSubject:BehaviorSubject<Pair<Int,ScheduleDateTextView>>
}