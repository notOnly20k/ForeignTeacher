package com.whynuttalk.foreignteacher.util

import java.lang.System.loadLibrary

/**
 * Created by cz on 3/28/18.
 */
class JNIUtils {
    init {
        //名字注意，需要跟你的build.gradle ndk节点下面的名字一样
        loadLibrary("jni_util")
    }


    external fun getKey(): String
}