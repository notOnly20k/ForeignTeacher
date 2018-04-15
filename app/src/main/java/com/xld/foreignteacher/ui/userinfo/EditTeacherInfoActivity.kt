package com.xld.foreignteacher.ui.userinfo

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.sinata.xldutils.utils.SPUtils
import com.example.liangmutian.mypicker.DatePickerDialog
import com.example.liangmutian.mypicker.DateUtil
import com.google.gson.Gson
import com.swifty.dragsquareimage.DraggablePresenterImpl
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.Language
import com.xld.foreignteacher.api.dto.User
import com.xld.foreignteacher.data.AlbumImgUrl
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.ext.isPhoneNumberValid
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.dialog.CustomDialog
import com.xld.foreignteacher.ui.dialog.MyActionDialog
import com.xld.foreignteacher.ui.dialog.StarrBarDialog
import com.xld.foreignteacher.ui.main.MainActivity
import com.xld.foreignteacher.ui.userinfo.adapter.LanguageAdapter
import com.xld.foreignteacher.ui.userinfo.adapter.StudentPageAdapter
import com.xld.foreignteacher.views.StarBarView
import com.xld.foreignteacher.views.ViewPagerIndicator
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

    private var sex: String? = null
    private var albumList = mutableListOf<AlbumImgUrl>()


    override fun initView() {
        try {
            val user: User = Gson().fromJson(SPUtils.getString("user"), User::class.java)
            et_contact_number.setText(user.phone)
            et_name.setText(user.nickName)
        } catch (e: Exception) {
            showToast(e.message)
        }

        if (intent.getStringExtra("type") == EDIT) {
            val urls = ArrayList<String>()
            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521814228383&di=7f62d8349c5414d66d647e1563fa0631&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F472309f790529822fb05a7e7ddca7bcb0a46d4e4.jpg")
            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521814228383&di=c7a129ca9489f8b42379273069d4acf1&imgtype=0&src=http%3A%2F%2Fp.yjbys.com%2Fimage%2F20161206%2F1481008661538628.jpg")
            adapter = StudentPageAdapter(this, urls)
            viewpager.adapter = adapter
            val viewPagerIndicator = ViewPagerIndicator(this, ll_indicator, R.mipmap.indicator_white, R.mipmap.indicator_yellow, urls.size)
            viewPagerIndicator.setupWithViewPager(viewpager)
            tv_title_right.text = EDIT
            tv_title_right.setOnClickListener {
                activityUtil.go(EditTeacherInfoActivity::class.java).put("type", SAVE).start()
            }
            et_name.isEnabled = false
            et_contact_number.isEnabled = false
            languageAdapter = LanguageAdapter(this, emptyList())
            languageAdapter.isShowFoot(false)
            rec_languages.adapter = languageAdapter
            rec_languages.layoutManager = LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL }


        } else {
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
                                sex = "1"
                                customDialog.dismiss()
                            }

                            override fun clickButton2(customDialog: CustomDialog) {
                                tv_gender_edit.text = "Female"
                                sex = "2"
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
                showDateDialog(DateUtil.getDateForString("1990-01-01"))
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

            tv_title_right.setOnClickListener { SaveTeacher() }
        }

    }

    private fun SaveTeacher() {

        var sort = 1
        for (i in 0 until draggablePresenter.imageUrls.size()) {
            if (draggablePresenter.imageUrls[i] != null) {
                sort++
                albumList.add(AlbumImgUrl(draggablePresenter.imageUrls[i], 1))
            }
        }

        val albumImgUrl = albumList.toString()
        logger.e { Gson().toJson(albumList.toString()) }
        appComponent.netWork.editTeacher(SPUtils.getInt("id"), et_name.text.toString(), draggablePresenter.imageUrls[0],
                sex!!, tv_birth_edit.text.toString(), et_contact_number.text.toString(), star_chinese_level.getSartRating().toInt(),
                personalProfile = er_introduction.text.toString())
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
        if (resultCode == SELECT_LANGUAGE) {
            tv_language.visibility = View.GONE
            val language = data!!.getSerializableExtra("language") as Language
            languageList.add(language)
            languageAdapter.updateList(languageList)
        }
    }

    override fun commitCheck(): Boolean {
        if (!et_contact_number.text.toString().isPhoneNumberValid()) {
            showToast("The phone is not in the correct format")
            return false
        }
        if (draggablePresenter.imageUrls == null || draggablePresenter.imageUrls.size() == 0) {
            showToast("Select at least one picture ")
            return false
        }
        if (tv_gender_edit.text.isEmpty() || tv_birth_edit.text.isEmpty() || et_name.text.isEmpty()) {
            showToast("Submit incomplete information ")
            return false
        }
        return super.commitCheck()
    }

    private fun showDateDialog(date: List<Int>) {
        val builder = DatePickerDialog.Builder(this)
        builder.setOnDateSelectedListener(object : DatePickerDialog.OnDateSelectedListener {
            override fun onDateSelected(dates: IntArray) {
                tv_birth_edit.text = (dates[0].toString() + "-" + (if (dates[1] > 9) dates[1] else "0" + dates[1]) + "-"
                        + if (dates[2] > 9) dates[2] else "0" + dates[2])

            }

            override fun onCancel() {

            }
        })
                .setSelectYear(date[0] - 1)
                .setSelectMonth(date[1] - 1)
                .setSelectDay(date[2] - 1)

        builder.setMaxYear(DateUtil.getYear())
        builder.setMaxMonth(DateUtil.getDateForString(DateUtil.getToday())[1])
        builder.setMaxDay(DateUtil.getDateForString(DateUtil.getToday())[2])
        val dateDialog = builder.create()
        dateDialog.show()
    }

    fun showUrls(view: View) {
        val array = draggablePresenter.imageUrls ?: return
        val stringBuffer = StringBuilder()
        for (i in 0 until array.size()) {
            val o = array.get(array.keyAt(i))
            stringBuffer.append(i).append(":").append(o).append("\n")
        }
    }

    companion object {
        const val SELECT_LANGUAGE = 2
        const val SELECT_CITY = 2
        const val EDIT = "edit"
        const val SAVE = "save"
    }
}
