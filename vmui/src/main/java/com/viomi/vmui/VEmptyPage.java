package com.viomi.vmui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class VEmptyPage extends LinearLayout {
    public VCircleProgress vProcess;
    public ImageView ivEmpty;
    public TextView tvTitle;
    public TextView tvSubTitle;
    public VButton vButton;
    boolean isloading;

    public VEmptyPage(Context context) {
        this(context, null);
    }

    public VEmptyPage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VEmptyPage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.v_empty, this);
        ivEmpty = findViewById(R.id.iv_emnty);
        vProcess = findViewById(R.id.v_process);
        tvTitle = findViewById(R.id.tv_title);
        tvSubTitle = findViewById(R.id.tv_subtitle);
        vButton = findViewById(R.id.btn);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.VEmptyPage);
        isloading = a.getBoolean(R.styleable.VEmptyPage_empty_loading, false);
        if (isloading)
            vProcess.setVisibility(VISIBLE);
        vProcess.setProgress(a.getInteger(R.styleable.VEmptyPage_empty_process, 0));
        ivEmpty.setImageDrawable(a.getDrawable(R.styleable.VEmptyPage_empty_img));
        tvTitle.setText(a.getString(R.styleable.VEmptyPage_empty_title));
        tvTitle.setTextColor(a.getColor(R.styleable.VEmptyPage_empty_title_textcolor, getResources().getColor(R.color.title_gray)));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.VEmptyPage_empty_title_textsize
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics())));
        tvSubTitle.setTextColor(a.getColor(R.styleable.VEmptyPage_empty_subtitle_textcolor, getResources().getColor(R.color.tips_gray)));
        tvSubTitle.setText(a.getString(R.styleable.VEmptyPage_empty_subtitle));
        tvSubTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.VEmptyPage_empty_subtitle_textsize
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics())));
        vButton.setText_content(a.getString(R.styleable.VEmptyPage_empty_button));
        a.recycle();
    }

    public void setButtonOnclickListner(OnClickListener onclickListner) {
        vButton.setOnClickListener(onclickListner);
    }

    public void setLoading(boolean isloading) {
        this.isloading = isloading;
        vProcess.setVisibility(isloading ? VISIBLE : GONE);
    }

    public void setProcess(int process) {
        vProcess.setProgress(process);
    }

    public void setImg(Drawable drawable) {
        ivEmpty.setImageDrawable(drawable);
    }

    public VCircleProgress getVCircleProgress() {
        return vProcess;
    }

    public VButton getButton() {
        return vButton;
    }

    public TextView getTitle() {
        return tvTitle;
    }

    public TextView getSubTitle() {
        return tvSubTitle;
    }


}
