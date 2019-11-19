package com.viomi.vmuidemo;

import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {

    @Override
    public Resources getResources() {
//        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources());
//        AutoSizeCompat.autoConvertDensity(super.getResources(), 375, true);
        return super.getResources();
    }
}
