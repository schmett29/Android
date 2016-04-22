package com.example.david.todohw4partb;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditDetails extends AppCompatActivity {

    private EditText editTitle, editDescription;
    private TextView showDate;
    private Button save;
    private String title, description, date;
    private String returntitle, returndescription;
    private ToDo todo;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        editTitle = (EditText) findViewById(R.id.edittitle);
        editDescription = (EditText) findViewById(R.id.editdescription);
        showDate = (TextView) findViewById(R.id.showdate);
        save = (Button) findViewById(R.id.save);

        // pick call made to Activity2 via Intent
        Intent myLocalIntent = getIntent();

        // look into the bundle sent to Activity2 for data items
        Bundle myBundle =  myLocalIntent.getExtras();
        title = myBundle.getString("title");
        description = myBundle.getString("description");
        date = myBundle.getString("date");
        todo = (ToDo) myBundle.getSerializable("todo");
        position = myBundle.getInt("position");

        // operate on the input data
        editTitle.setText(title, TextView.BufferType.EDITABLE);
        editDescription.setText(description, TextView.BufferType.EDITABLE);
        showDate.setText(date, TextView.BufferType.NORMAL);

        // return sending an OK signal to calling activity
        //setResult(Activity.RESULT_OK, myLocalIntent);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returntitle = editTitle.getText().toString();
                returndescription = editDescription.getText().toString();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("title", returntitle);
                intent.putExtra("description", returndescription);
                intent.putExtra("todo", todo);
                intent.putExtra("position", position);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
