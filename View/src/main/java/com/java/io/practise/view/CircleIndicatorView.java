package com.java.io.practise.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.view.ViewPager;

import com.java.io.practise.R;
import com.java.io.practise.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huguojin on 2017/6/15.
 */

public class CircleIndicatorView extends View implements ViewPager.OnPageChangeListener {

    private Paint mCirclePaint;
    private Paint mTextPaint;
    private
    @ColorInt
    int mDotNormalColor;
    private
    @ColorInt
    int mSelectedColor;
    private
    @ColorInt
    int mTextColor;
    private int mRadius; // 半径
    private int mStrokWidth; // border
    private int mSpace = 0; // 圆点间的距离
    private boolean mIsEnableClickSwitch = false; // 是否允许点击切换
    private int mFillMode = NONE;// 默认小圆点

    private List<Indicator> mIndicators; // indicator 数组
    private int mCount;// indicator的数量
    private int mSelectedPosition;//选中的位置

    private OnIndicatorSelectListener mListener;
    private ViewPager mViewPager;

    public static final int NONE = 0;
    public static final int NUMBER = 1;
    public static final int LETTER = 2;

    @IntDef({NONE, NUMBER, LETTER})
    public @interface FillMode {
    }

    private class Indicator {
        float cx;
        float cy;
    }

    interface OnIndicatorSelectListener {
        void onSelected(int position);
    }

    public CircleIndicatorView(Context context) {
        this(context, null);
    }

