package com.xld.foreignteacher.api.dto

/**
 * Created by cz on 4/27/18.
 */


data class PersonalTrainingOrder(var total: Int = 0, var records: Int = 0, var pageInfo: PageInfoBean? = null, var rows: List<RowsBean>? = null) {


    class PageInfoBean(var pageNum: Int = 0, var pageSize: Int = 0, var size: Int = 0, var startRow: Int = 0, var endRow: Int = 0,
                       var total: Int = 0, var pages: Int = 0, var firstPage: Int = 0, var prePage: Int = 0, var nextPage: Int = 0,
                       var lastPage: Int = 0, var isIsFirstPage: Boolean = false, var isIsLastPage: Boolean = false, var isHasPreviousPage: Boolean = false,
                       var isHasNextPage: Boolean = false, var navigatePages: Int = 0, var list: List<ListBean>? = null, var navigatepageNums: List<Int>? = null) {


        class ListBean(var sortField: Any? = null, var id: Int = 0, var orderNumber: String? = null, var userId: Int = 0, var teacherId: Int = 0,
                       var curriculumId: Int = 0, var bookingAutoWeeks: Int = 0, var bookingAutoIds: String? = null, var bookingInfo: String? = null,
                       var numberOfPeople: Int = 0, var bookingUserIds: String? = null, var totalPrice: Int = 0, var userCouponId: Any? = null,
                       var offset: Int = 0, var userService: Int = 0, var teacherService: Int = 0, var platformTotalService: Int = 0,
                       var payMoney: Int = 0, var teacherIncomeMoney: Int = 0, var state: Int = 0, var refuseReason: Any? = null, var teacherCancelReason: Any? = null,
                       var teacherCancelDescribe: Any? = null, var userCancelReason: Any? = null, var userCancelDescribe: Any? = null, var totalRefund: Any? = null, var address: String? = null,
                       var reminderCount: Any? = null, var paytime: Any? = null, var addtime: Long = 0, var stateArray: Any? = null, var userName: String? = null,
                       var phone: String? = null, var user: UserBean? = null, var curriculum: CurriculumBean? = null, var teachers: TeachersBean? = null) {


            class UserBean(var id: Int = 0, var createTime: Long = 0, var phone: String? = null, var passWord: String? = null,
                           var inviteCode: Any? = null, var imgUrl: Any? = null, var nickName: String? = null, var sex: Int = 0,
                           var birthDay: Any? = null, var balance: Int = 0, var state: Int = 0, var groupId: Any? = null,
                           var openId: Any? = null, var authType: Any? = null, var identCode: String? = null, var contactInformation: Any? = null,
                           var startTime: Any? = null, var endTime: Any? = null, var name: Any? = null, var money: Any? = null)

            class CurriculumBean(var id: Int = 0, var title: String? = null, var languagesId: Int = 0,
                                 var classificationId: Int = 0, var price: Int = 0, var status: Int = 0,
                                 var teachersId: Int = 0, var craeteTime: Long = 0, var name: Any? = null,
                                 var cName: Any? = null, var eName: Any? = null, var teacherName: Any? = null,
                                 var className: Any? = null, var orderNum: Any? = null, var pMoney: Any? = null,
                                 var peopleNum: Any? = null, var startTime: Any? = null, var endTime: Any? = null)

            class TeachersBean(var id: Int = 0, var createTime: Long = 0, var phone: String? = null, var passWord: String? = null,
                               var inviteCode: Any? = null, var imgUrl: String? = null, var nickName: String? = null, var sex: Int = 0,
                               var identCode: Any? = null, var contactInformation: Any? = null, var groupId: Any? = null, var state: Int = 0,
                               var birthDay: Any? = null, var chineseLevel: Any? = null, var nationality: String? = null, var languagesId: String? = null,
                               var openCityId: Int = 0, var personalProfile: Any? = null, var isHot: Int = 0, var hotSort: Any? = null,
                               var balance: Int = 0, var lat: Any? = null, var lon: Any? = null)
        }
    }

