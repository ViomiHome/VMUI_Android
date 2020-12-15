package com.viomi.vmuidemo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;

import com.viomi.vmui.VInputItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InputItemActivity extends BaseActivity {

    @BindView(R.id.item_error)
    VInputItem itemError;
    @BindView(R.id.item_input)
    VInputItem itemInput;
    @BindView(R.id.item_password)
    VInputItem itemPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("InputItem");
        ButterKnife.bind(this);
        itemError.setError(true);
        itemError.setOnTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    itemError.setTitle("单行输入");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        itemInput.setText("输入时显示清除按钮");
        itemPassword.setOnPasswordVisibilityChangeListener(passwordVisibility -> itemPassword.setTitle(passwordVisibility ? "可见密码" : "隐藏密码"));
        //
        //itemInput.setFilters(new InputFilter[]{VInputUtil.Utf8InputFilter(10)});
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
}
