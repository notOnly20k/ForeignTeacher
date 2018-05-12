package com.whynuttalk.foreignteacher.views

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by cz on 4/16/18.
 */
class ScheduleLayoutManager : RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        detachAndScrapAttachedViews(recycler)
        for (i in 0 until itemCount) {
            val child = recycler!!.getViewForPosition(i)
            measureChildWithMargins(child, 0, 0)
            addView(child)
            val width = getDecoratedMeasuredWidth(child)
            val height = getDecoratedMeasuredHeight(child)
            layoutDecorated(child, 0, 0, width, height)
            if (i < itemCount - 1) {
                child.scaleX = 0.8f
                child.scaleY = 0.8f
            }
        }
    }
}
