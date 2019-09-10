package com.viomi.vmuidemo;

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
                        startActivity(new Intent(getBaseContext(), TitleActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getBaseContext(), TabsActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getBaseContext(), NoticeBarActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(getBaseContext(), TabbarActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(getBaseContext(), SegmentActivity.class));
                        break;
                    case 6://Dialog
                        break;
                    case 7://TextDialog
                        break;
                    case 8://Picker
                        break;
                    case 9://DatePicker
                        break;
                    case 10://Popup
                        break;
                    case 11://ActionSheet
                        break;
                    case 12://ShareSheet
                        break;
                    case 13://Toast
                        break;
                    case 14://SearchBar
                        startActivity(new Intent(getBaseContext(), SearchBarActivity.class));
                        break;
                    case 15://EmptyPage
                        startActivity(new Intent(getBaseContext(), EmtyActivity.class));
                        break;
                    case 16://LoadingPage
                        startActivity(new Intent(getBaseContext(), LoadingActivity.class));
                        break;
                }
            }
        });
    }
}