    class RowsBean(var sortField: Any? = null, var id: Int = 0, var orderNumber: String? = null, var userId: Int = 0, var teacherId: Int = 0,
                   var curriculumId: Int = 0, var bookingAutoWeeks: Int = 0, var bookingAutoIds: String? = null, var bookingInfo: String? = null,
                   var numberOfPeople: Int = 0, var bookingUserIds: String? = null, var totalPrice: Int = 0, var userCouponId: Any? = null,
                   var offset: Int = 0, var userService: Int = 0, var teacherService: Int = 0, var platformTotalService: Int = 0, var payMoney: Int = 0,
                   var teacherIncomeMoney: Int = 0, var state: Int = 0, var refuseReason: Any? = null, var teacherCancelReason: Any? = null,
                   var teacherCancelDescribe: Any? = null, var userCancelReason: Any? = null, var userCancelDescribe: Any? = null, var totalRefund: Any? = null,
                   var address: String? = null, var reminderCount: Any? = null, var paytime: Any? = null, var addtime: Long = 0, var stateArray: Any? = null, var userName: String? = null,
                   var phone: String? = null, var user: UserBeanX? = null, var curriculum: CurriculumBeanX? = null, var teachers: TeachersBeanX? = null) {


        class UserBeanX(var id: Int = 0, var createTime: Long = 0, var phone: String? = null, var passWord: String? = null, var inviteCode: Any? = null,
                        var imgUrl: String? = null, var nickName: String? = null, var sex: Int = 0, var birthDay: String? = null, var balance: Int = 0,
                        var state: Int = 0, var groupId: Any? = null, var openId: Any? = null, var authType: Any? = null, var identCode: String? = null,
                        var contactInformation: Any? = null, var startTime: Any? = null, var endTime: Any? = null, var name: Any? = null, var money: Any? = null)

        class CurriculumBeanX(var id: Int = 0, var title: String? = null, var languagesId: Int = 0, var classificationId: Int = 0,
                              var price: Int = 0, var status: Int = 0, var teachersId: Int = 0, var craeteTime: Long = 0, var name: Any? = null,
                              var cName: Any? = null, var eName: Any? = null, var teacherName: Any? = null, var className: String? = null, var orderNum: Any? = null,
                              var pMoney: Any? = null, var peopleNum: Any? = null, var startTime: Long? = null, var endTime: Long? = null)

        class TeachersBeanX(var id: Int = 0, var createTime: Long = 0, var phone: String? = null, var passWord: String? = null, var inviteCode: Any? = null,
                            var imgUrl: String? = null, var nickName: String? = null, var sex: Int = 0, var identCode: Any? = null, var contactInformation: Any? = null,
                            var groupId: Any? = null, var state: Int = 0, var birthDay: String? = null, var chineseLevel: Any? = null, var nationality: String? = null,
                            var languagesId: String? = null, var openCityId: Int = 0, var personalProfile: Any? = null, var isHot: Int = 0, var hotSort: Any? = null,
                            var balance: Int = 0, var lat: Any? = null, var lon: Any? = null)
    }
}

data class GroupOrder(var id: Int = 0, var title: String? = null, var price: Int = 0, var address: String? = null,
                      var enrolment: Int = 0, var number: Int = 0, var cName: String? = null, var endTime: Long = 0,
                      var reservationDeadline: Long = 0, var deadlineRegistration: Long = 0, var openingTime: Long = 0,
                      var backgroundCourse: String? = null, var collect: Int = 0, var eName: String? = null)

class GroupOrderDetail(var deadlineRegistration: Long = 0, var enrolment: Int = 0, var languages_id: String? = null, var endTime: Long = 0, var teachersImgUrl: String? = null,
                       var id: Int = 0, var title: String? = null, var sex: Int = 0, var teacherLanguagesEName: String? = null, var number1: Int = 0, var classesNumber: Int = 0,
                       var lat: String? = null, var lon: String? = null, var status: Int = 0, var cName: String? = null, var number: Int = 0, var teachersName: String? = null,
                       var teacherLanguagesCName: String? = null, var reservationDeadline: Long = 0, var km: Int = 0, var eName: String? = null, var price: Int = 0, var nationality: String? = null,
                       var score: Double = 0.toDouble(), var address: String? = null, var openingTime: Long = 0, var collect1: Int = 0, var teacherId: Int = 0, var backgroundCourse: String? = null,
                       var collect: Int = 0, var orderInfo: List<OrderInfoBean>? = null,var courseEtails:String="") {

    class OrderInfoBean(var peopleNum: Int = 0, var createTime: Long = 0, var userId: Int = 0, var imgUrl: String? = null, var nickName: String = "", var sex: Int = 0)
}


data class OrderNum(var num1: Int = 0, var num2: Int = 0, var num3: Int = 0, var num4: Int = 0, var num5: Int = 0)
