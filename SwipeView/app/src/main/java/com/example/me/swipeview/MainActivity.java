package com.example.me.swipeview;

import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    ListView mListView;


    ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mListView = findViewById(R.id.activity_list_view);


        listViewAdapter = new ListViewAdapter(this, R.layout.item_list_view);


        mListView.setAdapter(listViewAdapter);


    }


    public void resetLists(View v) {

        mListView.setAdapter(listViewAdapter);


    }


}
