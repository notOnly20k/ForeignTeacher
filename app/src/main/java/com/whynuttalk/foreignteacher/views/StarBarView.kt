package com.whynuttalk.foreignteacher.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.whynuttalk.foreignteacher.R

/**
 * Created by cz on 4/1/18.
 */
class StarBarView @JvmOverloads constructor( context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {
    //实心图片
    private val mSolidBitmap: Bitmap?
    //空心图片
    private val mHollowBitmap: Bitmap?
    //最大的数量
    private val starMaxNumber: Int
    private var starRating: Float = 0.toFloat()
    private val paint: Paint
    private val mSpaceWidth: Int//星星间隔
    private val mStarWidth: Int//星星宽度
    private val mStarHeight: Int//星星高度
    private val isIndicator: Boolean//是否是一个指示器（用户无法进行更改）
    private val mOrientation: Int

    init {
        paint = Paint()
        val a = context.obtainStyledAttributes(attrs, R.styleable.StarBarView, defStyle, 0)
        mSpaceWidth = a.getDimensionPixelSize(R.styleable.StarBarView_space_width, 0)
        mStarWidth = a.getDimensionPixelSize(R.styleable.StarBarView_star_width, 0)
        mStarHeight = a.getDimensionPixelSize(R.styleable.StarBarView_star_height, 0)
        starMaxNumber = a.getInt(R.styleable.StarBarView_star_max, 0)
        starRating = a.getFloat(R.styleable.StarBarView_star_rating, 0f)
        mSolidBitmap = getZoomBitmap(BitmapFactory.decodeResource(context.resources, a.getResourceId(R.styleable.StarBarView_star_solid, 0)))
        mHollowBitmap = getZoomBitmap(BitmapFactory.decodeResource(context.resources, a.getResourceId(R.styleable.StarBarView_star_hollow, 0)))
        mOrientation = a.getInt(R.styleable.StarBarView_star_orientation, HORIZONTAL)
        isIndicator = a.getBoolean(R.styleable.StarBarView_star_isIndicator, false)
        a.recycle()
    }

    /**
     * 获取缩放图片
     * @param bitmap
     * @return
     */

    fun getZoomBitmap(bitmap: Bitmap): Bitmap {
        if (mStarWidth == 0 || mStarHeight == 0) {
            return bitmap
        }
        // 获得图片的宽高
        val width = bitmap.width
        val height = bitmap.height
        // 设置想要的大小
        val newWidth = mStarWidth
        val newHeight = mStarHeight
        // 计算缩放比例
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mOrientation == HORIZONTAL) {
            //判断是横向还是纵向，测量长度
            setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec))
        } else {
            setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec))
        }
    }

    private fun measureLong(measureSpec: Int): Int {
        var result: Int
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = (paddingLeft + paddingRight + (mSpaceWidth + mStarWidth) * starMaxNumber).toInt()
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    private fun measureShort(measureSpec: Int): Int {
        var result: Int
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = (mStarHeight + paddingTop + paddingBottom).toInt()
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        if (mHollowBitmap == null || mSolidBitmap == null) {
            return
        }
        //绘制实心进度
        val solidStarNum = starRating.toInt()
        //绘制实心的起点位置
        var solidStartPoint = 0
        if (mOrientation == HORIZONTAL)
            for (i in 1..solidStarNum) {
                canvas.drawBitmap(mSolidBitmap, solidStartPoint.toFloat(), 0f, paint)
                solidStartPoint = solidStartPoint + mSpaceWidth + mSolidBitmap.width
            }
        else
            for (i in 1..solidStarNum) {
                canvas.drawBitmap(mSolidBitmap, 0f, solidStartPoint.toFloat(), paint)
                solidStartPoint = solidStartPoint + mSpaceWidth + mSolidBitmap.height
            }
        //虚心开始位置
        var hollowStartPoint = solidStartPoint
        //多出的实心部分起点
        val extraSolidStarPoint = hollowStartPoint
        //虚心数量
        val hollowStarNum = starMaxNumber - solidStarNum
        if (mOrientation == HORIZONTAL)
            for (j in 1..hollowStarNum) {
                canvas.drawBitmap(mHollowBitmap, hollowStartPoint.toFloat(), 0f, paint)
                hollowStartPoint = hollowStartPoint + mSpaceWidth + mHollowBitmap.width
            }
        else
            for (j in 1..hollowStarNum) {
                canvas.drawBitmap(mHollowBitmap, 0f, hollowStartPoint.toFloat(), paint)
                hollowStartPoint = hollowStartPoint + mSpaceWidth + mHollowBitmap.width
            }
        //多出的实心长度
        val extraSolidLength = ((starRating - solidStarNum) * mHollowBitmap.width).toInt()
        val rectSrc = Rect(0, 0, extraSolidLength, mHollowBitmap.height)
        val dstF = Rect(extraSolidStarPoint, 0, extraSolidStarPoint + extraSolidLength, mHollowBitmap.height)
        canvas.drawBitmap(mSolidBitmap, rectSrc, dstF, paint)
    }

    fun setStarRating(starRating: Float) {
        this.starRating = starRating
        invalidate()
    }

    fun getSartRating(): Float {
        return starRating
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isIndicator) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> if (mOrientation == HORIZONTAL) {
                    val TotalWidth = (starMaxNumber * (mStarWidth + mSpaceWidth)).toFloat()
                    if (event.x <= TotalWidth) {
                        val newStarRating = (event.x.toInt() / (mStarWidth + mSpaceWidth) + 1).toFloat()
                        setStarRating(Math.round(newStarRating).toFloat())
                    }
                } else {
                    val TotalHeight = (starMaxNumber * (mStarHeight + mSpaceWidth)).toFloat()
                    if (event.y <= TotalHeight) {
                        val newStarRating = (event.y.toInt() / (mStarHeight + mSpaceWidth) + 1).toFloat()
                        setStarRating(Math.round(newStarRating).toFloat())
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                }
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_CANCEL -> {
                }
            }//                    float starTotalWidth = starMaxNumber * (mStarWidth + mSpaceWidth);
            //                    if (event.getX() <= starTotalWidth) {
            //                        float newStarRating = (int) event.getX() / (mStarWidth + mSpaceWidth) + 1;
            //                    setStarRating(newStarRating);
            //                    }
        }
        return super.onTouchEvent(event)
    }

    companion object {
        //星星水平排列
        val HORIZONTAL = 0
        //星星垂直排列
        val VERTICAL = 1
    }
}
