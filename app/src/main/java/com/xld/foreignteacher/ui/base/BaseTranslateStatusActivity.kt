package com.xld.foreignteacher.ui.base

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import butterknife.ButterKnife
import cn.sinata.xldutils.activitys.BaseActivity
import cn.sinata.xldutils.netstatus.NetUtils
import cn.sinata.xldutils.utils.ActivityUtil
import cn.sinata.xldutils.utils.Toast
import com.xld.foreignteacher.util.StatusBarUtil
import com.xld.foreignteacher.views.MyDialogProgress
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by cz on 3/28/18.
 */
abstract class BaseTranslateStatusActivity : BaseActivity() {
    protected lateinit var activityUtil: ActivityUtil
    protected lateinit var mCompositeDisposable: CompositeDisposable
    protected lateinit var progress: MyDialogProgress
    protected var isOpen: Boolean = true

    /**
     * 提供Activity要绑定的xml资源id
     *
     * @return
     */
    protected abstract val contentViewResId: Int
    protected abstract val changeTitleBar: Boolean
    protected val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewResId)
        if (changeTitleBar) {
            setTransparentTitleBar()
        }
        ButterKnife.bind(this)
        activityUtil = ActivityUtil.create(this)
        mCompositeDisposable = CompositeDisposable()
        progress = MyDialogProgress(this)
        initView()
        initData()
    }

    /**
     * 设置状态栏透明
     */
    private fun setTransparentTitleBar() {
        //       完全透明，要改变状态栏文字颜色以适应白色状态栏
        //        View decorview = getWindow().getDecorView();
        //        if(Build.VERSION.SDK_INT>=21){//5.0以上的系统支持
        //            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//表示让应用主题内容占据系统状态栏的空间
        //            decorview.setSystemUiVisibility(option);
        //            getWindow().setStatusBarColor(Color.parseColor("#00ffffff"));//设置状态栏颜色为透明
        //        }
        //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        StatusBarUtil.initStatus(window)
    }

    /**
     * 初始化控件
     */
    protected abstract fun initView()


    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 提交信息前的检查
     * 默认检查了网络
     */
    protected open fun commitCheck(): Boolean {
        if (!NetUtils.isNetworkAvailable(this)) {
            Toast.create(this).show("网络错误，请检查后重试")
            return false
        }
        return true
    }

    /**
     * 关闭键盘
     *
     * @param
     */
    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && currentFocus != null) {
            if (currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
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
        if (mCompositeDisposable != null) { //取消订阅
            mCompositeDisposable.clear()
        }
    }
}
