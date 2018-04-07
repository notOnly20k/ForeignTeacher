package com.xld.foreignteacher.ui.userinfo

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.Language
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.userinfo.adapter.SelectAToZAdapter
import com.xld.foreignteacher.util.PinyinComparator
import com.xld.foreignteacher.views.SideNavigationView
import kotlinx.android.synthetic.main.activity_select_language.*
import java.util.*

/**
 * Created by cz on 4/5/18.
 */
class SelectLanguageActivity: BaseTranslateStatusActivity(), SelectAToZAdapter.OnItemClickListener {
    override fun onItemClick(language: Language) {
        showToast(language.eName)
    }

    private lateinit var adapter:SelectAToZAdapter

    override val contentViewResId: Int
        get() = R.layout.activity_select_language
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        adapter=SelectAToZAdapter(this, emptyList())
        rec_languages.adapter =adapter
        adapter.setOnItemClickListener(this)
        rec_languages.layoutManager=LinearLayoutManager(this)

        sn_sideBar.onNavigateListener=object :SideNavigationView.OnNavigationListener{
            override fun onNavigateTo(c: String) {
                tv_currentChar.visibility=View.VISIBLE
                tv_currentChar.text=c
                val position = adapter.getPositionForSection(c.toCharArray()[0].toInt())
                if (position != null) {
                    (rec_languages.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
                }
            }

            override fun onNavigateCancel() {
                tv_currentChar.visibility=View.GONE
            }

        }
    }

    override fun initData() {
        appComponent.netWork.getLanguage()
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .map {
                    Collections.sort(it, PinyinComparator())
                    it
                }
                .subscribe {list->
                    val newdata = mutableListOf<Language>()
                    newdata.add(Language("AC",null,1,null))
                    newdata.add(Language("BC",null,1,null))
                    newdata.add(Language("CC",null,1,null))
                    newdata.add(Language("DC",null,1,null))
                    newdata.add(Language("EC",null,1,null))
                    newdata.add(Language("FC",null,1,null))
                    newdata.add(Language("GC",null,1,null))
                    newdata.add(Language("GC",null,1,null))
                    newdata.add(Language("AC",null,1,null))
                    newdata.add(Language("GC",null,1,null))
                    newdata.add(Language("GC",null,1,null))
                    newdata.add(Language("ADC",null,1,null))
                    newdata.add(Language("ADC",null,1,null))
                    newdata.add(Language("ABC",null,1,null))
                    newdata.add(Language("AHDC",null,1,null))
                    newdata.add(Language("KADC",null,1,null))
                    newdata.add(Language("LADC",null,1,null))
                    newdata.add(Language("MADC",null,1,null))
                    newdata.add(Language("ZADC",null,1,null))
                    newdata.add(Language("VADC",null,1,null))
                    newdata.add(Language("FADC",null,1,null))
                    newdata.add(Language("ADC",null,1,null))
                    newdata.add(Language("BADC",null,1,null))
                    newdata.add(Language("VADC",null,1,null))
                    newdata.add(Language("AADC",null,1,null))
                    newdata.add(Language("EADC",null,1,null))
                    newdata.add(Language("RADC",null,1,null))
                    newdata.add(Language("FADC",null,1,null))
                    newdata.add(Language("GADC",null,1,null))
                    newdata.add(Language("GADC",null,1,null))
                    newdata.add(Language("AEDC",null,1,null))
                    Collections.sort(newdata, PinyinComparator())
                    adapter.updateList(newdata)
                }

    }

}