    public CircleIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyAttrs(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyAttrs(context, attrs);
        init();
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setDither(true);// 抗抖动
        mCirclePaint.setAntiAlias(true);// 抗锯齿
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);// 设置style

        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);

        // 默认值
        mIndicators = new ArrayList<>();

        setupPaint();
    }

    private void setupPaint() {
        mCirclePaint.setColor(mDotNormalColor);
        mCirclePaint.setStrokeWidth(mStrokWidth);

        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mRadius);
    }

    private void applyAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicatorView);
        // dimension
        mRadius = (int) ta.getDimension(R.styleable.CircleIndicatorView_indicatorRadius, DisplayUtils.dpToPx(6));
        mStrokWidth = (int) ta.getDimension(R.styleable.CircleIndicatorView_indicatorBorderwidth, DisplayUtils.dpToPx(2));
        mSpace = (int) ta.getDimension(R.styleable.CircleIndicatorView_indicatorSpace, DisplayUtils.dpToPx(5));

        // color
        mTextColor = ta.getColor(R.styleable.CircleIndicatorView_indicatorTextColor, Color.BLACK);
        mSelectedColor = ta.getColor(R.styleable.CircleIndicatorView_indicatorSelectColor, Color.WHITE);
        mDotNormalColor = ta.getColor(R.styleable.CircleIndicatorView_indicatorColor, Color.GRAY);

        mIsEnableClickSwitch = ta.getBoolean(R.styleable.CircleIndicatorView_enableIndicatorSwitch, false);
        mFillMode = ta.getInt(R.styleable.CircleIndicatorView_fill_mode, 2);
        ta.recycle();
    }

    /**
     * 测量每个圆点的位置
     */
    private void measureIndicators() {
        mIndicators.clear();
        float cx = 0.0f;
        for (int i = 0; i < mCount; i++) {
            Indicator indicator = new Indicator();
            if (i == 0) {
                cx = mRadius + mStrokWidth;// 第一个点横坐标
            } else {
                cx += (mRadius + mStrokWidth) * 2 + mSpace;//后续点坐标加上间隔
            }
            indicator.cx = cx;
            indicator.cy = getMeasuredHeight() / 2;

            mIndicators.add(indicator);
        }
    }

    private String getRightText(int index) {
        if (mFillMode == LETTER) {
            return String.valueOf((char) (index + 65));
        } else {
            return String.valueOf(++index);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (mRadius + mStrokWidth) * mCount * 2 + mSpace * (mCount - 1);
        int height = mRadius * 2 + mSpace * 2;

        setMeasuredDimension(width, height);

        measureIndicators();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setupPaint();
        for (int i = 0; i < mIndicators.size(); i++) {
            // 画圆形
            Indicator indicator = mIndicators.get(i);
            float x = indicator.cx;
            float y = indicator.cy;
            if (mSelectedPosition == i) {
                mCirclePaint.setStyle(Paint.Style.FILL);
                mCirclePaint.setColor(mSelectedColor);
            } else {
                mCirclePaint.setColor(mDotNormalColor);
                if (mFillMode != NONE) {
                    mCirclePaint.setStyle(Paint.Style.STROKE);
                } else {
                    mCirclePaint.setStyle(Paint.Style.FILL);
                }
            }
            canvas.drawCircle(x, y, mRadius, mCirclePaint);
            //画文字
            if (mFillMode != NONE) {
                String text = getRightText(i);
                Rect bound = new Rect();
                mTextPaint.getTextBounds(text, 0, text.length(), bound);
                int width = bound.width();
                int height = bound.height();

                float textStartX = x - width / 2;
                float textStartY = y + height / 2;
                canvas.drawText(text, textStartX, textStartY, mTextPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPoint;
        float yPoint;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xPoint = event.getX();
                yPoint = event.getY();
                handleActionDown(xPoint,yPoint);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void handleActionDown(float x, float y) {
        for (int i = 0; i < mIndicators.size(); i++) {
            Indicator indicator = mIndicators.get(i);
            if (x < (indicator.cx + mRadius + mStrokWidth)
                    && x >= (indicator.cx -(mRadius + mStrokWidth))
                    && y >= (y-(indicator.cy + mStrokWidth))
                    && y < (indicator.cy + mRadius + mStrokWidth)) {
                if (mIsEnableClickSwitch) {
                    mViewPager.setCurrentItem(i, false);
                }

                if (null != mListener) {
                    mListener.onSelected(i);
                }
                break;
            }
        }
    }

    public void setOnIndicatorSelectListener(OnIndicatorSelectListener listener) {
        mListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mSelectedPosition = position;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 与viewpager关联
     * @param viewPager
     */
    public void setupWithViewPager(ViewPager viewPager) {
        releaseViewPager();
        if (null == viewPager) {
            return;
        }
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        int count = mViewPager.getAdapter().getCount();
        setCount(count);
    }

    /**
     * 重置viewpager
     */
    public void releaseViewPager() {
        if (null != mViewPager) {
            mViewPager.removeOnPageChangeListener(this);
            mViewPager = null;
        }
    }

    public void setCount(int count) {
        this.mCount = count;
        invalidate();
    }

    public int getDotNormalColor() {
        return mDotNormalColor;
    }

    public void setDotNormalColor(int dotNormalColor) {
        this.mDotNormalColor = dotNormalColor;
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.mSelectedColor = selectedColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
        invalidate();
    }

    public int getStrokWidth() {
        return mStrokWidth;
    }

    public void setStrokWidth(int strokWidth) {
        this.mStrokWidth = strokWidth;
        invalidate();
    }

    public int getSpace() {
        return mSpace;
    }

    public void setSpace(int space) {
        this.mSpace = space;
        invalidate();
    }

    public boolean isEnableClickSwitch() {
        return mIsEnableClickSwitch;
    }

    public void setIsEnableClickSwitch(boolean isEnableClickSwitch) {
        this.mIsEnableClickSwitch = isEnableClickSwitch;
    }

    public int getFillMode() {
        return mFillMode;
    }

    public void setFillMode(int fillMode) {
        this.mFillMode = fillMode;
    }

    public List<Indicator> getIndicators() {
        return mIndicators;
    }

    public void setIndicators(List<Indicator> indicators) {
        this.mIndicators = indicators;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }
}
