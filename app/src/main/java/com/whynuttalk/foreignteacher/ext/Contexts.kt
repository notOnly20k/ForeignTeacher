package com.whynuttalk.foreignteacher.ext

import android.content.Context
import android.support.v4.app.Fragment
import com.whynuttalk.foreignteacher.AppComponent

/**
 * Created by cz on 3/28/18.
 */

fun <T> Fragment.callbacks(): T? {
    @Suppress("UNCHECKED_CAST")
    return (parentFragment as? T) ?: (activity as? T)
}

val Context.appComponent: AppComponent
    get() = applicationContext as AppComponent

val Fragment.appComponent: AppComponent
    get() = activity!!.appComponent
