package com.xld.foreignteacher

import com.xld.foreignteacher.api.AppApi
import com.xld.foreignteacher.api.NetWork

/**
 * Created by cz on 3/27/18.
 */
interface  AppComponent{
    val appApi: AppApi
    val netWork:NetWork
}