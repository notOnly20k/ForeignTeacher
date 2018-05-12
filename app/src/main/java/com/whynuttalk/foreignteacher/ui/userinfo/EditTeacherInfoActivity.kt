package com.whynuttalk.foreignteacher.ui.userinfo

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.City
import com.whynuttalk.foreignteacher.api.dto.Language
import com.whynuttalk.foreignteacher.api.dto.Nationality
import com.whynuttalk.foreignteacher.api.dto.User
import com.whynuttalk.foreignteacher.data.AlbumImgUrl
import com.whynuttalk.foreignteacher.ext.*
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.dialog.CustomDialog
import com.whynuttalk.foreignteacher.ui.dialog.MyActionDialog
import com.whynuttalk.foreignteacher.ui.dialog.SelectDateDialog
import com.whynuttalk.foreignteacher.ui.dialog.StarrBarDialog
import com.whynuttalk.foreignteacher.ui.login.LoginActivity
import com.whynuttalk.foreignteacher.ui.userinfo.SelectLanguageActivity.Companion.SELECT_CITY
import com.whynuttalk.foreignteacher.ui.userinfo.SelectLanguageActivity.Companion.SELECT_LANGUAGE
import com.whynuttalk.foreignteacher.ui.userinfo.SelectLanguageActivity.Companion.SELECT_NATIONALITY
import com.whynuttalk.foreignteacher.ui.userinfo.adapter.LanguageAdapter
import com.whynuttalk.foreignteacher.ui.userinfo.adapter.StudentPageAdapter
import com.whynuttalk.foreignteacher.util.DraggablePresenterImpl
import com.whynuttalk.foreignteacher.views.StarBarView
import kotlinx.android.synthetic.main.activity_user_info.*

/**
 * Created by cz on 3/29/18.
 */
class EditTeacherInfoActivity : BaseTranslateStatusActivity() {
    override val changeTitleBar: Boolean
        get() = false

    private lateinit var draggablePresenter: DraggablePresenterImpl


    override val contentViewResId: Int
        get() = R.layout.activity_user_info

    private lateinit var languageAdapter: LanguageAdapter

    private var languageList = mutableListOf<Language>()
    private lateinit var adapter: StudentPageAdapter

    private var sex: Int? = null
    private var cityId: Int? = null
    private var albumList = mutableListOf<AlbumImgUrl>()
    private var dateDialog: SelectDateDialog? = null


    override fun initView() {
        try {
            val user: User = appComponent.userHandler.getUser()!!
            et_contact_number.setText(user.phone!!.formateToTel())
            et_name.setText(user.nickName)
            tv_birth_edit.text = user.birthDay
            if (user.sex == 1) {
                tv_gender_edit.text = "Male"
            } else {
                tv_gender_edit.text = "Female"
            }
        } catch (e: Exception) {
            showToast(e.message)
        }

        drag_square.visibility = View.VISIBLE
        viewpager.visibility = View.GONE
        ll_indicator.visibility = View.GONE
        draggablePresenter = DraggablePresenterImpl(this, drag_square)
        draggablePresenter.setCustomActionDialog(MyActionDialog(this))

        tv_gender_edit.setOnClickListener({
            CustomDialog.Builder()
                    .create()
                    .setButton1Text("Male")
                    .setButton2Text("Female")
                    .setDialogListene(object : CustomDialog.CustomDialogListener {
                        override fun clickButton1(customDialog: CustomDialog) {
                            tv_gender_edit.text = "Male"
                            sex = 1
                            customDialog.dismiss()
                        }

                        override fun clickButton2(customDialog: CustomDialog) {
                            tv_gender_edit.text = "Female"
                            sex = 2
                            customDialog.dismiss()
                        }


                    })
                    .showtitle(false)
                    .show(supportFragmentManager, "sex_dialog")
        })

        tv_current_city_edit.setOnClickListener({
            activityUtil.go(SelectLanguageActivity::class.java).put("type", SelectLanguageActivity.CITY).startForResult(SELECT_CITY)
        })

        tv_nationality_edit.setOnClickListener {
            activityUtil.go(SelectLanguageActivity::class.java).put("type", SelectLanguageActivity.NATIONALITY).startForResult(SELECT_NATIONALITY)
        }

        tv_birth_edit.setOnClickListener {
            showDateDialog()
        }


        tv_language.setOnClickListener {
            activityUtil.go(SelectLanguageActivity::class.java).put("type", SelectLanguageActivity.LANGUAGE).startForResult(SELECT_LANGUAGE)
        }
        languageAdapter = LanguageAdapter(this, emptyList())
        rec_languages.adapter = languageAdapter
        languageAdapter.setOnItemClickListener(object : LanguageAdapter.OnItemClickListener {
            override fun onDeleteClick(language: Language) {
                languageList.remove(language)
                languageAdapter.updateList(languageList)
                if (languageList.isNotEmpty()) {
                    tv_language.visibility = View.GONE
                } else {
                    tv_language.visibility = View.VISIBLE
                }
            }

            override fun onAddItemClick() {
                activityUtil.go(SelectLanguageActivity::class.java).put("type", SelectLanguageActivity.LANGUAGE).startForResult(SELECT_LANGUAGE)
            }

        })
        rec_languages.layoutManager = LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL }

