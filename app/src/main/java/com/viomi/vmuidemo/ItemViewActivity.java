package com.viomi.vmuidemo;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.view.menu.MenuView;

import com.viomi.vmui.VItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemViewActivity extends BaseActivity {


    float x = 0;
    @BindView(R.id.select_1)
    VItemView select1;
    @BindView(R.id.select_2)
    VItemView select2;
    @BindView(R.id.radio_1)
    VItemView radio1;
    @BindView(R.id.radio_2)
    VItemView radio2;
    @BindView(R.id.switch0)
    VItemView switch0;
    @BindView(R.id.tv_delete)
    VItemView tvDelete;
    @BindView(R.id.item_button)
    VItemView itemButton;
    boolean animated;
    @BindView(R.id.items)
    LinearLayout items;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ItemView");
        setContentView(R.layout.activity_item_view);
        ButterKnife.bind(this);
        tvDelete = findViewById(R.id.tv_delete);
//        VItemView switch0 = findViewById(R.id.switch0);
//        VItemView switch1 = findViewById(R.id.switch1);
//        switch0.vSwitch.setEnabled(false);
//        switch1.vSwitch.setEnabled(false);
//        tvDelete.buttonDelete.setBackgroundColor(getResources().getColor(R.color.price_red));
//        tvDelete.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int itemWith = tvDelete.getWidth();
//                if (itemWith > 0) {
//                    tvDelete.llRoot.scrollTo(tvDelete.buttonDelete.getWidth(), 0);
//                    tvDelete.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
//            }
//        });

//        tvDelete.setOnTouchListener((v, event) -> {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    x = event.getX();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    if (x - event.getX() > tvDelete.buttonDelete.getWidth() / 2) {
//                        if (tvDelete.llRoot.getScrollX() ==tvDelete.buttonDelete.getWidth())
//                            break;
//                        aniTo(0, tvDelete.buttonDelete.getWidth());
//                    } else if (x - event.getX() < -tvDelete.buttonDelete.getWidth() / 2) {
//                        if (tvDelete.llRoot.getScrollX() == 0)
//                            break;
//                        aniTo(tvDelete.buttonDelete.getWidth(), 0);
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    x = event.getX();
//                    animated = false;
//                    break;
//            }
//            return true;
//        });

        select1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select1.setItemSelect(!select1.isItemSelected());
            }
        });
        select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select2.setItemSelect(!select2.isItemSelected());
            }
        });
        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio1.setItemCheck(!radio1.isItemChecked());
            }
        });
        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio2.setItemCheck(!radio2.isItemChecked());
            }
        });

    }

    ValueAnimator animator;

    void aniTo(int start, int end) {
        animated = true;
        if (animator != null && animator.isRunning())
            return;
        animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tvDelete.llRoot.scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        animator.setDuration(200);
        animator.start();

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
