package com.viomi.vmui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.lang.reflect.Type;

/**
 * Created by Master on 2016/1/4.
 */
public class VColorTextView extends View {

    private int mTextStartX;
    private int mTextStartY;

    private String mText = "";
    private Paint mPaint;
    private int mTextSize = sp2px(30);

    private int mTextOriginColor = 0xffffffff;
    private int mTextChangeColor = 0xff00CA96;

    private Rect mTextBound = new Rect();
    private int mTextWidth;
    private int mTextHeight;


    public VColorTextView(Context context) {
        this(context, null);
    }

    public VColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measureText();
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);

        mTextStartX = getMeasuredWidth() / 2 - mTextWidth / 2;
        mTextStartY = getMeasuredHeight() / 2 - mTextHeight / 2;
    }

    private int measureHeight(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mTextBound.height();
                result += getPaddingTop() + getPaddingBottom();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result;
    }

    private int measureWidth(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                // result = mTextBound.width();
                result = mTextWidth;
                result += getPaddingLeft() + getPaddingRight();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result;
    }

    private void measureText() {
        mTextWidth = (int) mPaint.measureText(mText);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        mTextHeight = (int) Math.ceil(fm.descent - fm.top);

        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        mTextHeight = mTextBound.height();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawChange(canvas);
        drawOrigin(canvas);
    }

    Rect mSelectedRect = new Rect();

    public void setSelectedRect(Rect rect) {
        mSelectedRect = rect;
        invalidate();
    }


    private void drawText_h(Canvas canvas, int color, int startX, int endX) {
        mPaint.setColor(color);
        canvas.save();
        canvas.clipRect(startX, 0, endX, getMeasuredHeight());// left, top,
        // right, bottom
        canvas.drawText(mText, mTextStartX,
                getMeasuredHeight() / 2
                        - ((mPaint.descent() + mPaint.ascent()) / 2), mPaint);
        canvas.restore();
    }


    private void drawChange(Canvas canvas) {
        drawText_h(canvas, mTextChangeColor, mSelectedRect.left,
                mSelectedRect.right);
//        mPaint.setColor(Color.RED);
//        mPaint.setStyle(Style.STROKE);
//        mPaint.setStrokeWidth(10);
//        canvas.drawRect(mSelectedRect, mPaint);
    }

    private void drawOrigin(Canvas canvas) {
        drawText_h(canvas, mTextOriginColor, 0,
                mSelectedRect.left);
        drawText_h(canvas, mTextOriginColor, mSelectedRect.right,
                getMeasuredWidth());
    }

    public void setTextColor(int color) {
        this.mTextOriginColor = color;
        invalidate();
    }


    public int getTextSize() {
        return mTextSize;
    }

    /**
     * @param mTextSize pixel units
     */
    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        if (mPaint != null)
            mPaint.setTextSize(mTextSize);
        requestLayout();
        invalidate();
    }

    public void setText(String text) {
        this.mText = text;
        requestLayout();
        invalidate();
    }

    public void setTypeface(Typeface typeface) {
        mPaint.setTypeface(typeface);
        requestLayout();
        invalidate();
    }

    public int getTextOriginColor() {
        return mTextOriginColor;
    }

    public void setTextOriginColor(int mTextOriginColor) {
        this.mTextOriginColor = mTextOriginColor;
        invalidate();
    }

    public int getTextChangeColor() {
        return mTextChangeColor;
    }

    public void setTextChangeColor(int mTextChangeColor) {
        this.mTextChangeColor = mTextChangeColor;
        invalidate();
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    private int sp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }
}