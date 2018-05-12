package com.whynuttalk.foreignteacher.ui.schedule

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import cn.sinata.xldutils.utils.TimeUtils
import cn.sinata.xldutils.utils.Toast
import cn.sinata.xldutils.utils.Utils
import cn.sinata.xldutils.xldUtils
import com.amap.api.services.core.PoiItem
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.data.PreviewGroupOrder
import com.whynuttalk.foreignteacher.ext.*
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.dialog.ClassDateDialog
import com.whynuttalk.foreignteacher.ui.dialog.CustomDialog
import com.whynuttalk.foreignteacher.ui.dialog.WheelDialog
import com.whynuttalk.foreignteacher.ui.locate.LocationActivity
import com.whynuttalk.foreignteacher.util.OssUtil
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
    var upDataPicList = mutableListOf<String>()
    val ossUtil: OssUtil by lazy { OssUtil(this) }

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Add Group offer")
        title_bar.addRightButton("Preview", {
            preview()
        })
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
            activityUtil.go(LocationActivity::class.java).put("type",1).startForResult(LocationActivity.ADDRESS)
        }
        img_up_load.setOnClickListener {
            showPicDialog()
        }

        et_offer_detail.setOnClickListener {
            activityUtil.go(IntroductionActivity::class.java).put("ins", introduction).putStringList("pic", picList as ArrayList<String>?).startForResult(IntroductionActivity.INTRODUCTION)
        }

        img_locate.setOnClickListener { initData() }

        btn_submit.setOnClickListener { submit() }

    }

    fun preview() {
        val previewData = PreviewGroupOrder()
        val f = SimpleDateFormat("MM-dd", Locale.CHINA)
        val f1 = SimpleDateFormat("HH:mm", Locale.CHINA)
        val f2 = SimpleDateFormat("yyyy-mm-dd HH:mm", Locale.CHINA)
        if (et_end_time.text.isNotEmpty() && et_start_time.text.isNotEmpty()) {
            val week = f.format(startTime)
            val start = f1.format(startTime)
            val end = f1.format(endtTime)
            previewData.time = "$week $start ~ $end"
        }

        if (et_deadline.text.isNotEmpty()) {
            val dead = f2.format(deadLinetTime)
            previewData.deadline = dead
        }

        previewData.address = et_location.text.toString()
        previewData.introduction = introduction
        previewData.pics = picList
        previewData.backGround = filePath
        previewData.max = et_max.text.toString()
        previewData.min = et_min.text.toString()
        previewData.title = et_name.text.toString()
        previewData.price = et_rate.text.toString()
        activityUtil.go(PreviewGroupOrderActivity::class.java).put("data", previewData).start()
    }

    fun transToDate(date: String, hour: String, min: String, time: Date): Date {
        val f = SimpleDateFormat("yyyy", Locale.CHINA)
        val year = f.format(time)
        val date = "$year-$date $hour:$min:00"
        return TimeUtils.parseTime(date)
    }

    fun submit() {
        if (commitCheck()) {
            showProgress(true)
            val list = mutableListOf<String>()
            list.addAll(picList)
            list.add(filePath)
            if (upDataPicList.isEmpty()) {
                ossUtil.uploadMulti(list, object : OssUtil.OSSUploadCallBack() {
                    override fun onFial(message: String?) {
                        super.onFial(message)
                        showProgress(false)
                    }

                    override fun onFinish(urls: ArrayList<String>?) {
                        super.onFinish(urls)
                        upDataPicList.addAll(urls!!)
                        updata(urls!!)

                    }
                })
            } else {
                updata(upDataPicList)
            }
        }
    }

    fun updata(urls: MutableList<String>) {
        val formart = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA)
        val deadline = formart.format(deadLinetTime)
        val start = formart.format(startTime)
        val end = formart.format(endtTime)
        appComponent.netWork.addFight(appComponent.userHandler.getUser().id, languageId!!, et_max.text.toString().toInt(), et_min.text.toString().toInt(),
                et_rate.text.toString().toInt(), et_name.text.toString(), et_contact.text.toString().formateToNum(), et_location.text.toString(), lat, lon,
                urls!!.last(), introduction, urls!!.dropLast(1).toString(), deadline, deadline, start, end)
                .doOnLoading { isShowDialog(it) }
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .subscribe {
                    showToast("submit success")
                    finish()
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
        if (introduction.isEmpty()) {
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
                    et_location.setText(address.title)
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
                                    filePath = path
                                    img_up_load.setImageURI(Uri.fromFile(File(path)))
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
        appComponent.netWork.getBenchmarkPrice(appComponent.userHandler.getUser()!!.id)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .subscribe {
                    if (it.benchmarkPrice!=null) {
                        val text = SpannableString("Actual income:￥${it.benchmarkPrice}/h")
                        text.setSpan(ForegroundColorSpan(resources.getColor(R.color.black_00)), 15,15+it.benchmarkPrice.toString().length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                        tv_actual.text = text
                    }else{
                        tv_actual.visibility= View.GONE
                    }
                }
        appComponent.locationHandler.locationSubject
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { isShowDialog(it) }
                .subscribe { locate ->
                    appComponent.netWork.getOpenedCity()
                            .doOnSubscribe { mCompositeDisposable.add(it) }
                            .doOnLoading { isShowDialog(it) }
                            .subscribe {
                                logger.e { locate.city }
                                if (it.find { it.enName == locate.city } != null) {
                                    et_location.setText(locate.address)
                                    lat = locate.latitude.toString()
                                    lon = locate.longitude.toString()
                                } else {
                                    showToast("current city is not open")
                                }

                            }
                }
        appComponent.locationHandler.start()


    }
}
