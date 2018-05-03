package com.xld.foreignteacher.ui.mine.verification

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import cn.sinata.xldutils.utils.Toast
import cn.sinata.xldutils.utils.Utils
import cn.sinata.xldutils.xldUtils
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.MyAuthentication
import com.xld.foreignteacher.api.dto.User
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.dialog.CustomDialog
import kotlinx.android.synthetic.main.activity_up_load_id.*
import java.io.File
import java.util.*

/**
 * Created by cz on 4/10/18.
 */
class UpLoadIdActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_up_load_id
    override val changeTitleBar: Boolean
        get() = false
    private var filePath = ""
    private var tempFile: File? = null//临时文件
    private
    val user: User by lazy { appComponent.userHandler.getUser() }
    lateinit var type: String
    lateinit var  myAuthentication: MyAuthentication
    var upLoadType: Int = -1
    override fun initView() {
        type = intent.getStringExtra("type")
        upLoadType = when (type) {
            PASSPORT -> 3
            VALID_VISA -> 4
            CERTIFICATE -> 1
            else -> -1
        }
        when (type) {
            PASSPORT -> {
                myAuthentication=intent.getSerializableExtra("data") as MyAuthentication
                tv_upload_title.text = "Passport page"
                tv_tips_title.text = "Holding passport photo"
                tv_tips_1.text = "1.-include passport owner half body picture"
                tv_tips_2.text = "2.-clear face without any cover"
                tv_tips_3.visibility = View.GONE
                tv_choose_type.text=myAuthentication.huzhao?.credentialsNumber?:""
                img_pic.setImageURI(myAuthentication.huzhao?.credentialsImg)
                tv_choose_type.hint = "Passport number"
            }
            VALID_VISA -> {
                myAuthentication=intent.getSerializableExtra("data") as MyAuthentication
                tv_tips_title.text = "Holding passport photo"
                tv_tips_1.text = "1.-include passport owner half body picture"
                tv_tips_2.text = "2.-visa need to be clearly viewed"
                tv_tips_3.text = "3.-clear face without any cover"
                tv_choose_type.text=myAuthentication.qianzheng?.credentialsNumber?:""
                img_pic.setImageURI(myAuthentication.qianzheng?.credentialsImg)
                tv_choose_type.hint = "Visa number"
            }
            CERTIFICATE -> {
                tv_upload_title.text = "Upload photo"
                tv_tips_title.visibility = View.GONE
                tv_tips_1.visibility = View.GONE
                tv_tips_2.visibility = View.GONE
                tv_tips_3.visibility = View.GONE
            }
        }

        img_pic.setOnClickListener { showPicDialog() }
        title_bar.setTitle(type)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.addRightButton("Submit") { upLoad() }
        title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
    }

    fun upLoad() {
        if (commitCheck()) {
            appComponent.netWork.applyAuthentication(1, null, et_number.text.toString(), filePath, upLoadType)
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .doOnLoading { showProgress(it) }
                    .subscribe { finish() }
        }
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
                0 -> if (tempFile != null && tempFile!!.exists()) {
                    filePath = tempFile!!.absolutePath
                    img_pic.setImageURI(Uri.fromFile(tempFile))
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
                                    filePath = tempFile!!.absolutePath
                                    img_pic.setImageURI(Uri.fromFile(tempFile))
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

    override fun commitCheck(): Boolean {
        if (et_number.text.toString().isEmpty()) {
            return false
        }
        if (filePath.isEmpty()) {
            return false
        }
        return super.commitCheck()
    }

    override fun initData() {
    }

    companion object {
        const val PASSPORT = "Passport"
        const val VALID_VISA = "Valid_visa"
        const val CERTIFICATE = "Teaching certificate"
    }
}
