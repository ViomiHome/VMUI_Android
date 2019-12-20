package com.viomi.vmuidemo;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;

import com.viomi.vmui.VVerticalTabSegment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerticalTabsActivity extends BaseActivity {

    @BindView(R.id.tabSegment)
    VVerticalTabSegment mTabSegment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_tabs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("VerticalTabs");
        ButterKnife.bind(this);
        initTab();
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

    private void initTab() {
        VVerticalTabSegment.Tab tab0 = new VVerticalTabSegment.Tab("选项一");
        VVerticalTabSegment.Tab tab1 = new VVerticalTabSegment.Tab("选项二");
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        mTabSegment.setDefaultSelectedColor(getResources().getColor(R.color.text_green));
        mTabSegment.setDefaultNormalColor(getResources().getColor(R.color.content_gray_light));
        mTabSegment.setIndicatorDrawable(getResources().getDrawable(R.drawable.indicator_ver));
        mTabSegment.setIndicatorWidthAdjustContent(false);
//        mTabSegment.setPadding(0, padding, 0, padding);
        mTabSegment.addTab(tab0);
        mTabSegment.addTab(tab1);
        mTabSegment.addTab(new VVerticalTabSegment.Tab("选项三"));
        mTabSegment.addTab(new VVerticalTabSegment.Tab("选项四"));
        mTabSegment.addTab(new VVerticalTabSegment.Tab("选项五"));
        mTabSegment.addTab(new VVerticalTabSegment.Tab("选项六"));
        mTabSegment.addTab(new VVerticalTabSegment.Tab("选项七"));
        mTabSegment.setMode(VVerticalTabSegment.MODE_SCROLLABLE);
        mTabSegment.selectTab(0);
    }

}
