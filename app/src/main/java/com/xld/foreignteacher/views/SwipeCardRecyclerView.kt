package com.xld.foreignteacher.views

import android.animation.Animator
import android.animation.TimeInterpolator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.support.annotation.Nullable
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.ui.schedule.adapter.ScheduleDateAdapter
import org.slf4j.LoggerFactory


/**
 * Created by cz on 4/16/18.
 */
class SwipeCardRecyclerView : RecyclerView {
    private val logger = LoggerFactory.getLogger("SwipeCardRecyclerView")
    private var mTopViewX: Float = 0.toFloat()
    private var mTopViewY: Float = 0.toFloat()
    private var deleteList = mutableListOf<View>()

    private var mTopViewOffsetX = 0f
    private var mTopViewOffsetY = 0f

    private var mTouchDownX: Float = 0.toFloat()
    private var mTouchDownY: Float = 0.toFloat()

    private val mBorder = dip2px(120f).toFloat()

    private var mRemovedListener: ItemRemovedListener? = null

    private lateinit var mDecorView: FrameLayout

    private val mDecorViewLocation = IntArray(2)


    private lateinit var mAnimatorMap: MutableMap<View, Animator>

    private val screenWidth: Float
        get() = context.resources.displayMetrics.widthPixels.toFloat()


    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView()
    }

    private fun initView() {
        mDecorView = (context as Activity).window.decorView as FrameLayout
        mDecorView.getLocationOnScreen(mDecorViewLocation)
        mAnimatorMap = HashMap()
    }

    fun setRemovedListener(listener: ItemRemovedListener) {
        mRemovedListener = listener
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onTouchEvent(e: MotionEvent): Boolean {
        logger.e { "touch" }
        if (childCount == 0) {
            return super.onTouchEvent(e)
        }
        val topView = getChildAt(childCount - 1)
        val touchX = e.x
        val touchY = e.y
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                if (mAnimatorMap.containsKey(topView)) {
                    mAnimatorMap[topView]?.cancel()
                    mAnimatorMap.remove(topView)
                    mTopViewOffsetX = topView.x
                    mTopViewOffsetY = topView.y
                } else {
                    mTopViewX = topView.x
                    mTopViewY = topView.y
                    mTopViewOffsetX = 0F
                    mTopViewOffsetY = 0F
                }
                mTouchDownX = touchX
                mTouchDownY = touchY
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = touchX - mTouchDownX
                val dy = touchY - mTouchDownY
                topView.x = mTopViewX + dx + mTopViewOffsetX
                topView.y = mTopViewY + dy + mTopViewOffsetY
                updateNextItem(Math.abs(topView.x - mTopViewX) * 0.2 / mBorder + 0.8)
            }
            MotionEvent.ACTION_UP -> {
                mTouchDownX = 0F
                mTouchDownY = 0F
                touchUp(topView)
            }
        }
        return super.onTouchEvent(e)
    }

    /**
     * 更新下一个View的宽高
     *
     * @param factor
     */
    private fun updateNextItem(factor: Double) {
        var factor = factor
        if (childCount < 2) {
            return
        }
        if (factor > 1) {
            factor = 1.0
        }
        val nextView = getChildAt(childCount - 2)
        nextView.scaleX = factor.toFloat()
        nextView.scaleY = factor.toFloat()
    }

    /**
     * 手指抬起时触发动画
     *
     * @param view
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun touchUp(view: View) {
        var targetX = 0f
        var targetY = 0f
        var del = false
        when {
            Math.abs(view.x - mTopViewX) < mBorder -> {
                targetX = mTopViewX
                targetY = mTopViewY
            }
            view.x - mTopViewX > mBorder -> {

                del = false
//                if (deleteList.isNotEmpty()) {
//                    mDecorView.addView(deleteList.last(), 0)
//                }
                targetX = screenWidth * 2
                mRemovedListener?.onRightRemoved()
            }
            else -> {
                deleteList.add(view)
                del = true
                targetX = -view.width - screenWidth
                mRemovedListener?.onLeftRemoved()
            }
        }
        var animView = view
        val interpolator: TimeInterpolator
        if (del) {
            animView = getMirrorView(view)
            val offsetX = x - mDecorView.x
            val offsetY = y - mDecorView.y
            targetY = caculateExitY(mTopViewX + offsetX, mTopViewY + offsetY, animView.x, animView.y, targetX)
            interpolator = LinearInterpolator()
        } else {
            interpolator = OvershootInterpolator()
        }
        val finalDel = del
        val finalAnimView = animView
        animView.animate()
                .setDuration(500)
                .x(targetX)
                .y(targetY)
                .setInterpolator(interpolator)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        if (!finalDel) {
                            mAnimatorMap.put(finalAnimView, animation)
                        }
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        if (finalDel) {
                            try {
                                mDecorView.removeView(finalAnimView)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        } else {
                            mAnimatorMap.remove(finalAnimView)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })
                .setUpdateListener {
                    if (!finalDel) {
                        updateNextItem(Math.abs(view.x - mTopViewX) * 0.2 / mBorder + 0.8)
                    }
                }

    }

    private fun dip2px(dip: Float): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dip, context.resources.displayMetrics
        ).toInt()
    }

    /**
     * 计算View退场时的纵坐标，简单的线性计算
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @return
     */
    private fun caculateExitY(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float): Float {
        return (y2 - y1) * (x3 - x1) / (x2 - x1) + y1
    }




    /**
     * 创建镜像View替代原有顶部View展示删除动画
     *
     * @param view
     * @return
     */
    private fun getMirrorView(view: View): ImageView {
        view.destroyDrawingCache()
        view.isDrawingCacheEnabled = true
        val mirrorView = ImageView(context)
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        mirrorView.setImageBitmap(bitmap)
        view.isDrawingCacheEnabled = false
        val params = FrameLayout.LayoutParams(bitmap.width, bitmap.height)
        val locations = IntArray(2)
        view.getLocationOnScreen(locations)

        mirrorView.alpha = view.getAlpha()
        view.visibility = GONE
        (adapter as ScheduleDateAdapter).delTopItem()
        mirrorView.x = (locations[0] - mDecorViewLocation[0]).toFloat()
        mirrorView.y = (locations[1] - mDecorViewLocation[1]).toFloat()
        mDecorView.addView(mirrorView, params)
        return mirrorView
    }

    interface ItemRemovedListener {
        fun onRightRemoved()

        fun onLeftRemoved()
    }
}
