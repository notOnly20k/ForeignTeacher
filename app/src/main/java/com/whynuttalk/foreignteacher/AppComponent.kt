package com.whynuttalk.foreignteacher

import com.whynuttalk.foreignteacher.Service.LocationHandler
import com.whynuttalk.foreignteacher.Service.UserHandler
import com.whynuttalk.foreignteacher.api.AppApi
import com.whynuttalk.foreignteacher.api.NetWork
import com.whynuttalk.foreignteacher.views.ScheduleDateTextView
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by cz on 3/27/18.
 */
interface  AppComponent{
    val appApi: AppApi
    val netWork:NetWork
    val userHandler:UserHandler
    val locationHandler:LocationHandler
    val scheduleSubject:BehaviorSubject<Pair<Int,ScheduleDateTextView>>
}