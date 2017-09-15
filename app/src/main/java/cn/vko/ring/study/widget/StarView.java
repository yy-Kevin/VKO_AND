package cn.vko.ring.study.widget;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by JiaRH on 2015/10/15.
 */
public class StarView extends View {
	private Paint mPaint;
	private int startX, startY;
	private Bitmap leftBitMap, rightBitMap;
	private int rightRectColor = Color.parseColor("#979797");
	private int textCorlor = Color.parseColor("#000099");
	private RectF mRectF;
	private String text = "0";
	private int marginLeftIconTopRate = 12;
	private int fx = 10;
	private int fy = 10;
	private int textSize = 18;
	private int density;

	public StarView(Context context) {
		this(context, null);
	}
	public StarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public StarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		density = (int)(ViewUtils.getScreenDensity(context)+0.5);
		initAttrbutes(context, attrs);
		initPaint();
	}
	private void initPaint() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
	}
	private void initAttrbutes(Context context, AttributeSet attrs) {
		TypedArray a = getResources().obtainAttributes(attrs, R.styleable.StarView);
		leftBitMap = BitmapFactory.decodeResource(getResources(),
				a.getResourceId(R.styleable.StarView_leftIcon, R.drawable.my_ico_star));
		text = a.getString(R.styleable.StarView_textContent);
		marginLeftIconTopRate = a.getInt(R.styleable.StarView_marginTopLeftIconRate, density>2?12*density:12);
		textSize = a.getInt(R.styleable.StarView_textStarSize, density>2?10*density:20);
		if (density>2&&density<4) {
			fx=19;
			fy=19;
		}
		fx = a.getInt(R.styleable.StarView_textbgX, fx)*(density>2?(density>3?density:1):1);
		fy = a.getInt(R.styleable.StarView_textbgY, fy)*(density>2?(density>3?density:1):1);

		textCorlor = a.getColor(R.styleable.StarView_textStarColor, textCorlor);
		rightRectColor = a.getColor(R.styleable.StarView_textBgColor, rightRectColor);
		a.recycle();
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int withMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int withSize = MeasureSpec.getSize(widthMeasureSpec);
		int heithSize = MeasureSpec.getSize(heightMeasureSpec);
		int with = withSize - getPaddingLeft() - getPaddingRight();
		int height = heithSize - getPaddingBottom() - getPaddingTop();
		if (withMode == MeasureSpec.AT_MOST) {
			with = Math.min(with, (int) (leftBitMap.getWidth() * 2.5));
		}
		if (heightMode == MeasureSpec.AT_MOST) {
			height = Math.min(height, leftBitMap.getHeight());
		}
		setMeasuredDimension(with, height);
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawRectF(canvas);
		drawText(canvas);
		drawLeftIcon(canvas);
	}
	private void drawLeftIcon(Canvas canvas) {
		// canvas.drawBitmap(leftBitMap, getWidth() / 2 - leftBitMap.getWidth() / 4, getHeight() / 2
		// - leftBitMap.getHeight() / 2, mPaint);
		canvas.drawBitmap(leftBitMap, 0, getHeight() / 2 - leftBitMap.getHeight() / 2, mPaint);
	}
	private void drawText(Canvas canvas) {
		mPaint.setTextSize(textSize);
		mPaint.setColor(textCorlor);
		Paint.FontMetrics fm = mPaint.getFontMetrics();
		int textHeight = (int) (Math.ceil(fm.descent - fm.ascent + 2));
		canvas.drawText(text, mRectF.left + mRectF.width() * 3 / 5, getHeight() / 2 + textHeight / 4, mPaint);
	}
	private void drawRectF(Canvas canvas) {
		int dy = leftBitMap.getHeight() / marginLeftIconTopRate;
		mRectF = new RectF(0, getHeight() / 2 - (leftBitMap.getHeight() / 2 - dy), leftBitMap.getWidth() * 2.5f,
				getHeight() / 2 + (leftBitMap.getHeight() / 2 - dy));
		// mRectF = new RectF(getWidth() / 2, getHeight() / 2 - (leftBitMap.getHeight() / 2 - dy),
		// getWidth() / 2 + leftBitMap.getWidth() * 2.5f, getHeight() / 2 + (leftBitMap.getHeight()
		// / 2 - dy));
		mPaint.setColor(rightRectColor);
		canvas.drawRoundRect(mRectF, fx, fy, mPaint);
	}
	public void setRightRectColor(int rightRectColor) {
		this.rightRectColor = rightRectColor;
	}
	public void setTextCorlor(int textCorlor) {
		this.textCorlor = textCorlor;
	}
	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
	public void setMarginLeftIconTopRate(int marginLeftIconTopRate) {
		this.marginLeftIconTopRate = marginLeftIconTopRate;
	}
	public void setFx(int fx) {
		this.fx = fx;
	}
	public void setFy(int fy) {
		this.fy = fy;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
