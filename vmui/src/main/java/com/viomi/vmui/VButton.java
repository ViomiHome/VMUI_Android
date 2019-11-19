package com.viomi.vmui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class VButton extends LinearLayout {
    int button_style;
    public TextView tvContent, tvSubcontent;
    public ImageView iv;
    String text_content, text_sub;
    Drawable drawable_right;
    boolean isloading;
    float drawablePadding;
    float textPadding;
    int loadingDuration;
    float textSize;
    float subTextSize;

    int textColor;
    int backgroundResId;

    public VButton(Context context) {
        this(context, null);
    }

    public VButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.vbutton, this);
        tvContent = findViewById(R.id.tv_content);
        tvSubcontent = findViewById(R.id.tv_subcontent);
        iv = findViewById(R.id.iv);
        initAttrs(attrs);
        init();
        setFocusable(true);
        setClickable(true);
    }

    private void init() {
        switch (button_style) {
            case -1:
                tvContent.setTextColor(textColor);
                tvSubcontent.setTextColor(textColor);
                break;
            case 0:
                tvContent.setTextColor(getResources().getColor(R.color.title_gray));
                tvSubcontent.setTextColor(getResources().getColor(R.color.title_gray));
                setBackgroundResource(R.drawable.black_stoke);
                break;
            case 1:
                tvContent.setTextColor(getResources().getColor(R.color.white));
                tvSubcontent.setTextColor(getResources().getColor(R.color.white));
                setBackgroundResource(R.drawable.black_fill);
                break;
            case 2:
                tvContent.setTextColor(getResources().getColor(R.color.viomi_green));
                tvSubcontent.setTextColor(getResources().getColor(R.color.viomi_green));
                setBackgroundResource(R.drawable.blue_stoke);
                break;
            case 3:
                tvContent.setTextColor(getResources().getColor(R.color.white));
                tvSubcontent.setTextColor(getResources().getColor(R.color.white));
                setBackgroundResource(R.drawable.blue_fill);
                break;
            case 4:
                tvContent.setTextColor(getResources().getColor(R.color.price_red));
                tvSubcontent.setTextColor(getResources().getColor(R.color.price_red));
                setBackgroundResource(R.drawable.red_stoke);
                break;
            case 5:
                tvContent.setTextColor(getResources().getColor(R.color.white));
                tvSubcontent.setTextColor(getResources().getColor(R.color.white));
                setBackgroundResource(R.drawable.red_fill);
                break;
            case 6:
                tvContent.setTextColor(textColor);
                tvSubcontent.setTextColor(textColor);
                setBackgroundResource(backgroundResId);
                break;
        }
        if (!TextUtils.isEmpty(text_content)) {
            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tvContent.setText(text_content);
        }
        if (!TextUtils.isEmpty(text_sub)) {
            tvSubcontent.setVisibility(VISIBLE);
            tvSubcontent.setText(text_sub);
            tvSubcontent.setTextSize(TypedValue.COMPLEX_UNIT_PX, subTextSize);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) tvSubcontent.getLayoutParams();
            layoutParams.topMargin = (int) textPadding;
            tvSubcontent.setLayoutParams(layoutParams);
        } else {
            tvSubcontent.setVisibility(GONE);
        }
        if (drawable_right != null) {
            iv.setVisibility(VISIBLE);
            iv.setImageDrawable(drawable_right);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) iv.getLayoutParams();
            layoutParams.rightMargin = (int) drawablePadding;
            iv.setLayoutParams(layoutParams);
            if (isloading) {
                if (mAnimator == null) {
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
                } else if (!mAnimator.isStarted()) {
                    mAnimator.start();
                }
            } else {
                if (mAnimator != null)
                    mAnimator.cancel();
                mAnimator = null;
            }
        } else {
            iv.setVisibility(GONE);
            if (mAnimator != null)
                mAnimator.cancel();
            mAnimator = null;
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null)
            mAnimator.cancel();
        mAnimator = null;
    }

    ValueAnimator mAnimator;

    private void initAttrs(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.VButton);
        button_style = a.getInt(R.styleable.VButton_button_style, -1);
        text_content = a.getString(R.styleable.VButton_text_content);
        text_sub = a.getString(R.styleable.VButton_text_sub);
        drawable_right = a.getDrawable(R.styleable.VButton_drawable_right);
        isloading = a.getBoolean(R.styleable.VButton_isloading, false);
        drawablePadding = a.getDimension(R.styleable.VButton_drawablepadding
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
        textPadding = a.getDimension(R.styleable.VButton_textpadding
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -1, getResources().getDisplayMetrics()));
        loadingDuration = a.getInt(R.styleable.VButton_loading_duration, 600);
        textSize = a.getDimension(R.styleable.VButton_text_size
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        subTextSize = a.getDimension(R.styleable.VButton_subtext_size
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11, getResources().getDisplayMetrics()));
        textColor = a.getColor(R.styleable.VButton_text_color, Color.BLACK);
        a.recycle();
    }


    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (isEnabled())
            if (pressed) {
                setAlpha(0.5f);
            } else {
                setAlpha(1f);
            }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            setAlpha(0.4f);
        } else {
            setAlpha(1f);
        }
        super.setEnabled(enabled);
    }

    public int getButton_style() {
        return button_style;
    }

    public void setButton_style(int button_style) {
        this.button_style = button_style;
        init();
    }

    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
        init();
    }

    public String getText_sub() {
        return text_sub;
    }

    public void setText_sub(String text_sub) {
        this.text_sub = text_sub;
        init();
    }

    public Drawable getDrawable_right() {
        return drawable_right;
    }

    public void setDrawable_right(Drawable drawable_right) {
        this.drawable_right = drawable_right;
        init();
    }

    public boolean isIsloading() {
        return isloading;
    }

    public void setIsloading(boolean isloading) {
        this.isloading = isloading;
        init();
    }

    public int getLoadingDuration() {
        return loadingDuration;
    }

    public void setLoadingDuration(int loadingDuration) {
        this.loadingDuration = loadingDuration;
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
        init();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        init();
    }

    public float getSubTextSize() {
        return subTextSize;
    }

    public void setSubTextSize(float subTextSize) {
        this.subTextSize = subTextSize;
        init();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        init();
    }

    public void setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
        init();
    }
}
