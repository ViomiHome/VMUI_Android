package com.viomi.vmuidemo;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.viomi.vmui.VDialogToast;
import com.viomi.vmui.VToast;

public class ToastActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Toast");
        findViewById(R.id.btn_normal).setOnClickListener(this::onClick);
        findViewById(R.id.btn_loading).setOnClickListener(this::onClick);
        findViewById(R.id.btn_success).setOnClickListener(this::onClick);
        findViewById(R.id.btn_error).setOnClickListener(this::onClick);
        findViewById(R.id.btn_special).setOnClickListener(this::onClick);
        findViewById(R.id.btn_def_time).setOnClickListener(this::onClick);
        findViewById(R.id.btn_dismiss).setOnClickListener(this::onClick);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_normal:
                VToast.makeText(this, "这是一个Toast", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_loading:
                VToast.makeLoadingToast(this, "正在加载...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_success:
                VToast.makeSuccessText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_error:
                Toast toast = VToast.makeErrorText(this, "加载失败", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.btn_special:
                VToast.makeText(this, "这是一个很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长的Toast", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_def_time:
                VDialogToast.makeLoadingToast(this, "正在连接...", 30000).show();
                break;
            case R.id.btn_dismiss:
                VToast.dismiss();
                VDialogToast.dismiss();
                break;
        }
    }
}
