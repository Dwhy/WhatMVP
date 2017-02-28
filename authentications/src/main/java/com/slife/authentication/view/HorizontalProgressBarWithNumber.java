package com.slife.authentication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.slife.authentication.R;

public class HorizontalProgressBarWithNumber extends ProgressBar {
	private static final int DEFAULT_TEXT_SIZE=10;
	private static final int DEFAULT_TEXT_COLOR=0xFF55A0FF;
	private static final int DEFAULT_COLOR_UNREACHED_COLOR=0xFFd3d6da;
	private static final int DEFAULT_HEIGHT_REACHED_PROGRESS_BAR=2;
	private static final int DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR=2;
	private static final int DEFAULT_SIZE_TEXT_OFFSET=10;


	/**画笔*/
	protected Paint mPaint=new Paint();
	/**进度条数字颜色*/
	protected int mTextColor=DEFAULT_TEXT_COLOR;
	/**字体大小*/
	protected int mTextSize=sp2px(DEFAULT_TEXT_SIZE);
	/**绘制进度偏移*/
	protected int mTextOffset=dp2px(DEFAULT_SIZE_TEXT_OFFSET);
	/**到达的进度条高度*/
	protected int mReachedProgressBarHeight=dp2px(DEFAULT_HEIGHT_REACHED_PROGRESS_BAR);
	/**
	 * 到达了的颜色
	 */
	protected int mReachedBarColor = DEFAULT_TEXT_COLOR;
	/**
	 * 未到达的颜色
	 */
	protected int mUnReachedBarColor = DEFAULT_COLOR_UNREACHED_COLOR;
	/**
	 * 未到达的高度
	 */
	protected int mUnReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR);
	/**
	 * 填充除了视图的宽度
	 */
	protected int mRealWidth;

	protected boolean mIfDrawText = true;

	protected static final int VISIBLE = 0;

	public HorizontalProgressBarWithNumber(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	public HorizontalProgressBarWithNumber(Context context, AttributeSet attrs,
										   int defStyle) {
		super(context, attrs, defStyle);
		obtainStyledAttributes(attrs);
		mPaint.setTextSize(mTextSize);
		mPaint.setColor(mTextColor);

	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
										  int heightMeasureSpec)
	{

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec);
		setMeasuredDimension(width, height);

		mRealWidth = getMeasuredWidth() - getPaddingRight() - getPaddingLeft();
	}

	private int measureHeight(int measureSpec)
	{
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.EXACTLY)
		{
			result = specSize;
		} else
		{
			float textHeight = (mPaint.descent() - mPaint.ascent());
			result = (int) (getPaddingTop() + getPaddingBottom() + Math.max(
					Math.max(mReachedProgressBarHeight,
							mUnReachedProgressBarHeight), Math.abs(textHeight)));
			if (specMode == MeasureSpec.AT_MOST)
			{
				result = Math.min(result, specSize);
			}
		}
		return result;
	}
	/**
	 * get the styled attributes
	 *
	 * @param attrs
	 */
	private void obtainStyledAttributes(AttributeSet attrs)
	{
		// init values from custom attributes
		final TypedArray attributes = getContext().obtainStyledAttributes(
				attrs, R.styleable.HorizontalProgressBarWithNumber);

		mTextColor = attributes
				.getColor(
						R.styleable.HorizontalProgressBarWithNumber_progress_text_color,
						DEFAULT_TEXT_COLOR);
		mTextSize = (int) attributes.getDimension(
				R.styleable.HorizontalProgressBarWithNumber_progress_text_size,
				mTextSize);

		mReachedBarColor = attributes
				.getColor(
						R.styleable.HorizontalProgressBarWithNumber_progress_reached_color,
						mTextColor);
		mUnReachedBarColor = attributes
				.getColor(
						R.styleable.HorizontalProgressBarWithNumber_progress_unreached_color,
						DEFAULT_COLOR_UNREACHED_COLOR);
		mReachedProgressBarHeight = (int) attributes
				.getDimension(
						R.styleable.HorizontalProgressBarWithNumber_progress_reached_bar_height,
						mReachedProgressBarHeight);
		mUnReachedProgressBarHeight = (int) attributes
				.getDimension(
						R.styleable.HorizontalProgressBarWithNumber_progress_unreached_bar_height,
						mUnReachedProgressBarHeight);
		mTextOffset = (int) attributes
				.getDimension(
						R.styleable.HorizontalProgressBarWithNumber_progress_text_offset,
						mTextOffset);

		int textVisible = attributes
				.getInt(R.styleable.HorizontalProgressBarWithNumber_progress_text_visibility,
						VISIBLE);
		if (textVisible != VISIBLE)
		{
			mIfDrawText = false;
		}
		attributes.recycle();
	}
	@Override
	protected synchronized void onDraw(Canvas canvas)
	{

		canvas.save();
		canvas.translate(getPaddingLeft(), getHeight() / 2);

		boolean noNeedBg = false;
		float radio = getProgress() * 1.0f / getMax();
		float progressPosX = (int) (mRealWidth * radio);
		String text = getProgress() + "%";
		// mPaint.getTextBounds(text, 0, text.length(), mTextBound);

		float textWidth = mPaint.measureText(text);
		float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

		if (progressPosX + textWidth > mRealWidth)
		{
			progressPosX = mRealWidth - textWidth;
			noNeedBg = true;
		}

		// draw reached bar
		float endX = progressPosX - mTextOffset / 2;
		if (endX > 0)
		{
			mPaint.setColor(mReachedBarColor);
			mPaint.setStrokeWidth(mReachedProgressBarHeight);
			canvas.drawLine(0, 0, endX, 0, mPaint);
		}
		// draw progress bar
		// measure text bound
		if (mIfDrawText)
		{
			mPaint.setColor(mTextColor);
			canvas.drawText(text, progressPosX, -textHeight, mPaint);
		}

		// draw unreached bar
		if (!noNeedBg)
		{
			float start = progressPosX + mTextOffset / 2 + textWidth;
			mPaint.setColor(mUnReachedBarColor);
			mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
			canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
		}

		canvas.restore();

	}
	/**
	 * dp转Px
	 *
	 * @param dpVal
	 */
	protected int dp2px(int dpVal)
	{
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, getResources().getDisplayMetrics());
	}

	/**
	 * sp转Px
	 *
	 * @param spVal
	 * @return
	 */
	protected int sp2px(int spVal)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, getResources().getDisplayMetrics());

	}
}
