package com.viomi.vmui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.listview)
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initListView();
    }

    private void initListView() {
        String[] listItems = new String[]{
                "Button",
                "NavBar",
                "Tabs",
                "NoticeBar",
                "TabBar",
                "Segment",
                "SettlementBar",
                "Dialog",
                "TextDialog"
                , "Picker"
                , "DatePicker"
                , "Popup"
                , "ActionSheet"
                , "ShareSheet"
                , "Toast"
                , "SearchBar"
                , "EmptyPage"
                , "LoadingPage"
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        listview.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.list_item, data));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getBaseContext(), ButtonActivity.class));
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                    case 9:
                        break;
                }
            }
        });
    }
}
