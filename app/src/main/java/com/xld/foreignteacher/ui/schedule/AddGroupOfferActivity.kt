package com.xld.foreignteacher.ui.schedule

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import cn.sinata.xldutils.utils.TimeUtils
import cn.sinata.xldutils.utils.Toast
import cn.sinata.xldutils.utils.Utils
import cn.sinata.xldutils.xldUtils
import com.amap.api.services.core.PoiItem
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.formateToNum
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.dialog.ClassDateDialog
import com.xld.foreignteacher.ui.dialog.CustomDialog
import com.xld.foreignteacher.ui.dialog.WheelDialog
import com.xld.foreignteacher.ui.locate.LocationActivity
import com.xld.foreignteacher.util.OssUtil
import kotlinx.android.synthetic.main.activity_add_grouo_offer.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cz on 4/16/18.
 */
class AddGroupOfferActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_add_grouo_offer
    override val changeTitleBar: Boolean
        get() = false
    private var filePath = ""
    private var tempFile: File? = null//临时文件
    private var languageId: Int? = null
    var lat = ""
    var lon = ""
    var introduction = ""
    var picList = mutableListOf<String>()
    lateinit var startTime: Date
    lateinit var endtTime: Date
    lateinit var deadLinetTime: Date
    val ossUtil: OssUtil by lazy { OssUtil(this) }

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Add Group offer")
        title_bar.addRightButton("Preview", {})
        title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))

        et_start_time.setOnClickListener {
            ClassDateDialog.Builder()
                    .create()
                    .setListener(object : ClassDateDialog.ClassDateDialogListener {
                        override fun clickCancel() {
                        }

                        override fun clickSure(date: String, hour: String, min: String, time: Date) {
                            val text = "$date  $hour:$min"
                            et_start_time.setText(text)
                            startTime = transToDate(date, hour, min, time)
                        }

                    })
                    .show(supportFragmentManager, "class")
        }
        et_end_time.setOnClickListener {
            ClassDateDialog.Builder()
                    .create()
                    .setListener(object : ClassDateDialog.ClassDateDialogListener {
                        override fun clickCancel() {
                        }

                        override fun clickSure(date: String, hour: String, min: String, time: Date) {
                            val text = "$date  $hour:$min"
                            et_end_time.setText(text)
                            endtTime = transToDate(date, hour, min, time)
                        }

                    })
                    .show(supportFragmentManager, "class")
        }
        et_deadline.setOnClickListener {
            ClassDateDialog.Builder()
                    .create()
                    .setListener(object : ClassDateDialog.ClassDateDialogListener {
                        override fun clickCancel() {
                        }

                        override fun clickSure(date: String, hour: String, min: String, time: Date) {
                            val text = "$date  $hour:$min"
                            et_deadline.setText(text)
                            deadLinetTime = transToDate(date, hour, min, time)
                        }

                    })
                    .show(supportFragmentManager, "class")
        }
        et_language.setOnClickListener {
            appComponent.netWork.getLanguage()
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .subscribe { list ->
                        val textList = mutableListOf<String>()
                        list.map { textList.add(it.eName!!) }
                        WheelDialog.Builder().create()
                                .setDate(textList)
                                .setListener(object : WheelDialog.WheelDialogListener {
                                    override fun clickCancel() {

                                    }

                                    override fun clickSure(string: String) {
                                        list.map {
                                            if (it.eName == string)
                                                languageId = it.id
                                        }
                                        et_language.setText(string)
                                    }

                                })
                                .show(supportFragmentManager, "language")
                    }
        }
        et_location.setOnClickListener {
            activityUtil.go(LocationActivity::class.java).startForResult(LocationActivity.ADDRESS)
        }
        img_up_load.setOnClickListener {
            showPicDialog()
        }

        et_offer_detail.setOnClickListener {
            activityUtil.go(IntroductionActivity::class.java).startForResult(IntroductionActivity.INTRODUCTION)
        }

        btn_submit.setOnClickListener { submit() }

    }

    fun transToDate(date: String, hour: String, min: String, time: Date): Date {
        val f = SimpleDateFormat("yyyy", Locale.CHINA)
        val year = f.format(time)
        val date = "$year-$date $hour:$min:00"
        return TimeUtils.parseTime(date)
    }

    fun submit() {
        if (commitCheck()) {
            val list = mutableListOf<String>()
            list.addAll(picList)
            list.add(filePath)
            ossUtil.uploadMulti(list, object : OssUtil.OSSUploadCallBack() {
                override fun onFial(message: String?) {
                    super.onFial(message)
                }

                override fun onFinish(urls: ArrayList<String>?) {
                    super.onFinish(urls)

                    appComponent.netWork.addFight(appComponent.userHandler.getUser().id, languageId!!, et_max.text.toString().toInt(), et_min.text.toString().toInt(),
                            et_rate.text.toString().toInt(), et_name.text.toString(), et_contact.text.toString().formateToNum(), et_location.text.toString(), lat, lon,
                            urls!!.last(), introduction, urls!!.dropLast(1).toString(), deadLinetTime, deadLinetTime, startTime, endtTime)
                            .doOnLoading { showProgress(it) }
                            .doOnSubscribe { mCompositeDisposable.add(it) }
                            .subscribe {
                                showToast("submit success")
                                finish()
                            }
                }
            })
        }
    }

    override fun commitCheck(): Boolean {
        if (et_deadline.text.isEmpty() || et_contact.text.isEmpty() || et_end_time.text.isEmpty() || et_start_time.text.isEmpty() || et_language.text.isEmpty()
                || et_location.text.isEmpty() || et_max.text.isEmpty() || et_rate.text.isEmpty() || et_min.text.isEmpty() || et_name.text.isEmpty()) {
            showToast("Perfect the form ")
            return false
        }
        if (filePath.isEmpty()) {
            showToast("select background picture")
            return false
        }
        if (startTime < deadLinetTime) {
            showToast("startTime must be great than the deadline")
            return false
        }
        if (startTime > endtTime) {
            showToast("endTime must be great than the startTime")
            return false
        }
        if (introduction.isEmpty()){
            showToast("edit introduction")
            return false
        }
        return super.commitCheck()
    }


    fun showPicDialog() {
        CustomDialog.Builder()
                .create()
                .showtitle(false)
                .setButton1Text((R.string.take_photo).toFormattedString(this))
                .setButton2Text((R.string.select_image).toFormattedString(this))
                .setDialogListene(object : CustomDialog.CustomDialogListener {
                    override fun clickButton1(customDialog: CustomDialog) {
                        takePhoto()
                        customDialog.dismiss()
                    }

                    override fun clickButton2(customDialog: CustomDialog) {
                        select_image()
                        customDialog.dismiss()
                    }

                }).show(supportFragmentManager, "select_photo")
    }

    fun select_image() {
        val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)// 调用android的图库
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    fun takePhoto() {
        xldUtils.initFilePath()
        val intent = Intent(
                MediaStore.ACTION_IMAGE_CAPTURE)
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        tempFile = File(xldUtils.PICDIR, fileName)
        val u = Uri.fromFile(tempFile)
        //7.0崩溃问题
        if (Build.VERSION.SDK_INT < 24) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, u)
        } else {
            val contentValues = ContentValues(1)
            contentValues.put(MediaStore.Images.Media.DATA, tempFile!!.absolutePath)
            val uri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                LocationActivity.ADDRESS -> {
                    val address = data!!.getParcelableExtra<PoiItem>("address")
                    lat = address.latLonPoint.latitude.toString()
                    lon = address.latLonPoint.longitude.toString()
                    et_location.setText(address.cityName + address.adName + address.snippet)
                }
                IntroductionActivity.INTRODUCTION -> {
                    val bundle = data!!.getBundleExtra("introduction")
                    introduction = bundle.getString("content")
                    picList = bundle.getStringArrayList("pics")
                    et_offer_detail.setText("Edited")
                }
                0 -> if (tempFile != null && tempFile!!.exists()) {
                    filePath = tempFile!!.absolutePath
                    img_up_load.setImageURI(Uri.fromFile(tempFile))
                }

                1 -> if (data != null) {
                    val uri = data.data
                    if (uri != null) {
                        val path = Utils.getUrlPath(this, uri)
                        if (path != null) {
                            val typeIndex = path.lastIndexOf(".")
                            if (typeIndex != -1) {
                                val fileType = path.substring(typeIndex + 1).toLowerCase(Locale.CHINA)
                                if (fileType == "jpg" || fileType == "gif"
                                        || fileType == "png" || fileType == "jpeg"
                                        || fileType == "bmp" || fileType == "wbmp"
                                        || fileType == "ico" || fileType == "jpe") {
                                    filePath = tempFile!!.absolutePath
                                    img_up_load.setImageURI(Uri.fromFile(tempFile))
                                } else {
                                    Toast.create(this).show((R.string.error_pic_type).toFormattedString(this))
                                }
                            } else {
                                Toast.create(this).show((R.string.error_pic_type).toFormattedString(this))
                            }
                        } else {
                            Toast.create(this).show((R.string.error_pic_type).toFormattedString(this))
                        }
                    } else {
                        Toast.create(this).show((R.string.error_pic_type).toFormattedString(this))
                    }
                }
            }
        }
    }

    override fun initData() {

    }
}