        tv_chinese_level.setOnClickListener {
            StarrBarDialog().apply {
                setRating(this@EditTeacherInfoActivity.findViewById<StarBarView>(R.id.star_chinese_level).getSartRating())
                setDialogListene(object : StarrBarDialog.SelectStarListener {
                    override fun onOkClick(level: Float) {
                        this@EditTeacherInfoActivity.findViewById<StarBarView>(R.id.star_chinese_level).setStarRating(level)
                    }

                })
            }.show(supportFragmentManager, "star_dialog")
        }

        tv_title_right.setOnClickListener {
            if (commitCheck())
                saveTeacher()
        }

    }


    private fun saveTeacher() {
        if (commitCheck()) {
            val telNum = et_contact_number.text.toString().formateToNum()
            sex = if (tv_gender_edit.text.toString() == "Male") {
                1
            } else {
                2
            }
            var sort = 1
            for (i in 0 until draggablePresenter.imageUrls.size()) {
                if (draggablePresenter.imageUrls[i] != null) {
                    albumList.add(AlbumImgUrl(draggablePresenter.imageUrls[i], sort))
                    sort++
                }
            }

            val albumImgUrl = Gson().toJson(albumList)
            var languagesIds = ""
            for (i in languageList.indices) {
                languagesIds += if (i == languageList.size - 1) {
                    "${languageList[i].id}"
                } else {
                    "${languageList[i].id},"
                }

            }
            appComponent.netWork.editTeacher(appComponent.userHandler.getUser()!!.id, et_name.text.toString(), albumList[0].imgUrl,
                    sex!!, tv_birth_edit.text.toString(), telNum, star_chinese_level.getSartRating().toInt(), albumImgUrl = albumImgUrl,
                    personalProfile = er_introduction.text.toString(), openCityId = cityId, languagesId = languagesIds,
                    nationality = tv_nationality_edit.text.toString())
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .doOnLoading { isShowDialog(it) }
                    .subscribe {
                        activityUtil.go(LoginActivity::class.java).start()
                    }
        }
    }


    override fun initData() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        draggablePresenter.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            SELECT_LANGUAGE -> {
                tv_language.visibility = View.GONE
                val language = data!!.getSerializableExtra(SelectLanguageActivity.LANGUAGE) as Language
                var boolean = true
                languageList.map {
                    if (it.id == language.id) {
                        boolean = false
                    }
                }
                if (boolean) {
                    languageList.add(language)
                    languageAdapter.updateList(languageList)
                }
                if (languageList.isNotEmpty()) {
                    tv_language.visibility = View.GONE
                } else {
                    tv_language.visibility = View.VISIBLE
                }
            }
            SELECT_CITY -> {
                val city = data!!.getSerializableExtra(SelectLanguageActivity.CITY) as City
                tv_current_city_edit.text = city.name
                cityId = city.id
            }
            SELECT_NATIONALITY -> {
                val nation = data!!.getSerializableExtra(SelectLanguageActivity.NATIONALITY) as Nationality
                tv_nationality_edit.text = nation.name
            }
        }
    }

    override fun commitCheck(): Boolean {
        if (!et_contact_number.text.toString().formateToNum().isPhoneNumberValid()) {
            showToast("The phone is not in the correct format")
            return false
        }
        if (tv_current_city_edit.text.isEmpty()){
            showToast("Must select city")
            return false
        }
        if (draggablePresenter.imageUrls == null || draggablePresenter.imageUrls.size() == 0 || draggablePresenter.imageUrls[0] == null) {
            showToast("Select at least one picture ")
            return false
        }
        if (tv_gender_edit.text.isEmpty() || tv_birth_edit.text.isEmpty() || et_name.text.isEmpty()) {
            showToast("Submit incomplete information ")
            return false
        }
        return super.commitCheck()
    }

    private fun showDateDialog() {
        val year = if (tv_birth_edit.text.isNotEmpty()) {
            tv_birth_edit.text.toString().split("-")[0].toInt()
        } else {
            1995
        }
        val monthe = if (tv_birth_edit.text.isNotEmpty()) {
            tv_birth_edit.text.toString().split("-")[1].toInt()
        } else {
            1
        }
        val day = if (tv_birth_edit.text.isNotEmpty()) {
            tv_birth_edit.text.toString().split("-")[2].toInt()
        } else {
            1
        }
        if (dateDialog == null) {
            dateDialog = SelectDateDialog.Builder()
                    .create()
                    .setListener(object : SelectDateDialog.SeletDateDialogListener {
                        override fun clickCancel() {

                        }

                        override fun clickSure(string: String) {
                            tv_birth_edit.text = string
                        }

                    })

        }
        dateDialog!!
                .setDate(year, monthe, day)
                .show(supportFragmentManager, "date")
    }

}
