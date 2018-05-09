package com.xld.foreignteacher.ui.schedule

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import cn.sinata.xldutils.utils.Toast
import cn.sinata.xldutils.utils.Utils
import cn.sinata.xldutils.xldUtils
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.square.adapter.MomentAdapter
import kotlinx.android.synthetic.main.activity_introduction.*
import java.io.File
import java.util.*

/**
 * Created by cz on 5/3/18.
 */
class IntroductionActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_introduction
    override val changeTitleBar: Boolean
        get() = false
    private lateinit var adapter: MomentAdapter
    private val list = mutableListOf<String>()
    private var tempFile: File? = null//临时文件
    var introduction = ""
    var picList = mutableListOf<String>()

    override fun initView() {
        picList=intent.getStringArrayListExtra("pic")
        introduction=intent.getStringExtra("ins")
        list.addAll(picList)
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Introduction")
        title_bar.addRightButton("Submit", { submit() })
        title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
        adapter = MomentAdapter(this, emptyList(), supportFragmentManager)
        adapter.setOnItemClickListener(object : MomentAdapter.OnItemClickListener {
            override fun takePhoto() {
                this@IntroductionActivity.takePhoto()
            }

            override fun select_image() {
                this@IntroductionActivity.selectPhoto()
            }

            override fun delete(url: String, position: Int) {
                list.removeAt(position)
                adapter.updateList(list)
            }

        })
        rec_pic.adapter = adapter
        adapter.updateList(picList)
        et_content.setText(introduction)
        rec_pic.layoutManager = GridLayoutManager(this, 3)

    }

    fun submit() {
        if (commitCheck()) {
            val bundle = Bundle()
            val picList = arrayListOf<String>()
            picList.addAll(list)
            bundle.putString("content", et_content.text.toString())
            bundle.putStringArrayList("pics", picList)
            setResult(Activity.RESULT_OK, intent.putExtra("introduction", bundle))
            finish()
        }
    }

    override fun commitCheck(): Boolean {
        if (list.isEmpty()) {
            showToast("select at least one picture")
            return false
        }

        if (et_content.text.isEmpty()) {
            showToast("write something")
            return false
        }
        return super.commitCheck()
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
            val uri = this@IntroductionActivity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
        startActivityForResult(intent, 0)
    }

    fun selectPhoto() {
        val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)// 调用android的图库
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                0 -> if (tempFile != null && tempFile!!.exists()) {
                    list.add(tempFile!!.absolutePath)
                    logger.e { tempFile!!.absolutePath }
                    adapter.updateList(list)
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
                                    logger.e { path }
                                    adapter.updateList(list)
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

    companion object {
        val INTRODUCTION = 111
    }
}
