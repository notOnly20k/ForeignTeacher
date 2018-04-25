package com.xld.foreignteacher.ui.userinfo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView
import com.xld.foreignteacher.R
import java.util.*

/**
 * Created by cz on 4/2/18.
 */
class StudentPageAdapter(context: Context, private val urls: MutableList<String>) : android.support.v4.view.PagerAdapter() {
    private val imgViews: MutableList<SimpleDraweeView>


    init {
        imgViews = ArrayList()
        val layoutInflater = LayoutInflater.from(context)
        for (i in urls.indices) {
            val view = layoutInflater.inflate(R.layout.item_user_photos, null) as SimpleDraweeView
            imgViews.add(view)
        }
    }

    override fun getCount(): Int {
        return urls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = imgViews[position]
        view.setImageURI(urls[position])
        container.addView(view)
        return view
    }

    fun update(list: List<String>){
        urls.clear()
        urls.addAll(list)
        notifyDataSetChanged()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(imgViews[position])
    }
}
