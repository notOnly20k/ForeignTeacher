package com.xld.foreignteacher.data

import java.io.Serializable

/**
 * Created by cz on 4/8/18.
 */
data class DeclineReason(val reason: String, var isSelect: Boolean)

class PreviewGroupOrder(var title: String = "",
                        var price: String = "0", var time: String = "", var address: String = "", var deadline: String = "", var max: String = "0",
                        var min: String = "0", var introduction: String = "", var pics: List<String> = emptyList(), var backGround: String = "") : Serializable

class PreviewGroupOrderIns(var introduce: String = "", introduceImgs: List<String> = emptyList())

class BookInfo(var bookingTeacherId: Int, var cancelReason: String = "", var day: String, var discount: Int, var endTime: Double, var price: Int, var startTime: Double, var name: String, var state: Int)
