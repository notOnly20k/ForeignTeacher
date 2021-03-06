package com.whynuttalk.foreignteacher.ui.userinfo

import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import cn.sinata.xldutils.activitys.BaseActivity
import cn.sinata.xldutils.utils.ActivityUtil
import com.hyphenate.easeui.EaseConstant
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMWeb
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.ListActivity
import com.whynuttalk.foreignteacher.ui.dialog.ShareDialog
import com.whynuttalk.foreignteacher.ui.msg.ChatActivity
import com.whynuttalk.foreignteacher.ui.schedule.EditScheduleActivity
import com.whynuttalk.foreignteacher.ui.userinfo.adapter.StudentPageAdapter
import com.whynuttalk.foreignteacher.ui.userinfo.adapter.TeacherEvaluateAdapter
import com.whynuttalk.foreignteacher.ui.userinfo.adapter.TeacherGoodsAdapter
import com.whynuttalk.foreignteacher.ui.userinfo.adapter.TeacherMomentAdapter
import com.whynuttalk.foreignteacher.util.StatusBarUtil
import com.whynuttalk.foreignteacher.views.MyDialogProgress
import com.whynuttalk.foreignteacher.views.ViewPagerIndicator
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_teacher_detail.*
import org.slf4j.LoggerFactory


/**
 * Created by cz on 4/2/18.
 */
class TeacherDetailActivity : BaseActivity(), ShareDialog.OnShareItemClickListener, UMShareListener {
    override fun onStart(p0: SHARE_MEDIA?) {

    }

    override fun share(type: SHARE_MEDIA) {
        val web = UMWeb(appComponent.netWork.ShareTeacher(id))
        web.title = "This is music title"//标题
        //web.setThumb()  //缩略图
        web.description = "my description"//描述

        ShareAction(this)
                .setPlatform(type)
                .withMedia(web)
                .setCallback(this)
                .share()
    }

    override fun onResult(p0: SHARE_MEDIA?) {

    }

    override fun onCancel(p0: SHARE_MEDIA?) {

    }

    override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {

    }

    private lateinit var adapter: StudentPageAdapter
    private lateinit var activityUtil: ActivityUtil
    private lateinit var mCompositeDisposable: CompositeDisposable
    private lateinit var progress: MyDialogProgress
    private lateinit var teacherGoodsAdapter: TeacherGoodsAdapter
    private lateinit var teacherMomentAdapter: TeacherMomentAdapter
    private lateinit var teacherEvaluateAdapter: TeacherEvaluateAdapter
    private var id: Int = -1
    private val logger = LoggerFactory.getLogger("TeacherDetailActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_detail)
        id = intent.getIntExtra("id", -1)
        mCompositeDisposable = CompositeDisposable()
        progress = MyDialogProgress(this)
        ButterKnife.bind(this)
        StatusBarUtil.initStatus2(window)
        StatusBarUtil.initBarHeight(this, null, fake_status_bar)
        initView()
        initData()
    }

    private fun initView() {
        if (intent.getIntExtra("type", -1) == 1) {
            tv_chat.visibility = View.GONE
        }
        tv_self_introduce.setOnLongClickListener({
            //todo:翻译后显示
            //tv_translation.visibility = View.VISIBLE
            true
        })
        iv_share.setOnClickListener {
            ShareDialog().apply {
                setOnShareItemClickListener(this@TeacherDetailActivity)
            }.show(supportFragmentManager, "dialog_share")
        }

        iv_back.setOnClickListener { finish() }
        activityUtil = ActivityUtil.create(this)
        teacherGoodsAdapter = TeacherGoodsAdapter(this)
        teacherEvaluateAdapter = TeacherEvaluateAdapter(this)
        teacherMomentAdapter = TeacherMomentAdapter(this)
        lv_goods.adapter = teacherGoodsAdapter
        lv_moment.adapter = teacherMomentAdapter
        lv_evaluate.adapter = teacherEvaluateAdapter


        tv_chat.setOnClickListener {
            activityUtil.go(ChatActivity::class.java).put(EaseConstant.EXTRA_USER_ID, "waijiao_teacher_" + id).start()
        }
    }

    private fun initData() {
        val user = appComponent.userHandler.getUser()
        if (user.lat != null && user.lon != null) {
            appComponent.netWork.getTeacherDetail(id, user.lat!!, user.lon!!)
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .doOnLoading { isShowDialog(it) }
                    .subscribe {
                        val urls = ArrayList<String>()
                        if (it.albumList != null && it.albumList!!.isNotEmpty()) {
                            it.albumList!!.sortedBy { it.sort }.map { urls.add(it.imgUrl!!) }
                            adapter = StudentPageAdapter(this, urls)
                            viewpager.adapter = adapter
                            val viewPagerIndicator = ViewPagerIndicator(this, ll_indicator, R.mipmap.indicator_white, R.mipmap.indicator_yellow, urls.size)
                            viewPagerIndicator.setupWithViewPager(viewpager)
                        }
                        iv_user_head.setImageURI(it.imgUrl)
                        tv_user_name.text = it.nickName
                        tv_distance.text = "${it.km}km"
                        tv_score.text = "${it.score?.toDouble()}"
                        tv_cancel_count.text = String.format(resources.getString(R.string.cancel_order_count), it.cnacleOrderNum)
                        tv_country_edit.text = it.nationality
                        sbv_starbar.setStarRating(it.chineseLevel?.toFloat() ?: 0F)
                        tv_self_introduce.text = it.personalProfiles ?: ""
                        tv_my_language.text = it.eName
                        tv_comment_count.text = String.format(resources.getString(R.string.evaluate_count), it.commentNum)
                        if (it.commentNum > 3) {
                            img_more_comment.setOnClickListener { activityUtil.go(ListActivity::class.java).put("type", ListActivity.COMMENT).put("id", id).start() }
                        }
                        img_more_feed.setOnClickListener { activityUtil.go(ListActivity::class.java).put("type", ListActivity.FEEDS).put("id", id).start() }
                        img_more_offer.setOnClickListener { activityUtil.go(ListActivity::class.java).put("type", ListActivity.OFFERS).put("id", id).start() }
                        tv_time_table.setOnClickListener {
                            activityUtil.go(EditScheduleActivity::class.java).put("id", it.id).put("type", EditScheduleActivity.SCHEDULE).start()
                        }
                        teacherGoodsAdapter.upDataList(it.curriculumList ?: emptyList())
                        teacherEvaluateAdapter.upDataList(it.commentList ?: emptyList())
                        teacherMomentAdapter.upDataList(it.squareList ?: emptyList())
                    }
        } else {
            showToast("Location error")

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }
    fun showProgress(isOnLoading: Boolean) {
        if (isOnLoading) {
            progress.show()
        } else {
            progress.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progress != null) {
            progress.dismiss()
        }
        if (mCompositeDisposable != null) { //取消订阅
            mCompositeDisposable.clear()
        }
    }
}
