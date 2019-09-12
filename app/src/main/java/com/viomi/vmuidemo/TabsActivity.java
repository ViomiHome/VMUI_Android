package com.viomi.vmuidemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabsActivity extends AppCompatActivity {
    ListView listview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tabs");
        listview = findViewById(R.id.listview);
        initListView();
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

    private void initListView() {
        String[] listItems = new String[]{
                "Horizontal_fixed",
                "Horizontal_scroll",
                "Dark",
                "Vertical"
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        listview.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.item_view, data));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getBaseContext(), FixedTabsActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getBaseContext(), ScrollTabsActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getBaseContext(), DarkTabsActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getBaseContext(), VerticalTabsActivity.class));
                        break;
                }
            }
        });
    }
}
