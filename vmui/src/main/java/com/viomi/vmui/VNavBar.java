package com.viomi.vmui;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class VNavBar extends ConstraintLayout {
    public ConstraintLayout clContainer;
    public View vSub;
    public ImageView ivBack;
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
    String textTitle;
    String textSub;
    String textLeft;
    String textRight;
    Drawable drawableRight;
    Drawable drawableLeft;
    Drawable drawableShare;
    float titleSize;
    float subtextSize;
    boolean enableRight;
    boolean enableShare;
    boolean enableBack;

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
        initAttrs(attrs);
        init();
    }


    private void initAttrs(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.VNavBar);
        bgStyle = a.getInt(R.styleable.VNavBar_title_style, 0);
        textTitle = a.getString(R.styleable.VNavBar_text_title);
        textSub = a.getString(R.styleable.VNavBar_text_subtitle);
        textLeft = a.getString(R.styleable.VNavBar_text_back);
        textRight = a.getString(R.styleable.VNavBar_text_right);
        drawableRight = a.getDrawable(R.styleable.VNavBar_title_drawable_right);
        drawableLeft = a.getDrawable(R.styleable.VNavBar_title_drawable_back);
        drawableShare = a.getDrawable(R.styleable.VNavBar_title_drawable_share);
        titleSize = a.getDimension(R.styleable.VNavBar_title_size
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 17, getResources().getDisplayMetrics()));
        subtextSize = a.getDimension(R.styleable.VNavBar_title_subtext_size
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        enableRight = a.getBoolean(R.styleable.VNavBar_enable_right, false);
        enableShare = a.getBoolean(R.styleable.VNavBar_enable_share, false);
        enableBack = a.getBoolean(R.styleable.VNavBar_enable_back, true);
        a.recycle();
    }

    void init() {
        if (bgStyle == 0) {
            clContainer.setBackgroundColor(Color.WHITE);
            drawableLeft = getResources().getDrawable(R.mipmap.back_black);
            drawableRight = getResources().getDrawable(R.mipmap.right_black);
            drawableShare = getResources().getDrawable(R.mipmap.share_black);
            tvTitle.setTextColor(getResources().getColor(R.color.title_gray));
            tvSubTitle.setTextColor(getResources().getColor(R.color.content_gray_light));
            tvBack.setTextColor(getResources().getColor(R.color.viomi_green));
            tvRight.setTextColor(getResources().getColor(R.color.viomi_green));
        } else {
            clContainer.setBackgroundColor(Color.BLACK);
            drawableLeft = getResources().getDrawable(R.mipmap.back_white);
            drawableRight = getResources().getDrawable(R.mipmap.right_white);
            drawableShare = getResources().getDrawable(R.mipmap.share_white);
            tvTitle.setTextColor(Color.WHITE);
            tvSubTitle.setTextColor(Color.parseColor("#b2ffffff"));
            tvBack.setTextColor(Color.WHITE);
            tvRight.setTextColor(Color.WHITE);
        }
        ivRight.setVisibility(enableRight ? VISIBLE : GONE);
        tvRight.setVisibility(enableRight ? VISIBLE : GONE);
        ivShare.setVisibility(enableShare ? VISIBLE : GONE);
        ivBack.setImageDrawable(drawableLeft);
        ivRight.setImageDrawable(drawableRight);
        ivShare.setImageDrawable(drawableShare);
        tvTitle.setText(textTitle);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        vSub.setVisibility(TextUtils.isEmpty(textSub) ? GONE : VISIBLE);
        tvSubTitle.setVisibility(TextUtils.isEmpty(textSub) ? GONE : VISIBLE);
        tvSubTitle.setText(textSub);
        tvSubTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, subtextSize);
        tvBack.setText(textLeft);
        tvRight.setText(textRight);
        ivBack.setVisibility(TextUtils.isEmpty(textLeft) && enableBack ? VISIBLE : GONE);
        ivRight.setVisibility(TextUtils.isEmpty(textRight) && enableRight ? VISIBLE : GONE);
        setBackOnClickListner(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).onBackPressed();
            }
        });
    }

    /**
     * @param style 0,light 1,dark
     */
    public void setStyle(int style) {
        bgStyle = style;
        init();
    }


    public void setTitle(String title) {
        textTitle = title;
        tvTitle.setText(textTitle);
    }

    public void setSubText(String text) {
        textSub = text;
        tvSubTitle.setText(textSub);
        vSub.setVisibility(TextUtils.isEmpty(textSub) ? GONE : VISIBLE);
        tvSubTitle.setVisibility(TextUtils.isEmpty(textSub) ? GONE : VISIBLE);
    }

    public void setBackText(String text) {
        textLeft = text;
        tvBack.setText(textLeft);
        ivBack.setVisibility(TextUtils.isEmpty(textLeft) ? VISIBLE : GONE);
    }

    public void setRightText(String text) {
        textRight = text;
        tvRight.setText(textRight);
        ivRight.setVisibility(TextUtils.isEmpty(textRight) && enableRight ? VISIBLE : GONE);
    }

    public void setBackImageResource(int drawable) {
        drawableLeft = getResources().getDrawable(drawable);
        ivBack.setImageDrawable(drawableLeft);
    }

    public void setRightImageResource(int drawable) {
        drawableRight = getResources().getDrawable(drawable);
        ivRight.setImageDrawable(drawableRight);
    }

    public void setShareImageResource(int drawable) {
        drawableShare = getResources().getDrawable(drawable);
        ivShare.setImageDrawable(drawableShare);
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

    public void EnableRight(boolean enable) {
        ivRight.setVisibility(enable ? VISIBLE : GONE);
        tvRight.setVisibility(enable ? VISIBLE : GONE);
    }

    public void EnableShare(boolean enable) {
        ivShare.setVisibility(enable ? VISIBLE : GONE);
    }
}
