package com.xld.foreignteacher.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * Created by cz on 4/16/18.
 */
class ScollerRecyclerView @JvmOverloads constructor(context : Context,
                                                    attrs : AttributeSet? = null,
                                                    defStyleAttr : Int = 0): RecyclerView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2,
                View.MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, heightSpec)
    }
}