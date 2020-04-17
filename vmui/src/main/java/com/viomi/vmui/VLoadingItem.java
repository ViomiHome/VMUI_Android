package com.viomi.vmui;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class VLoadingItem extends LinearLayout {
    public ValueAnimator mAnimator;
    public ImageView iv;
    public TextView textView;
    Drawable drawable;
    int loadingDuration;

    public VLoadingItem(Context context) {
        this(context, null);
    }

    public VLoadingItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLoadingItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.loading_item, this);
        iv = findViewById(R.id.iv);
        textView = findViewById(R.id.tv);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.VLoadingItem);
        drawable = a.getDrawable(R.styleable.VLoadingItem_loading_drawable);
        if (drawable == null)
            drawable = getResources().getDrawable(R.mipmap.icon_loading_gray);
        iv.setImageDrawable(drawable);
        loadingDuration = a.getInteger(R.styleable.VLoadingItem_loadingitem_duration, 600);
        textView.setText(a.getString(R.styleable.VLoadingItem_loading_text));
        a.recycle();
    }


    private void rotate() {
        if (mAnimator != null)
            mAnimator.cancel();
        mAnimator = ValueAnimator.ofInt(0, 365);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                iv.setRotation((int) valueAnimator.getAnimatedValue());
            }
        });
        mAnimator.setDuration(loadingDuration);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();
    }

    public void setLoadingText(String text) {
        textView.setText(text);
    }

    public void setLoadingDrawable(int drawableId) {
        iv.setImageDrawable(getResources().getDrawable(drawableId));
    }

    public void setLoadingDuration(int duration) {
        if (mAnimator != null)
            mAnimator.cancel();
        loadingDuration = duration;
        rotate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        rotate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null)
            mAnimator.cancel();
    }
}
