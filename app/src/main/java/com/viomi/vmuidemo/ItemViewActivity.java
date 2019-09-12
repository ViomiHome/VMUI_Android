package com.viomi.vmuidemo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;

import com.viomi.vmui.VItemView;

public class ItemViewActivity extends AppCompatActivity {
    VItemView itemViewDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ItemView");
        setContentView(R.layout.activity_item_view);
        itemViewDelete = findViewById(R.id.tv_delete);
        itemViewDelete.buttonDelete.setBackgroundColor(getResources().getColor(R.color.price_red));
        itemViewDelete.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int itemWith = itemViewDelete.getWidth();
                if (itemWith > 0) {
                    itemViewDelete.llRoot.scrollTo(itemViewDelete.buttonDelete.getWidth(), 0);
                    itemViewDelete.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
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
