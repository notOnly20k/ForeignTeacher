package com.xld.foreignteacher.api.dto

import java.io.Serializable

/**
 * Created by cz on 4/27/18.
 */

class TeacherBankAccout(var id: Int = 0, var role: Int = 0, var foreignId: Int = 0, var bankName: String? = null,
                        var name: String? = null, var account: String? = null, var updatedate: Long = 0, var updateby: Int = 0,
                        var createdate: Long = 0)


class WalletDetail {


    var weeklyAverage: Int = 0
    var lucreMonth: Int = 0
    var teachers: TeachersBean? = null
    var lucreTotal: Int = 0

    class TeachersBean {

        var id: Int = 0
        var createTime: Long = 0
        var phone: String? = null
        var passWord: String? = null
        var inviteCode: Any? = null
        var imgUrl: String? = null
        var nickName: String? = null
        var sex: Int = 0
        var identCode: Any? = null
        var contactInformation: String? = null
        var groupId: Int = 0
        var state: Int = 0
        var birthDay: String? = null
        var chineseLevel: Int = 0
        var nationality: String? = null
        var languagesId: String? = null
        var openCityId: Int = 0
        var personalProfile: String? = null
        var isHot: Int = 0
        var hotSort: Any? = null
        var balance: Any? = null
        var lat: Any? = null
        var lon: Any? = null
        var openCity: Any? = null
        var grouping: Any? = null
        var score: Any? = null
        var lucreTotal: Any? = null
        var totalOrderNum: Any? = null
        var startTime: Any? = null
        var endTime: Any? = null
    }
}


class WithDrawDetail(var id: Int = 0, var transactionNumber: String? = null, var role: Int = 0, var foreignId: Int = 0,
                     var phone: Any? = null, var withdrawMoney: Int = 0, var totalWithdrawMoney: Int = 0, var name: String? = null,
                     var bankaccount: String? = null, var state: Int = 0, var remark: String? = null, var createdate: Long = 0,
                     var updateby: Int = 0, var updatedate: Long = 0)



class MyAuthentication:Serializable {

    var waiyu: Int = 0
    var waiyuArray: Any? = null
    var qianzheng: QianzhengBean? = null
    var hanyu: Int = 0
    var muyu: Any? = null
    var huzhao: HuzhaoBean? = null

    class QianzhengBean:Serializable  {

        var id: Int = 0
        var teacherId: Int = 0
        var type: Int = 0
        var certificateName: Any? = null
        var credentialsNumber: String? = null
        var credentialsImg: String? = null
        var state: Int = 0
        var refuseReason: Any? = null
        var craeteTime: Long = 0
    }

    class HuzhaoBean :Serializable {

        var id: Int = 0
        var teacherId: Int = 0
        var type: Int = 0
        var certificateName: Any? = null
        var credentialsNumber: String? = null
        var credentialsImg: String? = null
        var state: Int = 0
        var refuseReason: Any? = null
        var craeteTime: Long = 0
    }
}
