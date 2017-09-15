package cn.vko.ring.study.widget;

import cn.vko.ring.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by JiaRH on 2015/12/9.
 */
public class ProgressBarView extends View {

   /*线宽的宽度*/
	private static final int stroke_with = 1;
	/*小旗*/
	Bitmap bp;
    /*总大小*/
    private float max = 100;
    /*当前进度*/
    private float currentPosition = 0;
    /*背景框*/
    private RectF rectF;
    /*实体*/
    private RectF solidRectF;
    /*进度条高度*/
    private int pbHeight = 15;
    /*圆弧半径*/
    private int rx = 5;
    private Paint mPaint;
    /*边框色*/
    private int colorBorder = Color.parseColor("#d9d9d9");
    /*背景色*/
    private int bgColor = Color.parseColor("#61C3F5");
    private int paddingLeft, paddingRight;
    private float bpLeft;
    private float height,width;

    public ProgressBarView(Context context) {
        this(context, null);
    }

    public ProgressBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bp = BitmapFactory.decodeResource(getResources(), R.drawable.small_red_flag);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(stroke_with);
        mPaint.setAntiAlias(true);//抗锯齿功能
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        height=getMeasuredHeight();
        rectF = new RectF(paddingLeft, height - pbHeight, getMeasuredWidth() - paddingLeft - paddingRight, height);
        bpLeft = currentPosition / max * rectF.width();
        solidRectF = new RectF(paddingLeft, height - pbHeight+1, bpLeft, height+1);        
  
        drawBg(canvas);               
 
        canvas.drawBitmap(bp, bpLeft - rx>paddingLeft? bpLeft - rx:paddingLeft, 0, mPaint);
    }

    private void drawBg(Canvas canvas) {
        mPaint.setColor(colorBorder);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF, rx, rx, mPaint);
        mPaint.setColor(bgColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(solidRectF, rx, rx, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, bp.getHeight());
        /*paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        height=getMeasuredHeight();
        rectF = new RectF(paddingLeft, height - pbHeight, getMeasuredWidth() - paddingLeft - paddingRight, height);
        bpLeft = currentPosition / max * rectF.width();
        solidRectF = new RectF(paddingLeft+1, height - pbHeight+1, bpLeft, height+1);*/
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        
    }

    public void setBp(Bitmap bp) {
        this.bp = bp;
    }


    public void setMax(float max) {
        this.max = max;
    }

    public void setCurrentPosition(float currentPosition) {
        this.currentPosition = currentPosition;
        if(currentPosition>100){
        	currentPosition=100;
        }
        if (currentPosition == 0){
        	 currentPosition = 1;
        }         
        postInvalidate();
    }

    public void setColorBorder(int colorBorder) {
        this.colorBorder = colorBorder;
    }


    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }


    public void setRx(int rx) {
        this.rx = rx;
    }
}
