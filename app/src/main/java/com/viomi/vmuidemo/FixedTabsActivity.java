package com.viomi.vmuidemo;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.viomi.vmui.VTabSegment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FixedTabsActivity extends BaseActivity {

    @BindView(R.id.tabSegment)
    VTabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;
    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            View view = getPageView(position);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_tabs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("FixedTabs");
        ButterKnife.bind(this);
        initTabAndPager();
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

    private void initTabAndPager() {
        mContentViewPager.setAdapter(mPagerAdapter);
        mContentViewPager.setCurrentItem(0, false);
        VTabSegment.Tab tab0 = new VTabSegment.Tab("选项一");
        VTabSegment.Tab tab1 = new VTabSegment.Tab("选项二");
        mTabSegment.addTab(tab0);
        mTabSegment.addTab(tab1);
        mTabSegment.addTab(new VTabSegment.Tab("选项三"));
        mTabSegment.addTab(new VTabSegment.Tab("选项四"));
        mTabSegment.setDefaultSelectedColor(getResources().getColor(R.color.title_gray));
        mTabSegment.setDefaultNormalColor(getResources().getColor(R.color.tips_gray));
        mTabSegment.setIndicatorDrawable(getResources().getDrawable(R.drawable.indicator));
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setIndicatorWidthAdjustContent(false);
        mTabSegment.setMode(VTabSegment.MODE_FIXED);
        mTabSegment.addOnTabSelectedListener(new VTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {

            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }

    private View getPageView(int position) {
        AppCompatTextView textView = new AppCompatTextView(getBaseContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setTextColor(Color.BLACK);
        textView.setText("选项" + (position + 1) );
        textView .getPaint().setFakeBoldText(true);
        return textView;
    }
}
