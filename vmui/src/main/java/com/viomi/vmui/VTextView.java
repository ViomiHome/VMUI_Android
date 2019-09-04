package com.viomi.vmui;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class VTextView extends AppCompatTextView {
    private StaticLayout myStaticLayout;
    private TextPaint tp;
    private boolean alignCenter = false;

    public VTextView(Context context) {
        this(context, null);
    }

    public VTextView(Context context,boolean value){
        this(context,null);
        this.alignCenter = value;
    }

    public VTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (alignCenter) {
            initView();
        }
    }

    private void initView() {
        tp = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        tp.setTextSize(getTextSize());
        tp.setColor(getCurrentTextColor());
        myStaticLayout = new StaticLayout(getText(), tp, getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!alignCenter) {
            super.onDraw(canvas);
        } else {
            myStaticLayout.draw(canvas);
        }
    }
}
