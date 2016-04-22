package com.example.david.pizzaorder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeatActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    Button done;
    String current;
    ListView meatsListView;
    ArrayList<String> chosen = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meat);

        done = (Button) findViewById(R.id.done);
        meatsListView = (ListView) findViewById(R.id.meatlist);

        List meatsList =
                Arrays.asList(getResources().getStringArray(R.array.meats));

        ArrayAdapter vegetableListAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice, meatsList);

        meatsListView.setAdapter(vegetableListAdapter);
        meatsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        meatsListView.setOnItemClickListener(this);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send meatsssss
                Intent intent = new Intent();
                intent.putStringArrayListExtra("Meats", chosen);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        current = (String) meatsListView.getItemAtPosition(pos);
        meatsListView.setItemChecked(pos, true);
        if (!chosen.contains(current))
            chosen.add(current);
    }
}
