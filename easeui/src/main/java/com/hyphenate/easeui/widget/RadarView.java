package com.hyphenate.easeui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by lmw on 2016/10/17.
 */

public class RadarView extends View {

    private List<Double> dataList;
    private String[] titles = {"词汇", "语法", "听力", "流利度", "发音"};

    private int count;//雷达网圈数
    private float angle;//多边形弧度
    private float radius;
    private float maxValue = 100f;
    private Paint mainPaint;//雷达区画笔
    private Paint valuePaint;//数据区画笔
    private Paint textPaint;//文本画笔

    private int mainColor = 0xFF888888;//雷达区颜色
    private int valueColor = 0xFFFFCC00;//数据区颜色
    private int textColor = 0xFF333333;//文本颜色
    private int outPointColor = 0x80FFCC00;//外层圆点颜色
    private int innerPointColor = 0xFFFFCC00;//内圆点颜色
    private int outCircleColor = 0xFFF9E886;//外层圆颜色
    private int dashLineColer = 0xFFCCCCCC;//虚线颜色
    private int MidCircleColer = 0xFFD2D2D2;//虚线颜色

    private float mainLineWidth = 0.5f;//雷达网线宽度dp
    private float valuePointWidth = 2f;//数据区边宽度dp
    private float valueLineWidth = 4f;//数据区边宽度dp
    private float valuePointRadius = 5f;//数据区圆点半径dp

    private float outPointRadius = 8f;//外层圆点半径dp
    private float innerPointRadius = 4f;//内圆点半径dp

    private float textSize = 14f;//字体大小sp

    private int mWidth, mHeight;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setup();
    }

    private void setup() {
        mainPaint = new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setColor(mainColor);
        mainPaint.setStyle(Paint.Style.STROKE);

        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setColor(valueColor);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);

        count = 5;
        angle = (float) (Math.PI * 2 / count);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(h, w) / 2 * 0.6f;
        mWidth = w;
        mHeight = h;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth / 2, mHeight / 2);
        drawCircle(canvas);
        drawText(canvas);
        if (isDataListValid()) {
            drawRegion(canvas);
        }
    }

    /**
     * 绘制蜘蛛网
     *
     * @param canvas
     */
    private void drawSpiderweb(Canvas canvas) {
        mainPaint.setStrokeWidth(dip2px(getContext(), mainLineWidth));
        Path webPath = new Path();
        Path linePath = new Path();
        float r = radius / (count - 1);//蜘蛛丝之间的间距
        for (int i = 0; i < count; i++) {
            float curR = r * i;//当前半径
            webPath.reset();
            for (int j = 0; j < count; j++) {
                float x = (float) (curR * Math.sin(angle / 2 + angle * j));
                float y = (float) (curR * Math.cos(angle / 2 + angle * j));
                if (j == 0) {
                    webPath.moveTo(x, y);
                } else {
                    webPath.lineTo(x, y);
                }
                if (i == count - 1) {//当绘制最后一环时绘制连接线
                    linePath.reset();
                    linePath.moveTo(0, 0);
                    linePath.lineTo(x, y);
                    canvas.drawPath(linePath, mainPaint);
                }
            }
            webPath.close();
            canvas.drawPath(webPath, mainPaint);
        }
    }

    /**
     * 绘制同心圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        mainPaint.setStrokeWidth(dip2px(getContext(), mainLineWidth));
        Path dashPath = new Path();
        //1.最外层圆
        mainPaint.setColor(outCircleColor);
        canvas.drawCircle(0, 0, radius, mainPaint);
        //2.虚线多边形
        Paint dashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setColor(dashLineColer);
        dashPaint.setStrokeWidth(dip2px(getContext(), mainLineWidth));
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{15, 10}, 0);
        dashPaint.setPathEffect(dashPathEffect);
        for (int i = 0; i < count; i++) {
            float x = (float) (radius * Math.sin(angle / 2 + angle * i));
            float y = (float) (radius * Math.cos(angle / 2 + angle * i));
            if (i == 0) {
                dashPath.moveTo(x, y);
            } else {
                dashPath.lineTo(x, y);
            }
        }
        dashPath.close();
        canvas.drawPath(dashPath, dashPaint);
        //3.多边形角上同心圆
        Paint outPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outPaint.setStyle(Paint.Style.FILL);
        outPaint.setColor(outPointColor);
        outPaint.setAlpha(40);
        Paint innerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerPaint.setStyle(Paint.Style.FILL);
        innerPaint.setColor(innerPointColor);
        for (int i = 0; i < count; i++) {
            float x = (float) (radius * Math.sin(angle / 2 + angle * i));
            float y = (float) (radius * Math.cos(angle / 2 + angle * i));
            canvas.drawCircle(x, y, dip2px(getContext(), outPointRadius), outPaint);
            canvas.drawCircle(x, y, dip2px(getContext(), innerPointRadius), innerPaint);
        }
        //4.第二层圆
        mainPaint.setColor(MidCircleColer);
        canvas.drawCircle(0, 0, radius / 2, mainPaint);
        //5.第三层圆
        mainPaint.setColor(dashLineColer);
//        mainPaint.setPathEffect(dashPathEffect);
        canvas.drawCircle(0, 0, radius / 5, dashPaint);

    }

    /**
     * 绘制文本
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        textPaint.setTextSize(sp2px(getContext(), textSize));
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i = 0; i < count; i++) {
            float x = (float) ((radius + fontHeight * 1.5) * Math.sin(angle / 2 + angle * i));
            float y = (float) ((radius + fontHeight * 1.5) * Math.cos(angle / 2 + angle * i));
            String title = titles[i];
            float dis = textPaint.measureText(title);//文本长度
            canvas.drawText(title, x - dis / 2, y, textPaint);
        }
    }

    /**
     * 绘制区域
     *
     * @param canvas
     */
    private void drawRegion(Canvas canvas) {
        valuePaint.setStrokeWidth(dip2px(getContext(), valueLineWidth));
        Path path = new Path();
        valuePaint.setAlpha(255);
        path.reset();
        //1.画能力值多边形
        for (int i = 0; i < count; i++) {
            double percent = dataList.get(i) / maxValue;
            float x = (float) (radius * Math.sin(angle / 2 + angle * i) * percent);
            float y = (float) (radius * Math.cos(angle / 2 + angle * i) * percent);
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.close();
        //1.1绘制值边界
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, valuePaint);
        //1.2绘制填充区域
        valuePaint.setAlpha(100);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, valuePaint);
        //2.画值圆点
        valuePaint.setStrokeWidth(dip2px(getContext(), valuePointWidth));
        valuePaint.setAlpha(255);
        Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(0xFFFFFFFF);
        for (int i = 0; i < count; i++) {
            double percent = dataList.get(i) / maxValue;
            float x = (float) (radius * Math.sin(angle / 2 + angle * i) * percent);
            float y = (float) (radius * Math.cos(angle / 2 + angle * i) * percent);
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            //绘制小圆点
            canvas.drawCircle(x, y, dip2px(getContext(), valuePointRadius), valuePaint);
            canvas.drawCircle(x, y, dip2px(getContext(), valuePointRadius - 1), whitePaint);
        }
    }


    private boolean isDataListValid() {
        return dataList != null && dataList.size() >= 3;
    }


    public void setDataList(List<Double> dataList) {
        if (dataList == null || dataList.size() < 3) {
            throw new RuntimeException("The number of data can not be less than 3");
        } else {
            this.dataList = dataList;
            count = dataList.size();
            angle = (float) (Math.PI * 2 / count);
            invalidate();
        }
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
