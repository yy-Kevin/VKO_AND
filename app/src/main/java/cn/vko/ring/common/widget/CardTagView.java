package cn.vko.ring.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * desc:标签
 * Created by jiarh on 16/3/24 09:54.
 */
public class CardTagView extends View {


    /**
     * 设置背景高度 设置宽度
     */
    private int height,width;
    
    /**
     * 设置文本 
     */
    private String text = "";
    /**
     * 背景颜色
     */
    private int bgColor = Color.parseColor("#2196F3");
    /**
     * 文本颜色
     */
    private int textColor = Color.parseColor("#ffffff");
    /**
     * 文本大小
     */
    private float textSize = 13;
   
    /**
     * 设置底下三角形的百分比
     */
    private float percent=0.2f;

    /*背景*/
    private Paint mPaint;
    /*文字*/
    private Paint mPaintText;
    private Path path;
    private Paint.FontMetrics fm;
    private Rect mTextBound;
    private float density;


    public CardTagView(Context context) {
        this(context, null);
    }

    public CardTagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getDensity();
//        BG_HEIGHT = BG_HEIGHT*density;
//        PADING = PADING*density;
        initPaint();

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mPaintText = new Paint();
//      mPaintText.setAntiAlias(true);
//      mPaintText.setTextAlign(Paint.Align.CENTER);
        mTextBound = new Rect(); 
        mPaintText.setTextSize(textSize*density);
        // 计算了描绘字体需要的范围  
        mPaintText.getTextBounds(text, 0, text.length(), mTextBound);
        path = new Path();

    }

    private float getDensity(){
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        density = metrics.density;
        return density;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	// TODO Auto-generated method stub
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
        // 计算了描绘字体需要的范围  
        mPaintText.getTextBounds(text, 0, text.length(), mTextBound);
         if (widthMode == MeasureSpec.EXACTLY) {  
             // Parent has told us how big to be. So be it.  
            width = widthSize;  
         } else {  
            width = getPaddingLeft() + getPaddingRight() + mTextBound.width();
            
         }
       if(heightMode == MeasureSpec.EXACTLY){
    	   height = heightSize;
       }else{
    	   height = (int) ((getPaddingTop()+getPaddingBottom()+mTextBound.height())*(1+percent));
       }
       setMeasuredDimension(width, height);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawBg(canvas);
        onDrawText(canvas);
//        canvas.drawText(text, textCenterX,textCenterVerticalBaselineY,mPaintText);

    }

    private void onDrawText(Canvas canvas) {
        if (TextUtils.isEmpty(text))
            return;
        mPaintText.setColor(textColor);
        fm = mPaintText.getFontMetrics();
        float textY = getTextHeight() / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
        /** 
         * 当前设置的宽度小于字体需要的宽度，将字体改为xxx... 
         */  
        if (mTextBound.width() > width)  
        {  
            TextPaint paint = new TextPaint(mPaintText);  
            String msg = TextUtils.ellipsize(text, paint, (float) width - getPaddingLeft() - getPaddingRight(),  
                    TextUtils.TruncateAt.END).toString();  
            canvas.drawText(msg, getPaddingLeft(), textY, mPaintText);  
        } else  
        {  
            //正常情况，将字体居中  
            canvas.drawText(text, width / 2 - mTextBound.width() * 1.0f / 2, textY, mPaintText);  
        }  
        
    }

    private float getTextHeight() {
        return height * (1-percent);
    }

    private void onDrawBg(Canvas canvas) {
        mPaint.setColor(bgColor);
        path.reset();
        path.moveTo(0, 0);
        path.lineTo(0, height);
        path.lineTo(width / 2, getTextHeight());
        path.lineTo(width, height);
        path.lineTo(width, 0);
        path.close();
        canvas.drawPath(path, mPaint);
    }


    public void setTextAndColor(String text,int bgColor) {
        this.text = text;
        this.bgColor = bgColor;
        invalidate();
    }


    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setPercent(float percent) {
        this.percent = percent;
        invalidate();
    }
}
