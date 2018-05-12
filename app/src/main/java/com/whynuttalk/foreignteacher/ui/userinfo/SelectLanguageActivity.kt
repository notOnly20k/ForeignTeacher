package com.whynuttalk.foreignteacher.ui.userinfo

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.City
import com.whynuttalk.foreignteacher.api.dto.Language
import com.whynuttalk.foreignteacher.api.dto.Nationality
import com.whynuttalk.foreignteacher.api.dto.SelectData
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.userinfo.adapter.SelectAToZAdapter
import com.whynuttalk.foreignteacher.util.PinyinComparator
import com.whynuttalk.foreignteacher.views.SideNavigationView
import kotlinx.android.synthetic.main.activity_select_language.*
import java.util.*

/**
 * Created by cz on 4/5/18.
 */
class SelectLanguageActivity : BaseTranslateStatusActivity(), SelectAToZAdapter.OnItemClickListener {
    override fun onItemClick(selectData: SelectData) {
        when (intent.getStringExtra("type")) {
            LANGUAGE -> setResult(SELECT_LANGUAGE, intent.putExtra(LANGUAGE, selectData as Language))
            CITY -> setResult(SELECT_CITY, intent.putExtra(CITY, selectData as City))
            NATIONALITY -> setResult(SELECT_NATIONALITY, intent.putExtra(NATIONALITY, selectData as Nationality))
        }

        finish()
    }

    private var sourceDateList = arrayListOf<SelectData>()
    private lateinit var adapter: SelectAToZAdapter

    override val contentViewResId: Int
        get() = R.layout.activity_select_language
    override val changeTitleBar: Boolean
        get() = false


    override fun initView() {
        if (intent.getStringExtra("type") == CITY) {
            rl_title.visibility = View.GONE
            ll_city_bar.visibility = View.VISIBLE
            et_search.hint = "Pleas enter current city"
        }
        adapter = SelectAToZAdapter(this, emptyList(),intent.getStringExtra("type"))
        rec_languages.adapter = adapter
        adapter.setOnItemClickListener(this)
        rec_languages.layoutManager = LinearLayoutManager(this)

        sn_sideBar.onNavigateListener = object : SideNavigationView.OnNavigationListener {
            override fun onNavigateTo(c: String) {
                tv_currentChar.visibility = View.VISIBLE
                tv_currentChar.text = c
                val position = adapter.getPositionForSection(c.toCharArray()[0].toInt())
                if (position != null) {
                    (rec_languages.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
                }
            }

            override fun onNavigateCancel() {
                tv_currentChar.visibility = View.GONE
            }

        }

        tv_search.setOnClickListener {
            ll_search_bar.visibility = View.VISIBLE
            if (intent.getStringExtra("type") == CITY) {
                ll_city_bar.visibility = View.GONE
            } else {
                rl_title.visibility = View.GONE
            }
            fl_translucent.visibility = View.VISIBLE
            et_search.isFocusable = true
            et_search.isFocusableInTouchMode = true
            et_search.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        tv_cancel.setOnClickListener {
            ll_search_bar.visibility = View.GONE
            if (intent.getStringExtra("type") == CITY) {
                ll_city_bar.visibility = View.VISIBLE
            } else {
                rl_title.visibility = View.VISIBLE
            }
            fl_translucent.visibility = View.GONE
            adapter.updateList(sourceDateList)
            hideKeyboard()
        }

        tv_search_city.setOnClickListener {
            ll_city_bar.visibility = View.GONE
            ll_search_bar.visibility = View.VISIBLE
            et_search.isFocusable = true
            et_search.isFocusableInTouchMode = true
            et_search.requestFocus()
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


        tv_close.setOnClickListener {
            finish()
        }
        tv_back.setOnClickListener {
            finish()
        }
    }

    private fun filterData(filterStr: String) {
        showProgress(true)
        var filterDateList: MutableList<SelectData> = ArrayList()

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = sourceDateList
        } else {
            filterDateList.clear()
            sourceDateList
                    .filter { it.enName!!.toLowerCase().contains(filterStr.toLowerCase()) }
                    .forEach { filterDateList.add(it) }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, PinyinComparator())
        adapter.updateList(filterDateList)
        showProgress(false)
    }

    override fun initData() {
        when (intent.getStringExtra("type")) {
            LANGUAGE -> {
                appComponent.netWork.getLanguage()
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .doOnLoading { isShowDialog(it) }
                        .map {
                            Collections.sort(it, PinyinComparator())
                            it
                        }
                        .subscribe { list ->
                            Collections.sort(list, PinyinComparator())
                            sourceDateList.addAll(list)
                            adapter.updateList(list)
                        }
            }
            CITY -> {
//                appComponent.locationHandler.locationSubject
//                        .doOnSubscribe { mCompositeDisposable.add(it) }
//                        .subscribe { locate ->
//                            appComponent.netWork.getOpenedCity()
//                                    .doOnSubscribe { mCompositeDisposable.add(it) }
//                                    .subscribe {
//                                        if (it.find { it.enName == locate.city } != null) {
//                                            appComponent.userHandler.saveUser(
//                                                    appComponent.userHandler.getUser().copy(
//                                                            lat = locate.latitude,
//                                                            lon = locate.longitude,
//                                                            city = locate.city
//                                                    )
//                                            )
//                                        } else {
//                                            showToast("current city is not open")
//                                            appComponent.userHandler.getUser().copy(
//                                                    city = it.firstOrNull()?.enName ?: ""
//                                            )
//                                        }
//
//                                    }
//                        }
//                appComponent.locationHandler.start()
                appComponent.netWork.getOpenedCity()
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .doOnLoading { isShowDialog(it) }
                        .subscribe { list ->
                            sourceDateList.addAll(list)
                            adapter.updateList(list)
                        }
                val header = LayoutInflater.from(this).inflate(R.layout.item_city_title, rec_languages, false) as TextView
                adapter.setHeaderView(header)
            }
            NATIONALITY -> {
                val list = appComponent.netWork.getNationality()
                Collections.sort(list, PinyinComparator())
                sourceDateList.addAll(list)
                adapter.updateList(list)
            }
        }


    }

    companion object {
        const val CITY = "city"
        const val LANGUAGE = "language"
        const val NATIONALITY = "nationality"
        const val SELECT_LANGUAGE = 1
        const val SELECT_CITY = 2
        const val SELECT_NATIONALITY = 3
    }

}
