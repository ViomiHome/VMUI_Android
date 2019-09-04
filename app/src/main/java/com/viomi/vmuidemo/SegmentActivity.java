package com.viomi.vmuidemo;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.viomi.vmui.VSegment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SegmentActivity extends AppCompatActivity {

    @BindView(R.id.tabSegment)
    VSegment mTabSegment;
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
        setContentView(R.layout.activity_segment);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ScrollTabs");
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
        VSegment.Tab tab0 = new VSegment.Tab("选项一");
        tab0.setSpaceWeight(1, 1);

        VSegment.Tab tab1 = new VSegment.Tab("选项");
        tab1.setSpaceWeight(1, 1);
        tab1.showSignCountView(getBaseContext(), 5);
        VSegment.Tab tab2 = new VSegment.Tab("选项三");
        tab2.setSpaceWeight(1, 1);
        tab2.showSignCountView(getBaseContext(), 0);
        VSegment.Tab tab3 = new VSegment.Tab("选项四");
        tab3.setSpaceWeight(1, 1);
        mTabSegment.addTab(tab0);
        mTabSegment.addTab(tab1);
        mTabSegment.addTab(tab2);
        mTabSegment.addTab(tab3);
        mTabSegment.setDefaultSelectedColor(getResources().getColor(R.color.viomi_green));
        mTabSegment.setDefaultNormalColor(getResources().getColor(R.color.viomi_green));
        mTabSegment.setIndicatorSelectedColor(getResources().getColor(R.color.white));
        mTabSegment.setIndicatorDrawable(getResources().getDrawable(R.drawable.segment_indicator));
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.addOnTabSelectedListener(new VSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {

            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
//                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }

    private View getPageView(int position) {
        TextView textView = new TextView(getBaseContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.viomi_green));
        textView.setText("第" + (position + 1) + "页");
        return textView;
    }
}
