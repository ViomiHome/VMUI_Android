package com.viomi.vmuilibs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class VButton extends LinearLayout {
    public VButton(Context context) {
        this(context, null);
    }

    public VButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.vbutton, null);
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
        super.setEnabled(enabled);
        if (enabled) {
            setAlpha(0.4f);
        } else {
            setAlpha(1f);
        }
    }
}
