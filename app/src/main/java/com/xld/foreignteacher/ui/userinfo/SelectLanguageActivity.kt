package com.xld.foreignteacher.ui.userinfo

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.Language
import com.xld.foreignteacher.api.dto.SelectData
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.userinfo.adapter.SelectAToZAdapter
import com.xld.foreignteacher.util.PinyinComparator
import com.xld.foreignteacher.views.SideNavigationView
import com.zhy.view.flowlayout.FlowLayout
import kotlinx.android.synthetic.main.activity_select_language.*
import java.util.*

/**
 * Created by cz on 4/5/18.
 */
class SelectLanguageActivity : BaseTranslateStatusActivity(), SelectAToZAdapter.OnItemClickListener {
    override fun onItemClick(selectData: SelectData) {
        when(intent.getStringExtra("type")){
            LANGUAGE->  setResult(EditTeacherInfoActivity.SELECT_LANGUAGE, intent.putExtra(LANGUAGE, selectData as Language))
            CITY->  setResult(EditTeacherInfoActivity.SELECT_LANGUAGE, intent.putExtra(CITY, selectData as Language))
            NATIONALITY->  setResult(EditTeacherInfoActivity.SELECT_LANGUAGE, intent.putExtra(NATIONALITY, selectData as Language))
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
     if (intent.getStringExtra("type")== CITY){
         rl_title.visibility=View.GONE
         ll_city_bar.visibility=View.VISIBLE
         et_search.hint="Pleas enter current city"
     }
        adapter = SelectAToZAdapter(this, emptyList())
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
            if (intent.getStringExtra("type")== CITY){
                ll_city_bar.visibility=View.GONE
            }else{
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
            if (intent.getStringExtra("type")== CITY){
                ll_city_bar.visibility=View.VISIBLE
            }else{
                rl_title.visibility = View.VISIBLE
            }
            fl_translucent.visibility = View.GONE
            adapter.updateList(sourceDateList)
            hideKeyboard()
        }

        tv_search_city.setOnClickListener {
            ll_city_bar.visibility=View.GONE
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
                    .filter { it.Name!!.toLowerCase().contains(filterStr) }
                    .forEach { filterDateList.add(it) }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, PinyinComparator())
        adapter.updateList(filterDateList)
        showProgress(false)
    }

    override fun initData() {
        when(intent.getStringExtra("type")){
            LANGUAGE->{
                appComponent.netWork.getLanguage()
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .doOnLoading { showProgress(it) }
                        .map {
                            Collections.sort(it, PinyinComparator())
                            it
                        }
                        .subscribe { list ->
                            val newdata = mutableListOf<Language>()
                    newdata.add(Language("AC", null, 1, null))
                    newdata.add(Language("BC", null, 1, null))
                    newdata.add(Language("CC", null, 1, null))
                    newdata.add(Language("DC", null, 1, null))
                    newdata.add(Language("EC", null, 1, null))
                    newdata.add(Language("FC", null, 1, null))
                    newdata.add(Language("GC", null, 1, null))
                    newdata.add(Language("GC", null, 1, null))
                    newdata.add(Language("AC", null, 1, null))
                    newdata.add(Language("GC", null, 1, null))
                    newdata.add(Language("GC", null, 1, null))
                    newdata.add(Language("ADC", null, 1, null))
                    newdata.add(Language("ADC", null, 1, null))
                    newdata.add(Language("ABC", null, 1, null))
                    newdata.add(Language("AHDC", null, 1, null))
                    newdata.add(Language("KADC", null, 1, null))
                    newdata.add(Language("LADC", null, 1, null))
                    newdata.add(Language("MADC", null, 1, null))
                    newdata.add(Language("ZADC", null, 1, null))
                    newdata.add(Language("VADC", null, 1, null))
                    newdata.add(Language("FADC", null, 1, null))
                    newdata.add(Language("ADC", null, 1, null))
                    newdata.add(Language("BADC", null, 1, null))
                    newdata.add(Language("VADC", null, 1, null))
                    newdata.add(Language("AADC", null, 1, null))
                    newdata.add(Language("EADC", null, 1, null))
                    newdata.add(Language("RADC", null, 1, null))
                    newdata.add(Language("FADC", null, 1, null))
                    newdata.add(Language("GADC", null, 1, null))
                    newdata.add(Language("GADC", null, 1, null))
                    newdata.add(Language("AEDC", null, 1, null))
                            Collections.sort(newdata, PinyinComparator())
                            sourceDateList.addAll(newdata)
                            adapter.updateList(newdata)
                        }
                val header = LayoutInflater.from(this).inflate(R.layout.item_flow_layout, rec_languages, false) as FlowLayout
                for (i in 0..4) {
                    val inflater = LayoutInflater.from(this)
                    val view = inflater.inflate(R.layout.item_flow, header, false)

                    view.setOnClickListener { it ->
                        //setResult(EditTeacherInfoActivity.SELECT_LANGUAGE, fromIntent.putExtra("language", (it as TextView).text))
                        finish()
                    }
                    header.addView(view)
                }
                adapter.setHeaderView(header)
            }
            CITY->{
                val header = LayoutInflater.from(this).inflate(R.layout.item_city_title, rec_languages, false) as TextView
                adapter.setHeaderView(header)
            }
            NATIONALITY->{}
        }


    }

    companion object {
        const val CITY="city"
        const val LANGUAGE="language"
        const val NATIONALITY="nationality"
    }

}
