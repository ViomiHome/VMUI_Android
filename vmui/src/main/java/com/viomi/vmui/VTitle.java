package com.viomi.vmui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

public class VTitle extends ConstraintLayout {
    public VTitle(Context context) {
        this(context, null);
    }

    public VTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
