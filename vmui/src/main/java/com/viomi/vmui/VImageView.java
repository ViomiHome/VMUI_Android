package com.viomi.vmui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class VImageView extends AppCompatImageView {
    public VImageView(Context context) {
        this(context, null);
    }

    public VImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
