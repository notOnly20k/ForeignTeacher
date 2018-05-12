package com.whynuttalk.foreignteacher.ui.msg

import android.content.Intent
import android.view.LayoutInflater
import android.widget.TextView
import com.hyphenate.easeui.EaseConstant
import com.hyphenate.easeui.utils.EaseUserUtils
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.im.MyEaseChatFragment
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.dialog.CustomDialog
import com.whynuttalk.foreignteacher.ui.dialog.CustomPopWindow
import com.whynuttalk.foreignteacher.ui.report.ReportActivity
import kotlinx.android.synthetic.main.activity_chat.*

/**
 * Created by cz on 5/7/18.
 */
class ChatActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_chat
    override val changeTitleBar: Boolean
        get() = false


    private var chatFragment: MyEaseChatFragment? = null
    lateinit var toChatUsername: String
    private lateinit var popWindow: CustomPopWindow


    override fun initView() {
        img_title_left.setOnClickListener { finish() }
        tv_title_right.setOnClickListener {
            val split = toChatUsername.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val view = LayoutInflater.from(this).inflate(R.layout.item_pop_schedule, null, false)
            view.findViewById<TextView>(R.id.tv_offer).text = "Report"
            view.findViewById<TextView>(R.id.tv_group).text = "Block"

            view.findViewById<TextView>(R.id.tv_offer).setOnClickListener {
                if (split.size != 3)
                    return@setOnClickListener
                activityUtil.go(ReportActivity::class.java).put("id", Integer.parseInt(split[2])).put("type", ReportActivity.REPORT).start()
                popWindow.dissmiss()
            }
            view.findViewById<TextView>(R.id.tv_group).setOnClickListener {
                if (split.size != 3) {
                    return@setOnClickListener
                } else {
                    popWindow.dissmiss()
                    CustomDialog.Builder().create()
                            .setTitle("Do you really want to block this person sending you message?")
                            .setDialogListene(object : CustomDialog.CustomDialogListener {
                                override fun clickButton1(customDialog: CustomDialog) {
                                    appComponent.netWork.addBlacklist(appComponent.userHandler.getUser().id, Integer.parseInt(split[2]))
                                            .doOnLoading { isShowDialog(it) }
                                            .doOnSubscribe { mCompositeDisposable.add(it) }
                                            .subscribe {
                                                showToast("Add block success")
                                                customDialog.dismiss()
                                            }
                                }

                                override fun clickButton2(customDialog: CustomDialog) {
                                    customDialog.dismiss()
                                }

                            }).show(supportFragmentManager,"block")

                }

            }
            popWindow = CustomPopWindow.PopupWindowBuilder(this)
                    .setView(view)
                    .setFocusable(true)
                    .enableBackgroundDark(true)
                    .setBgDarkAlpha(40F)
                    .setOutsideTouchable(true)
                    .create()
            popWindow.showAsDropDown(tv_title_right, 0, 10)

        }
    }

    override fun initData() {
        toChatUsername = intent.extras.getString(EaseConstant.EXTRA_USER_ID)
        tv_title!!.text = (EaseUserUtils.getUserInfo(toChatUsername)!!.nick)
        chatFragment = MyEaseChatFragment()
        //set arguments
        chatFragment!!.arguments = intent.extras
        supportFragmentManager.beginTransaction().add(R.id.container, chatFragment).commit()
    }

    override fun onNewIntent(intent: Intent) {
        // enter to chat activity when click notification bar, here make sure only one chat activiy
        val username = intent.getStringExtra("userId")
        if (toChatUsername == username)
            super.onNewIntent(intent)
        else {
            finish()
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        chatFragment!!.onBackPressed()
    }

    /**
     * 获取评分成就数据
     */
    fun getScoreCard() {
        showDialog()
    }
}
