package com.viomi.vmuidemo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.viomi.vmui.VNavBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavbarActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title1)
    VNavBar title1;
    @BindView(R.id.title2)
    VNavBar title2;
    @BindView(R.id.title3)
    VNavBar title3;
    @BindView(R.id.title4)
    VNavBar title4;
    @BindView(R.id.title5)
    VNavBar title5;
    @BindView(R.id.title6)
    VNavBar title6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("NavBar");
        ButterKnife.bind(this);
        title1.setRightOnClickListner(this);
        title1.setShareOnClickListner(this);
        title2.setRightOnClickListner(this);
        title2.setShareOnClickListner(this);
        title3.setRightOnClickListner(this);
        title3.setShareOnClickListner(this);
        title4.setRightOnClickListner(this);
        title4.setShareOnClickListner(this);
        title5.setRightOnClickListner(this);
        title5.setShareOnClickListner(this);
        title6.setRightOnClickListner(this);
        title6.setShareOnClickListner(this);

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
