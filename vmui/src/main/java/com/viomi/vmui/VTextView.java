package com.viomi.vmui;


import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class VTextView extends AppCompatTextView {
    public VTextView(Context context) {
        this(context, null);
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
}
