package com.xld.foreignteacher.ui.square

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import cn.sinata.xldutils.utils.Toast
import cn.sinata.xldutils.utils.Utils
import cn.sinata.xldutils.xldUtils
import com.amap.api.services.core.PoiItem
import com.google.gson.Gson
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.SquareDate
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.locate.LocationActivity
import com.xld.foreignteacher.ui.square.adapter.MomentAdapter
import com.xld.foreignteacher.util.OssUtil
import kotlinx.android.synthetic.main.activity_moment.*
import java.io.File
import java.util.*

/**
 * Created by cz on 4/9/18.
 */
class MomentActivity : BaseTranslateStatusActivity(), MomentAdapter.OnItemClickListener {
    override val contentViewResId: Int
        get() = R.layout.activity_moment
    override val changeTitleBar: Boolean
        get() = false

    private val list = mutableListOf<String>()
    private var tempFile: File? = null//临时文件

    private lateinit var adpter: MomentAdapter
    lateinit var ossUtil: OssUtil

    override fun initView() {
        ossUtil = OssUtil(this)
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.addRightButton("Send") {
            sendSquare()
        }
        title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setTitle("Moment")
        adpter = MomentAdapter(this, emptyList(), supportFragmentManager)
        adpter.setOnItemClickListener(this)
        rec_pic.adapter = adpter
        rec_pic.layoutManager = GridLayoutManager(this, 3)
        tv_location.setOnClickListener {
            activityUtil.go(LocationActivity::class.java).startForResult(LocationActivity.ADDRESS)
        }

    }

    private fun sendSquare() {
        if (list.size != 0) {
            ossUtil.uploadMulti(list, object : OssUtil.OSSUploadCallBack() {
                override fun onFial(message: String?) {
                    super.onFial(message)
                    showToast(message)
                }

                override fun onFinish(urls: ArrayList<String>) {
                    super.onFinish(urls)
                    val imgList = mutableListOf<SquareDate.ImgUrlBean>()
                    urls.map { imgList.add(SquareDate.ImgUrlBean(it)) }
                    appComponent.netWork.addSquare(appComponent.userHandler.getUser()!!.id, Gson().toJson(imgList), tv_location.text.toString(), et_content.text.toString())
                            .doOnSubscribe { mCompositeDisposable.add(it) }
                            .doOnLoading { isShowDialog(it) }
                            .subscribe { finish() }
                }
            })
        } else {
            val imgList = mutableListOf<SquareDate.ImgUrlBean>()
            appComponent.netWork.addSquare(appComponent.userHandler.getUser()!!.id, imgList.toString(), tv_location.text.toString(), et_content.text.toString())
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .doOnLoading { isShowDialog(it) }
                    .subscribe { finish() }
        }

    }


    override fun initData() {
    }

    override fun takePhoto() {
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

    override fun select_image() {
        val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)// 调用android的图库
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun delete(url: String, position: Int) {
        list.removeAt(position)
        adpter.updateList(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                LocationActivity.ADDRESS -> {
                    val address = data!!.getParcelableExtra<PoiItem>("address")
                    tv_location.text = address.title
                }
                0 -> if (tempFile != null && tempFile!!.exists()) {
                    list.add(tempFile!!.absolutePath)
                    adpter.updateList(list)
                }

                1 -> if (data != null) {
                    val uri = data.data
                    if (uri != null) {
                        val path = Utils.getUrlPath(this, uri)
                        if (path != null) {
                            val typeIndex = path.lastIndexOf(".")
                            if (typeIndex != -1) {
                                val fileType = path.substring(typeIndex + 1).toLowerCase(Locale.CHINA)
                                //某些设备选择图片是可以选择一些非图片的文件。然后发送出去或出错。这里简单的通过匹配后缀名来判断是否是图片文件
                                //如果是图片文件则发送。反之给出提示
                                if (fileType == "jpg" || fileType == "gif"
                                        || fileType == "png" || fileType == "jpeg"
                                        || fileType == "bmp" || fileType == "wbmp"
                                        || fileType == "ico" || fileType == "jpe") {
                                    list.add(path)
                                    adpter.updateList(list)
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


}
