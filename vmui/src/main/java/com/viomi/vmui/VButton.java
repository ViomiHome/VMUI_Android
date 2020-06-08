package com.viomi.vmui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class VButton extends LinearLayout {
    public final static int STYLE_NONE = -1;
    public final static int STYLE_BLACK_STROKE = 0;
    public final static int STYLE_BLACK_FILL = 1;
    public final static int STYLE_GREEN_STORKE = 2;
    public final static int STYLE_GREEN_FILL = 3;
    public final static int STYLE_RED_STROKE = 4;
    public final static int STYLE_RED_FILL = 5;
    public final static int STYLE_UNENABLE = 6;
    public final static int STYLE_DIALOG = 7;
    public final static int STYLE_GREEN_FILL_HORIZONTAL = 8;

    private final static int DEFAULT_DURATION = 600;

    int button_style;
    public TextView tvContent, tvSubcontent;
    public ImageView iv;
    Drawable drawable_right;

    Drawable drawable_loading;
    boolean isloading;
    float drawablePadding;
    float textPadding;
    int loadingDuration;


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
        tvSubcontent.setVisibility(GONE);
        iv = findViewById(R.id.iv);
        drawable_loading = getResources().getDrawable(R.mipmap.icon_loading_white);
        initAttrs(attrs);
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
        button_style = a.getInt(R.styleable.VButton_button_style, 0);
        drawablePadding = a.getDimension(R.styleable.VButton_drawablepadding
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
        textPadding = a.getDimension(R.styleable.VButton_textpadding
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -1, getResources().getDisplayMetrics()));
        loadingDuration = a.getInt(R.styleable.VButton_loading_duration, DEFAULT_DURATION);
        setButton_style(button_style);
        setDrawable_right(a.getDrawable(R.styleable.VButton_drawable_right));
        setIsloading(a.getBoolean(R.styleable.VButton_isloading, false));
        String text = a.getString(R.styleable.VButton_text_content);
        setText_content(text == null ? "" : text);
        setText_sub(a.getString(R.styleable.VButton_text_sub));
        setTextSize(a.getDimension(R.styleable.VButton_text_size
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics())));
        setSubTextSize(a.getDimension(R.styleable.VButton_subtext_size
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11, getResources().getDisplayMetrics())));
        setTextColor(a.getColor(R.styleable.VButton_text_color, tvContent.getCurrentTextColor()));
        setEnabled(a.getBoolean(R.styleable.VButton_enable, true));
        a.recycle();
    }


    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (isEnabled())
            if (pressed) {
                setAlpha(0.6f);
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
        switch (button_style) {
            case STYLE_NONE:
                break;
            case STYLE_BLACK_STROKE:
                tvContent.setTextColor(getResources().getColor(R.color.title_gray));
                tvSubcontent.setTextColor(getResources().getColor(R.color.title_gray));
                setBackgroundResource(R.drawable.black_stoke);
                break;
            case STYLE_BLACK_FILL:
                tvContent.setTextColor(getResources().getColor(R.color.white));
                tvSubcontent.setTextColor(getResources().getColor(R.color.white));
                setBackgroundResource(R.drawable.black_fill);
                break;
            case STYLE_GREEN_STORKE:
                tvContent.setTextColor(getResources().getColor(R.color.text_green));
                tvSubcontent.setTextColor(getResources().getColor(R.color.text_green));
                setBackgroundResource(R.drawable.blue_stoke);
                break;
            case STYLE_GREEN_FILL:
                tvContent.setTextColor(getResources().getColor(R.color.white));
                tvSubcontent.setTextColor(getResources().getColor(R.color.white));
                setBackgroundResource(R.drawable.blue_fill);
                break;
            case STYLE_RED_STROKE:
                tvContent.setTextColor(getResources().getColor(R.color.red_stroke_text));
                tvSubcontent.setTextColor(getResources().getColor(R.color.red_stroke_text));
                setBackgroundResource(R.drawable.red_stoke);
                break;
            case STYLE_RED_FILL:
                tvContent.setTextColor(getResources().getColor(R.color.white));
                tvSubcontent.setTextColor(getResources().getColor(R.color.white));
                setBackgroundResource(R.drawable.red_fill);
                break;
            case STYLE_UNENABLE:
                tvContent.setTextColor(getResources().getColor(R.color.tips_gray));
                tvSubcontent.setTextColor(getResources().getColor(R.color.tips_gray));
                setBackgroundResource(R.drawable.btn_unenable);
                break;
            case STYLE_DIALOG:
                tvContent.setTextColor(getResources().getColor(R.color.text_green));
                tvSubcontent.setTextColor(getResources().getColor(R.color.tips_gray));
                setBackgroundResource(R.drawable.btn_dialog);
                break;
            case STYLE_GREEN_FILL_HORIZONTAL:
                tvContent.setTextColor(getResources().getColor(R.color.white));
                tvSubcontent.setTextColor(getResources().getColor(R.color.white));
                setBackgroundResource(R.drawable.green_fill);
                break;
        }
        if (button_style == STYLE_UNENABLE) {
            setFocusable(false);
            setClickable(false);
        } else {
            setFocusable(true);
            setClickable(true);
        }
    }

    public String getText_content() {
        return tvContent.getText().toString();
    }

    public void setText_content(String text_content) {
        tvContent.setText(text_content);
    }

    public String getText_sub() {
        return tvSubcontent.getText().toString();
    }

    public void setText_sub(String text_sub) {
        if (!TextUtils.isEmpty(text_sub)) {
            tvSubcontent.setVisibility(VISIBLE);
            tvSubcontent.setText(text_sub);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) tvSubcontent.getLayoutParams();
            layoutParams.topMargin = (int) textPadding;
            tvSubcontent.setLayoutParams(layoutParams);
        } else {
            tvSubcontent.setVisibility(GONE);
        }
    }

    public Drawable getDrawable_right() {
        return drawable_right;
    }

    public void setDrawable_right(Drawable drawable_right) {
        this.drawable_right = drawable_right;
        if (drawable_right != null) {
            iv.setVisibility(VISIBLE);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) iv.getLayoutParams();
            layoutParams.rightMargin = (int) drawablePadding;
            iv.setLayoutParams(layoutParams);
            iv.setImageDrawable(drawable_right);
        } else {
            iv.setVisibility(GONE);
        }
    }

    public boolean isIsloading() {
        return isloading;
    }

    public void setIsloading(boolean isloading) {
        this.isloading = isloading;
        if (isloading) {
            iv.setVisibility(VISIBLE);
            iv.setImageDrawable(drawable_loading);
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
            iv.setImageDrawable(drawable_right);
            if (mAnimator != null)
                mAnimator.cancel();
            mAnimator = null;
        }
    }

    public int getLoadingDuration() {
        return this.loadingDuration;
    }

    public void setLoadingDuration(int loadingDuration) {
        this.loadingDuration = loadingDuration;
        if (mAnimator != null)
            mAnimator.setDuration(loadingDuration);
    }

    public float getTextSize() {
        return tvContent.getTextSize();
    }

    public void setTextSize(float textSize) {
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public float getSubTextSize() {
        return tvSubcontent.getTextSize();
    }

    public void setSubTextSize(float subTextSize) {
        tvSubcontent.setTextSize(TypedValue.COMPLEX_UNIT_PX, subTextSize);
    }

    public void setTextColor(int textColor) {
        tvContent.setTextColor(textColor);
    }

    public void setBackgroundResId(int backgroundResId) {
        setBackgroundResource(backgroundResId);
    }

    public Drawable getDrawable_loading() {
        return drawable_loading;
    }

    public void setDrawable_loading(Drawable drawable_loading) {
        this.drawable_loading = drawable_loading;
    }
}
