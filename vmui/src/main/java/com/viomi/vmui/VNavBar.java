package com.viomi.vmui;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class VNavBar extends ConstraintLayout {
    public ConstraintLayout clContainer;
    public View vSub;
    public ImageView ivBack;
    public ImageView ivLoading;
    public TextView tvBack;
    public ImageView ivRight;
    public TextView tvRight;
    public ImageView ivShare;
    public TextView tvTitle;
    public TextView tvSubTitle;
    /**
     * 0,light 1,dark
     */
    int bgStyle;
    float titleSize;
    float subtextSize;
    boolean enableRight;
    boolean enableBack;


    Drawable loadingDrawable;
    boolean isLoading;
    int loadingDuration;
    ValueAnimator mAnimator;

    public VNavBar(Context context) {
        this(context, null);
    }

    public VNavBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VNavBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.vtitle, this);
        clContainer = findViewById(R.id.cl_container);
        tvTitle = findViewById(R.id.tv_title);
        tvSubTitle = findViewById(R.id.tv_sub_title);
        ivBack = findViewById(R.id.iv_back);
        tvBack = findViewById(R.id.tv_back);
        vSub = findViewById(R.id.v_sub);
        ivShare = findViewById(R.id.iv_share);
        ivRight = findViewById(R.id.iv_right);
        tvRight = findViewById(R.id.tv_right);
        ivLoading = findViewById(R.id.iv_loading);
        initAttrs(attrs);
    }


    private void initAttrs(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.VNavBar);
        setStyle(a.getInt(R.styleable.VNavBar_title_style, 0));
        setTitle(a.getString(R.styleable.VNavBar_text_title));
        setSubText(a.getString(R.styleable.VNavBar_text_subtitle));
        String textLeft = a.getString(R.styleable.VNavBar_text_back);
        String textRight = a.getString(R.styleable.VNavBar_text_right);
        tvBack.setText(textLeft);
        tvRight.setText(textRight);
        Drawable drawableBack, drawableRight, drawableShare;
        drawableBack = a.getDrawable(R.styleable.VNavBar_title_drawable_back);
        drawableRight = a.getDrawable(R.styleable.VNavBar_title_drawable_right);
        drawableShare = a.getDrawable(R.styleable.VNavBar_title_drawable_share);
        if (drawableBack != null)
            ivBack.setImageDrawable(drawableBack);
        if (drawableRight != null)
            ivRight.setImageDrawable(drawableRight);
        if (drawableShare != null)
            ivShare.setImageDrawable(drawableShare);
        titleSize = a.getDimension(R.styleable.VNavBar_title_size
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 17, getResources().getDisplayMetrics()));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        subtextSize = a.getDimension(R.styleable.VNavBar_title_subtext_size
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        tvSubTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, subtextSize);
        enableRight(a.getBoolean(R.styleable.VNavBar_enable_right, false));
        enableShare(a.getBoolean(R.styleable.VNavBar_enable_share, false));
        enableBack(a.getBoolean(R.styleable.VNavBar_enable_back, true));
        loadingDrawable = a.getDrawable(R.styleable.VNavBar_nar_loading_drawable);
        if (loadingDrawable == null)
            loadingDrawable = getResources().getDrawable(R.mipmap.icon_loading_gray);
        setLoadingDrawable(loadingDrawable);
        isLoading = a.getBoolean(R.styleable.VNavBar_nar_isloading, false);
        loadingDuration = a.getInteger(R.styleable.VNavBar_nar_loading_duration, 600);
        setLoading(isLoading);
        setBackOnClickListner(view -> ((Activity) getContext()).finish());
        a.recycle();
    }


    public void setLoading(boolean loading) {
        isLoading = loading;
        ivLoading.setVisibility(isLoading ? VISIBLE : GONE);
        if (loading) {
            rotate();
        } else {
            if (mAnimator != null)
                mAnimator.cancel();
        }
    }

    public void setLoadingDrawable(Drawable loadingDrawable) {
        this.loadingDrawable = loadingDrawable;
        ivLoading.setImageDrawable(loadingDrawable);
    }

    public void setLoadingDuration(int loadingDuration) {
        this.loadingDuration = loadingDuration;
        if (isLoading)
            rotate();
    }

    private void rotate() {
        if (mAnimator != null)
            mAnimator.cancel();
        mAnimator = ValueAnimator.ofInt(0, 365);
        mAnimator.addUpdateListener(valueAnimator -> ivLoading.setRotation((int) valueAnimator.getAnimatedValue()));
        mAnimator.setDuration(loadingDuration);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null) mAnimator.cancel();
    }

    /**
     * @param style 0,light 1,dark
     */
    public void setStyle(int style) {
        bgStyle = style;
        if (bgStyle == 0) {
            clContainer.setBackgroundColor(Color.WHITE);
            setBackImageResource(R.mipmap.back_black);
            setRightImageResource(R.mipmap.right_black);
            setShareImageResource(R.mipmap.share_black);
            tvTitle.setTextColor(getResources().getColor(R.color.title_gray));
            tvSubTitle.setTextColor(getResources().getColor(R.color.content_gray_light));
            tvBack.setTextColor(getResources().getColor(R.color.text_green));
            tvRight.setTextColor(getResources().getColor(R.color.text_green));
        } else {
            clContainer.setBackgroundColor(Color.BLACK);
            setBackImageResource(R.mipmap.back_white);
            setRightImageResource(R.mipmap.right_white);
            setShareImageResource(R.mipmap.share_white);
            tvTitle.setTextColor(Color.WHITE);
            tvSubTitle.setTextColor(Color.parseColor("#b2ffffff"));
            tvBack.setTextColor(Color.WHITE);
            tvRight.setTextColor(Color.WHITE);
        }
    }


    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setSubText(String text) {
        tvSubTitle.setText(text);
        vSub.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
        tvSubTitle.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
    }

    public void setBackText(String text) {
        tvBack.setText(text);
        ivBack.setVisibility(TextUtils.isEmpty(text) ? VISIBLE : GONE);
    }

    public void setRightText(String text) {
        tvRight.setText(text);
        ivRight.setVisibility(TextUtils.isEmpty(text) && enableRight ? VISIBLE : GONE);
    }

    public void setBackImageResource(int drawable) {
        ivBack.setImageDrawable(getResources().getDrawable(drawable));
    }

    public void setRightImageResource(int drawable) {
        ivRight.setImageDrawable(getResources().getDrawable(drawable));
    }

    public void setShareImageResource(int drawable) {
        ivShare.setImageDrawable(getResources().getDrawable(drawable));
    }


    public void setBackOnClickListner(OnClickListener onClickListner) {
        ivBack.setOnClickListener(onClickListner);
        tvBack.setOnClickListener(onClickListner);
    }

    public void setRightOnClickListner(OnClickListener onClickListner) {
        ivRight.setOnClickListener(onClickListner);
        tvRight.setOnClickListener(onClickListner);
    }

    public void setShareOnClickListner(OnClickListener onClickListner) {
        ivShare.setOnClickListener(onClickListner);
    }

    public void enableRight(boolean enable) {
        if (enable) {
            if (TextUtils.isEmpty(tvBack.getText().toString())) {
                ivRight.setVisibility(VISIBLE);
                tvRight.setVisibility(GONE);
            } else {
                ivRight.setVisibility(GONE);
                tvRight.setVisibility(VISIBLE);
            }
        } else {
            ivRight.setVisibility(GONE);
            tvRight.setVisibility(GONE);
        }
    }

    public void enableBack(boolean enable) {
        if (enable) {
            if (TextUtils.isEmpty(tvBack.getText().toString())) {
                ivBack.setVisibility(VISIBLE);
                tvRight.setVisibility(GONE);
            } else {
                ivBack.setVisibility(GONE);
                tvBack.setVisibility(VISIBLE);
            }
        } else {
            ivBack.setVisibility(GONE);
            tvBack.setVisibility(GONE);
        }
    }

    public void enableShare(boolean enable) {
        ivShare.setVisibility(enable ? VISIBLE : GONE);
    }
}
