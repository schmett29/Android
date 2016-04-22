package com.example.david.hw5partb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MoreStats extends AppCompatActivity {
    TextView increase, decrease;
    ListView citylist;
    cities max, min;
    ArrayList<String> thecitylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_stats);

        increase = (TextView) findViewById(R.id.increase);
        decrease = (TextView) findViewById(R.id.decrease);
        citylist = (ListView) findViewById(R.id.list);

        // pick call made to Activity2 via Intent
        Intent myLocalIntent = getIntent();
        thecitylist = myLocalIntent.getStringArrayListExtra("citylist");

        // look into the bundle sent to Activity2 for data items
        Bundle myBundle =  myLocalIntent.getExtras();
        max = (cities) myBundle.getSerializable("max");
        min = (cities) myBundle.getSerializable("min");

        // operate on the input data
        increase.setText(max.getCity() + " with a population increase of " + max.getDiff());
        decrease.setText(min.getCity() + " with a population decrease of " + min.getDiff());

        citylist.setAdapter(new ArrayAdapter<String>(MoreStats.this, android.R.layout.simple_list_item_1, thecitylist));

    }
}
