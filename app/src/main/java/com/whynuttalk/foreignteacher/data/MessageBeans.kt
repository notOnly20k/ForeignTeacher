package com.whynuttalk.foreignteacher.data

/**
 * Created by cz on 5/7/18.
 */
class OrderMsg {
    /**
     * id : 1
     * title : 测试
     * content : 测试测试测试测试
     * addtime : 1523159514000
     * userId : 1
     * type : 1
     */

    var id: Int = 0
    var title: String? = null
    var content: String? = null
    var addtime: Long = 0
    var userId: Int = 0
    var type: Int = 0
}


class SystemMsg {
    /**
     * id : 1
     * title : 测试
     * img : http://img5.imgtn.bdimg.com/it/u=950004826,3164354653&fm=27&gp=0.jpg
     * url :
     * addtime : 1523159484000
     */

    var id: Int = 0
    var title: String? = null
        get() = if (field == null) "" else field
    var img: String? = null
        get() = if (field == null) "" else field
    var url: String? = null
        get() = if (field == null) "" else field
    var addtime: Long = 0
}

