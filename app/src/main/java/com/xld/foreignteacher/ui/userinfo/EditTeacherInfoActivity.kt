package com.xld.foreignteacher.ui.userinfo

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.City
import com.xld.foreignteacher.api.dto.Language
import com.xld.foreignteacher.api.dto.User
import com.xld.foreignteacher.data.AlbumImgUrl
import com.xld.foreignteacher.ext.*
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.dialog.CustomDialog
import com.xld.foreignteacher.ui.dialog.MyActionDialog
import com.xld.foreignteacher.ui.dialog.SelectDateDialog
import com.xld.foreignteacher.ui.dialog.StarrBarDialog
import com.xld.foreignteacher.ui.main.MainActivity
import com.xld.foreignteacher.ui.userinfo.SelectLanguageActivity.Companion.SELECT_CITY
import com.xld.foreignteacher.ui.userinfo.SelectLanguageActivity.Companion.SELECT_LANGUAGE
import com.xld.foreignteacher.ui.userinfo.adapter.LanguageAdapter
import com.xld.foreignteacher.ui.userinfo.adapter.StudentPageAdapter
import com.xld.foreignteacher.util.DraggablePresenterImpl
import com.xld.foreignteacher.views.StarBarView
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

        tv_birth_edit.setOnClickListener {
            showDateDialog()
        }


        tv_language.setOnClickListener {
            activityUtil.go(SelectLanguageActivity::class.java).put("type", SelectLanguageActivity.LANGUAGE).startForResult(SELECT_LANGUAGE)
        }
        languageAdapter = LanguageAdapter(this, emptyList())
        rec_languages.adapter = languageAdapter
        languageAdapter.setOnItemClickListener(object : LanguageAdapter.OnItemClickListener {
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
        albumList.map {
            logger.e { it }
        }

        val albumImgUrl = albumList.toString()
        appComponent.netWork.editTeacher(appComponent.userHandler.getUser()!!.id, et_name.text.toString(), albumList[0].imgUrl,
                sex!!, tv_birth_edit.text.toString(), telNum, star_chinese_level.getSartRating().toInt(),albumImgUrl = albumImgUrl,
                personalProfile = er_introduction.text.toString(), openCityId = cityId)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { showProgress(it) }
                .subscribe {
                    activityUtil.go(MainActivity::class.java).start()
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
                languageList.add(language)
                languageAdapter.updateList(languageList)
            }
            SELECT_CITY -> {
                val city = data!!.getSerializableExtra(SelectLanguageActivity.CITY) as City
                tv_current_city_edit.text = city.name
                cityId = city.id
            }
        }
    }

    override fun commitCheck(): Boolean {
        if (!et_contact_number.text.toString().formateToNum().isPhoneNumberValid()) {
            showToast("The phone is not in the correct format")
            return false
        }
        logger.e { draggablePresenter.imageUrls }
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
