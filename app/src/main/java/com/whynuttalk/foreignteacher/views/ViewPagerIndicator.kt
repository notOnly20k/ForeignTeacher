package com.whynuttalk.foreignteacher.views

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.whynuttalk.foreignteacher.R
import com.youth.banner.Banner

/**
 * Created by cz on 3/29/18.
 */
class ViewPagerIndicator(context: Context, private val container: LinearLayout, private val normalRes: Int, private val selectedRes: Int, private val count: Int) {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    /**
     * 必须在viewpager获取数据之后再设置关联
     *
     * @param viewPager
     */
    fun setupWithViewPager(viewPager: ViewPager) {
        container.removeAllViews()
        for (i in 0 until count) {
            val view = inflater.inflate(R.layout.indicator_viewpager_dot, container, false)
            if (i == 0) {
                view.setBackgroundResource(R.color.yellow_ffcc00)
            } else
                view.setBackgroundResource(R.color.white)
            container.addView(view)
        }
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until count) {
                    val view = container.getChildAt(i).findViewById<View>(R.id.image_indicator)
                    if (i == position % count) {
                        view.setBackgroundResource(R.color.yellow_ffcc00)
                    } else
                        view.setBackgroundResource(R.color.white)
                }
            }
        })
    }

    fun setupWithBanner(banner: Banner) {
        if (count < 2)
            return
        container.removeAllViews()
        for (i in 0 until count) {
            val indicator = inflater.inflate(R.layout.indicator_banner, container, false) as ImageView
            if (i == 0) {
                indicator.setImageResource(selectedRes)
            } else
                indicator.setImageResource(normalRes)
            container.addView(indicator)
        }
        banner.setOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until count) {
                    val imageView = container.getChildAt(i) as ImageView
                    if (i == position % count) {
                        imageView.setImageResource(selectedRes)
                    } else
                        imageView.setImageResource(normalRes)
                }
            }
        })
    }
}
