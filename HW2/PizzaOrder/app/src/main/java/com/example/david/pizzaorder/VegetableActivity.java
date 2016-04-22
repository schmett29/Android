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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VegetableActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    Button done;
    String current;
    ListView vegetablesListView;
    ArrayList<String> chosen = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetable);

        done = (Button) findViewById(R.id.done);
        vegetablesListView = (ListView) findViewById(R.id.veglist);

        List vegetablesList =
                Arrays.asList(getResources().getStringArray(R.array.vegetables));

        ArrayAdapter vegetableListAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice, vegetablesList);

        vegetablesListView.setAdapter(vegetableListAdapter);
        vegetablesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        vegetablesListView.setOnItemClickListener(this);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send vegList
                Intent intent = new Intent();
                intent.putStringArrayListExtra("Vegetables", chosen);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        current = (String) vegetablesListView.getItemAtPosition(pos);
        vegetablesListView.setItemChecked(pos, true);
        if (!chosen.contains(current))
            chosen.add(current);
    }
}
