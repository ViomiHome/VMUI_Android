package com.viomi.vmuidemo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.viomi.vmui.VButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ButtonActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.vb)
    VButton vb;
    @BindView(R.id.vb1)
    VButton vb1;

    @BindView(R.id.vb3)
    VButton vb3;
    @BindView(R.id.vb4)
    VButton vb4;

    @BindView(R.id.vb6)
    VButton vb6;
    @BindView(R.id.vb7)
    VButton vb7;

    @BindView(R.id.vb9)
    VButton vb9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Button");
        vb7.setEnabled(false);
        vb7.setAlpha(0.6f);
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
    public void onClick(View view) {

    }
}
