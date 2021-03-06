package com.viomi.vmuidemo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.viomi.vmui.VButton;
import com.viomi.vmui.VToast;
import com.viomi.vmuidemo.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingStatusActivity extends BaseActivity {

    @BindView(R.id.tv_toast)
    VButton tvToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.WHITE);
        }
        setContentView(R.layout.activity_loading_status);
        ButterKnife.bind(this);
        StatusBarUtil.StatusBarLightMode(this, true);
        tvToast.setOnClickListener(v -> VToast.makeLoadingToast(this, "正在加载...", Toast.LENGTH_SHORT).show());
    }

}
