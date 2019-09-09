package com.viomi.vmui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class VEmptyPage extends LinearLayout {
    VCircleProgress vProcess;
    ImageView ivEmpty;

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
        vProcess.setProgress(60);
    }
}
