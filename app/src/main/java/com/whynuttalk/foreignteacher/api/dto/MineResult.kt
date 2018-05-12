package com.whynuttalk.foreignteacher.api.dto

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


class MyAuthentication : Serializable {

    var waiyu: Int = 0
    var waiyuArray: Any? = null
    var qianzheng: QianzhengBean? = null
    var hanyu: Int = 0
    var muyu: Any? = null
    var huzhao: HuzhaoBean? = null

    class QianzhengBean : Serializable {

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

    class HuzhaoBean : Serializable {

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


/**
 * Created by cz on 4/17/18.
 */

class TestClass {


    var score: Any? = null
    var lucreMonth: Int = 0
    var teachers: TeachersBean? = null
    var cancelOrderNum: Int = 0
    var totalOrderNum: Int = 0
    var ingOrderNum: Int = 0

    class TeachersBean {
        var id: Int = 0
        var createTime: Long = 0
        var phone: String? = null
        var passWord: Any? = null
        var inviteCode: Any? = null
        var imgUrl: String? = null
        var nickName: String? = null
        var sex: Int = 0
        var identCode: String? = null
        var contactInformation: String? = null
        var groupId: Any? = null
        var state: Int = 0
        var birthDay: String? = null
        var chineseLevel: Int = 0
        var nationality: String? = null
        var languagesId: Any? = null
        var openCityId: Any? = null
        var personalProfile: String? = null
        var personalProfiles: String? = null
        var isHot: Any? = null
        var hotSort: Any? = null
        var balance: Int = 0
        var lat: Any? = null
        var lon: Any? = null
        var openCity: Any? = null
        var grouping: Any? = null
        var score: Any? = null
        var lucreTotal: Any? = null
        var totalOrderNum: Any? = null
        var startTime: Any? = null
        var endTime: Any? = null
        var age: Int = 0
    }
}

class TeacherInfo {

    /**
     * score : null
     * lucreMonth : 0
     * teachers : {"id":3,"createTime":1525247199000,"phone":"18888888888","passWord":null,"inviteCode":null,"imgUrl":"http://foreignteachers.oss-cn-beijing.aliyuncs.com/c0b1b110-86bd-47d0-a0b3-ad3fbe77b74f","nickName":"外教教师","sex":1,"identCode":"8vsk6g","contactInformation":"18888888888","groupId":null,"state":1,"birthDay":"1995-01-01","chineseLevel":3,"nationality":null,"languagesId":null,"openCityId":null,"personalProfile":"","personalProfiles":null,"isHot":null,"hotSort":null,"balance":-12,"lat":null,"lon":null,"openCity":null,"grouping":null,"score":null,"lucreTotal":null,"totalOrderNum":null,"startTime":null,"endTime":null,"age":23}
     * cancelOrderNum : 0
     * totalOrderNum : 0
     * ingOrderNum : 0
     */

    var score: Score? = null
    var lucreMonth: Int = 0
    var teachers: TeachersBean? = null
    var cancelOrderNum: Int = 0
    var totalOrderNum: Int = 0
    var ingOrderNum: Int = 0

    class Score {
        var score: Double = 0.0
        var teacher_id = -1
    }

    class TeachersBean {
        /**
         * id : 3
         * createTime : 1525247199000
         * phone : 18888888888
         * passWord : null
         * inviteCode : null
         * imgUrl : http://foreignteachers.oss-cn-beijing.aliyuncs.com/c0b1b110-86bd-47d0-a0b3-ad3fbe77b74f
         * nickName : 外教教师
         * sex : 1
         * identCode : 8vsk6g
         * contactInformation : 18888888888
         * groupId : null
         * state : 1
         * birthDay : 1995-01-01
         * chineseLevel : 3
         * nationality : null
         * languagesId : null
         * openCityId : null
         * personalProfile :
         * personalProfiles : null
         * isHot : null
         * hotSort : null
         * balance : -12
         * lat : null
         * lon : null
         * openCity : null
         * grouping : null
         * score : null
         * lucreTotal : null
         * totalOrderNum : null
         * startTime : null
         * endTime : null
         * age : 23
         */

        var id: Int = 0
        var createTime: Long = 0
        var phone: String? = null
        var passWord: Any? = null
        var inviteCode: String? = null
        var imgUrl: String? = null
        var nickName: String? = null
        var sex: Int = 0
        var identCode: String? = null

        var contactInformation: String? = null
        var groupId: Any? = null
        var state: Int = 0
        var birthDay: String? = null
        var chineseLevel: Int = 0

        var nationality: String? = null
        var languagesId: String? = null
        var openCityId: Int? = null
        var personalProfile: String? = null
        var personalProfiles: String? = null
        var isHot: Any? = null
        var hotSort: Any? = null
        var balance: Int = 0
        var lat: Any? = null
        var lon: Any? = null
        var openCity: Any? = null
        var grouping: Any? = null
        var score: Any? = null
        var lucreTotal: Int = 0
        var totalOrderNum: Any? = null
        var startTime: Any? = null
        var endTime: Any? = null
        var age: Int = 0
    }
}

class AuthenticationDetail {

    var id: Int = 0
    var teacherId: Int = 0
    var type: Int = 0
    var certificateName: String? = null
    var credentialsNumber: Any? = null
    var credentialsImg: Any? = null
    var state: Int = 0
    var refuseReason: Any? = null
    var craeteTime: Long = 0
    var country: Any? = null
    var teacherName: Any? = null
    var stateArray: Any? = null
    var typeArray: Any? = null
    var startTime: Any? = null
    var endTime: Any? = null
    var nationality: Any? = null
}

class BlockUser {
    /**
     * id : 1
     * userId : 10
     * imgUrl : 测试
     * teacherId : 12
     * sex : 0
     */

    var id: Int = 0
    var userId: Int = 0
    var imgUrl: String? = null
    var teacherId: Int = 0
    var nickName: String?= null
    var sex: Int = 0
}


