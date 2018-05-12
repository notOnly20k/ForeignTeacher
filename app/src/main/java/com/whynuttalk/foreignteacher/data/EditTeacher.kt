package com.whynuttalk.foreignteacher.data

/**
 * Created by cz on 4/11/18.
 */
class EditTeacher( var id: Int,val nickName: String,val sex: String,val birthDay: String, val contactInformation: String,
                   var chineseLevel: Int? = null,var nationality: String? = null,var languagesId: Int? = null,
                   var openCityId: Int? = null,var personalProfile: String? = null,var albumImgUrl: String? = null) {
    fun getId(){}
}

data class AlbumImgUrl(var imgUrl: String,var sort:Int)
