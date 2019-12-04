package com.viomi.vmuidemo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.viomi.vmui.VSearchBar;

public class SearchBarActivity extends BaseActivity {
    VSearchBar searchBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        searchBar = findViewById(R.id.search_bar);
        textView = findViewById(R.id.text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SearchBar");
        searchBar.setListner(new VSearchBar.OnSearchListner() {
            @Override
            public void onTextChanged(CharSequence s) {
                textView.setText(s.toString());
            }

            @Override
            public void onCancelClick() {
                Toast.makeText(getBaseContext(), "onCancelClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchClick(String text) {
                Toast.makeText(getBaseContext(), "onSearchClick:" + text, Toast.LENGTH_SHORT).show();
                textView.setText(text);
            }
        });
        findViewById(R.id.layout_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.clearInput();
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
