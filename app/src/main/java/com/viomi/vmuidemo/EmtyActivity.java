package com.viomi.vmuidemo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.viomi.vmui.VEmptyPage;
import com.viomi.vmuidemo.util.StatusBarUtil;

public class EmtyActivity extends BaseActivity {
    VEmptyPage vEmptyPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        StatusBarUtil.StatusBarLightMode(this, true);
        vEmptyPage = findViewById(R.id.empty);
        vEmptyPage.ivEmpty.setImageDrawable(getDrawable(R.drawable.empty_drawable));
    }
}
