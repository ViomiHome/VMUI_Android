package com.viomi.vmuidemo;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;

import com.viomi.vmui.VItemView;

public class ItemViewActivity extends AppCompatActivity {
    VItemView itemViewDelete;
    float x = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ItemView");
        setContentView(R.layout.activity_item_view);
        itemViewDelete = findViewById(R.id.tv_delete);
        itemViewDelete.buttonDelete.setBackgroundColor(getResources().getColor(R.color.price_red));
//        itemViewDelete.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int itemWith = itemViewDelete.getWidth();
//                if (itemWith > 0) {
//                    itemViewDelete.llRoot.scrollTo(itemViewDelete.buttonDelete.getWidth(), 0);
//                    itemViewDelete.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
//            }
//        });

        itemViewDelete.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (x - event.getX() > itemViewDelete.buttonDelete.getWidth()/2) {
                        aniTo(0, itemViewDelete.buttonDelete.getWidth());
                    } else if (x - event.getX() < -itemViewDelete.buttonDelete.getWidth()/2) {
                        aniTo(itemViewDelete.buttonDelete.getWidth(), 0);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    x = event.getX();
                    break;
            }
            return true;
        });
    }

    ValueAnimator animator;

    void aniTo(int start, int end) {
        if (animator != null && animator.isRunning())
            return;
        animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                itemViewDelete.llRoot.scrollTo((Integer) animation.getAnimatedValue(), 0);
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
