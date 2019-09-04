package com.viomi.vmuidemo;

import android.graphics.drawable.Drawable;
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

import com.viomi.vmui.VTabSegment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabbarActivity extends AppCompatActivity {

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
            return 3;
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
        setContentView(R.layout.activity_tabbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tabbar");
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
        Drawable selectedDrawable0 = getResources().getDrawable(R.drawable.tabbar_selected);
        Drawable normalDrawable0 = getResources().getDrawable(R.drawable.tabbar_normal);
        Drawable selectedDrawable1 = getResources().getDrawable(R.drawable.tabbar_selected);
        Drawable normalDrawable1 = getResources().getDrawable(R.drawable.tabbar_normal);
        Drawable selectedDrawable2 = getResources().getDrawable(R.drawable.tabbar_selected);
        Drawable normalDrawable2 = getResources().getDrawable(R.drawable.tabbar_normal);
        VTabSegment.Tab tab0 = new VTabSegment.Tab(normalDrawable0, selectedDrawable0, "选项一", true);
        tab0.showSignCountView(getBaseContext(), 0);
        tab0.setIconPosition(VTabSegment.ICON_POSITION_TOP);

        VTabSegment.Tab tab1 = new VTabSegment.Tab(normalDrawable1, selectedDrawable1, "选项二", true);
        tab1.showSignCountView(getBaseContext(), 100);
        tab1.setIconPosition(VTabSegment.ICON_POSITION_TOP);

        VTabSegment.Tab tab2 = new VTabSegment.Tab(normalDrawable2, selectedDrawable2, "选项三", true);
        tab2.setIconPosition(VTabSegment.ICON_POSITION_TOP);

        mTabSegment.addTab(tab0);
        mTabSegment.addTab(tab1);
        mTabSegment.addTab(tab2);
        mTabSegment.setDefaultSelectedColor(getResources().getColor(R.color.viomi_green));
        mTabSegment.setDefaultNormalColor(getResources().getColor(R.color.tips_gray));
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setHasIndicator(false);
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
