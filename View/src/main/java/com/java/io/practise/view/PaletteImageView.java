package com.java.io.practise.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.java.io.practise.R;
import com.java.io.practise.utils.DisplayUtils;

/**
 * Created by huguojin on 2017/7/11.
 */

public class PaletteImageView extends View implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "PaletteImageView";
    private float mRadius;
    private int mImgId;
    private int mPadding;
    private int mOffsetX;
    private int mOffsetY;
    private int mShadowRadius;

    private Paint mShadowPaint;
    private Paint mRoundPaint;

    private PorterDuffXfermode mPorterDuffXfermode;

    private static final int DEFAULT_PADDING = 10;
    private static final int DEFAULT_OFFSET = 5;
    private static final int DEFAULT_SHADOW_RADIUS = 5;
    private int mMeasureHeightMode;

    private Bitmap mBitmap;
    private Bitmap mRealBitmap;
    private RectF mRectFShadow;
    private RectF mRoundRectF;
    private Palette mPalette;

    private int mMainColor;

    private OnParseColorListener mListener;

    public void setListener(OnParseColorListener listener) {
        mListener = listener;
    }

    public PaletteImageView(Context context) {
        this(context, null);
    }

    public PaletteImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaletteImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public PaletteImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PaletteImageView);
        mRadius = ta.getDimension(R.styleable.PaletteImageView_paletteRadius, DisplayUtils.dpToPx(0));
        mImgId = ta.getResourceId(R.styleable.PaletteImageView_paletteSrc, 0);
        mPadding = (int) ta.getDimension(R.styleable.PaletteImageView_palettePadding, DisplayUtils.dpToPx(DEFAULT_PADDING));
        mOffsetX = (int) ta.getDimension(R.styleable.PaletteImageView_paletteOffsetX, DisplayUtils.dpToPx(DEFAULT_OFFSET));
        mOffsetY = (int) ta.getDimension(R.styleable.PaletteImageView_paletteOffsetY, DisplayUtils.dpToPx(DEFAULT_OFFSET));
        mShadowRadius = (int) ta.getDimension(R.styleable.PaletteImageView_paletteShadowRadius, DisplayUtils.dpToPx(DEFAULT_SHADOW_RADIUS));
        ta.recycle();

        setPadding(mPadding, mPadding, mPadding, mPadding);

        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setDither(true);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));

        mRoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRoundPaint.setDither(true);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onAttachedToWindow() {
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void onGlobalLayout() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mMeasureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (mMeasureHeightMode == MeasureSpec.UNSPECIFIED) {
            if (null != mBitmap) {
                height = (int) ((width - mPadding * 2) * (mBitmap.getHeight() * 1.0f / mBitmap.getWidth())) + mPadding * 2;
            }

            if (0 != mImgId && null != mRealBitmap) {
                height = mRealBitmap.getHeight() + mPadding * 2;
            }
        }
        if (null != mBitmap) {
            height = (int) ((width - mPadding * 2) * (mBitmap.getHeight() * 1.0f / mBitmap.getWidth())) + mPadding * 2;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRoundRectF = new RectF(0, 0, getWidth() - mPadding * 2, getHeight() - mPadding * 2);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void reDraw() {
        mOffsetX = mOffsetX < DisplayUtils.dpToPx(DEFAULT_OFFSET) ?
                DisplayUtils.dpToPx(DEFAULT_OFFSET) : mOffsetX;
        mOffsetY = mOffsetY < DisplayUtils.dpToPx(DEFAULT_OFFSET) ?
                DisplayUtils.dpToPx(DEFAULT_OFFSET) : mOffsetY;
        mShadowRadius = mShadowRadius < DisplayUtils.dpToPx(DEFAULT_SHADOW_RADIUS) ?
                DisplayUtils.dpToPx(DEFAULT_SHADOW_RADIUS) : mShadowRadius;
        mShadowPaint.setShadowLayer(mShadowRadius, mOffsetX, mOffsetY, mMainColor);
        invalidate();
    }

    private Bitmap createRoundConerImage(Bitmap src, int radius) {
        Bitmap dst = Bitmap.createBitmap(getWidth() - mPadding * 2, getHeight() - mPadding * 2, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(dst);
        canvas.drawRoundRect(mRoundRectF, radius, radius, mRoundPaint);
        mRoundPaint.setXfermode(mPorterDuffXfermode);
        canvas.drawBitmap(src, 0, 0, mRoundPaint);
        mRoundPaint.setXfermode(null);
        return dst;

    }

    public void setShadowColor(int color) {
        mMainColor = color;
        reDraw();
    }

    public void setOffsetX(int offsetX) {
        mOffsetX = offsetX;
        reDraw();
    }

    public void setRadius(float radius) {
        mRadius = radius;
        reDraw();
    }

    public void setOffsetY(int offsetY) {
        mOffsetY = offsetY;
        reDraw();
    }

    private Palette.PaletteAsyncListener mPaletteAsyncListener = new Palette.PaletteAsyncListener() {
        @Override
        public void onGenerated(Palette palette) {
            if (null != palette) {
                mPalette = palette;
                mMainColor = mPalette.getDominantSwatch().getRgb();
                if (mListener != null) mListener.onComplete(PaletteImageView.this);
            } else {
                if (mListener != null) mListener.onFail();
            }
        }
    };

    public int[] getVibrantColor() {
        if (mPalette == null || mPalette.getVibrantSwatch() == null) return null;
        int[] array = new int[3];
        array[0] = mPalette.getVibrantSwatch().getTitleTextColor();
        array[1] = mPalette.getVibrantSwatch().getBodyTextColor();
        array[2] = mPalette.getVibrantSwatch().getRgb();
        return array;
    }

    public int[] getDarkVibrantColor() {
        if (mPalette == null || mPalette.getDarkVibrantSwatch() == null) return null;
        int[] array = new int[3];
        array[0] = mPalette.getDarkVibrantSwatch().getTitleTextColor();
        array[1] = mPalette.getDarkVibrantSwatch().getBodyTextColor();
        array[2] = mPalette.getDarkVibrantSwatch().getRgb();
        return array;
    }

    public int[] getLightVibrantColor() {
        if (mPalette == null || mPalette.getLightVibrantSwatch() == null) return null;
        int[] array = new int[3];
        array[0] = mPalette.getLightVibrantSwatch().getTitleTextColor();
        array[1] = mPalette.getLightVibrantSwatch().getBodyTextColor();
        array[2] = mPalette.getLightVibrantSwatch().getRgb();
        return array;
    }

    public int[] getMutedColor() {
        if (mPalette == null || mPalette.getMutedSwatch() == null) return null;
        int[] array = new int[3];
        array[0] = mPalette.getMutedSwatch().getTitleTextColor();
        array[1] = mPalette.getMutedSwatch().getBodyTextColor();
        array[2] = mPalette.getMutedSwatch().getRgb();
        return array;
    }

    public int[] getDarkMutedColor() {
        if (mPalette == null || mPalette.getDarkMutedSwatch() == null) return null;
        int[] array = new int[3];
        array[0] = mPalette.getDarkMutedSwatch().getTitleTextColor();
        array[1] = mPalette.getDarkMutedSwatch().getBodyTextColor();
        array[2] = mPalette.getDarkMutedSwatch().getRgb();
        return array;
    }

    public int[] getLightMutedColor() {
        if (mPalette == null || mPalette.getLightMutedSwatch() == null) return null;
        int[] array = new int[3];
        array[0] = mPalette.getLightMutedSwatch().getTitleTextColor();
        array[1] = mPalette.getLightMutedSwatch().getBodyTextColor();
        array[2] = mPalette.getLightMutedSwatch().getRgb();
        return array;
    }

    interface OnParseColorListener {
        void onComplete(PaletteImageView paletteImageView);

        void onFail();
    }
}
