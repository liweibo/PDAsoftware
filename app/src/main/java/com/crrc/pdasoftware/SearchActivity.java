package com.crrc.pdasoftware;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar_searchreal);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//
//        MenuItem item = menu.findItem(R.id.action_search);
//        mSearchView.setMenuItem(item);
//
//        return true;
//    }

    @Override
    public void onBackPressed() {
       finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
