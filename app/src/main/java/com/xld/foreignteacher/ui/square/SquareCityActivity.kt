package com.xld.foreignteacher.ui.square

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.City
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_square_city.*

/**
 * Created by cz on 4/9/18.
 */
class SquareCityActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_square_city
    override val changeTitleBar: Boolean
        get() = false

    private val openList = mutableListOf<City>()
    private val notOpenList = mutableListOf<City>()


    lateinit var inflater: LayoutInflater

    override fun initView() {
        tv_close.setOnClickListener { finish() }
        tv_search_city.setOnClickListener {
            startSearch()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }

        })

        tv_cancel.setOnClickListener {
            cancelSearch()
        }
    }

    fun startSearch() {
        ll_search_bar.visibility = View.VISIBLE
        ll_city_bar.visibility = View.GONE
        tv_locate_city.visibility = View.GONE
        et_search.isFocusableInTouchMode = true
        tv_title_open.visibility = View.GONE
        tv_title_not_open.visibility = View.GONE
        et_search.requestFocus()
        fl_opended_city.removeAllViews()
        fl_not_open_city.removeAllViews()
    }

    fun cancelSearch() {
        tv_title_open.visibility = View.VISIBLE
        tv_title_not_open.visibility = View.VISIBLE
        ll_search_bar.visibility = View.GONE
        ll_city_bar.visibility = View.VISIBLE
        tv_locate_city.visibility = View.VISIBLE
        openList.map { filedDate(it,"open") }
        notOpenList.map { filedDate(it,"not_open") }
        hideKeyboard()
    }

    private fun filterData(string: String) {
        startSearch()
        if (!TextUtils.isEmpty(string)) {
            openList.map {
                if (it.name.contains(string)) {
                    tv_title_open.visibility = View.VISIBLE
                    filedDate(it, "open")
                }
            }

            notOpenList.map {
                if (it.name.contains(string)) {
                    tv_title_open.visibility = View.VISIBLE
                    filedDate(it, "not_open")
                }
            }
        }
    }

    override fun initData() {
        inflater = LayoutInflater.from(this)
        appComponent.netWork
                .getOpenedCity()
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { isShowDialog(it) }
                .subscribe { citys ->
                    openList.clear()
                    openList.addAll(citys)
                    citys.map { filedDate(it, "open") }

                }

        appComponent.netWork.getNotOpenCity()
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { isShowDialog(it) }
                .subscribe { citys ->
                    notOpenList.clear()
                    notOpenList.addAll(citys)
                    citys.map { filedDate(it, "not_open") }
                }
    }

    fun filedDate(city: City, type: String) {
        if (type == "open") {

            val view = inflater.inflate(R.layout.item_flow, fl_opended_city, false) as TextView
            view.text = city.name
            if (intent.getStringExtra(CURRENT_CITY) != null && city.name == intent.getStringExtra(CURRENT_CITY)) run {
                view.background = this@SquareCityActivity.resources.getDrawable(R.drawable.bg_city_item_select)
            }
            view.setOnClickListener { it ->
                logger.e { (it as TextView).text }
                setResult(SELECTCITY, intent.putExtra("city", "sssss"))
                finish()
            }
            fl_opended_city.addView(view)

        } else if (type == "not_open") {
            val view = inflater.inflate(R.layout.item_flow, fl_not_open_city, false) as TextView
            view.text = city.name
            fl_not_open_city.addView(view)
            view.setOnClickListener { it ->
                showToast("This city not open")
            }
        }

    }

    companion object {
        const val SELECTCITY = 1
        const val CURRENT_CITY = "current_city"
    }
}
