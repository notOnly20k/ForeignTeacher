package com.xld.foreignteacher.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xld.foreignteacher.R;


/**
 * Created by lmw on 2018/3/23.
 */

public class LinePointView extends View {
    private boolean pointLeft;
    private float radius = 6;
    public LinePointView(Context context) {
        this(context,null);
    }

    public LinePointView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public LinePointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinePointView);
        pointLeft = typedArray.getBoolean(R.styleable.LinePointView_pointLeft,false);
        Log.d("LinePointView", "pointLeft:" + pointLeft);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffcccccc);
        paint.setStrokeWidth(2);
        canvas.drawLine(radius,radius,getWidth()-radius,radius,paint);
        paint.setStrokeWidth(20);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        Log.d("LinePointViewdraw", "pointLeft:" + pointLeft);
        if (pointLeft)
            canvas.drawCircle(radius,radius,radius,paint);
        else
            canvas.drawCircle(getWidth()-radius,radius,radius,paint);
    }
}
