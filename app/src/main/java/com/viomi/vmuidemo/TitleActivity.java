package com.viomi.vmuidemo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.viomi.vmui.VTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.title1)
    VTitle title1;
    @BindView(R.id.title2)
    VTitle title2;
    @BindView(R.id.title3)
    VTitle title3;
    @BindView(R.id.title4)
    VTitle title4;
    @BindView(R.id.title5)
    VTitle title5;
    @BindView(R.id.title6)
    VTitle title6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
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
    public void onClick(View view) {

    }
}
