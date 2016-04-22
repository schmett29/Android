package com.example.david.pizzaorder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    RadioButton rbutton1;
    RadioButton rbutton2;
    RadioButton rbutton3;
    Button vegetables;
    Button meats;
    Button order;
    String size = "";
    ArrayList<String> thevegetables = new ArrayList<String>();
    ArrayList<String> themeats = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRadioButtons();
        initVegetables();
        initMeats();
        initCheckout();


    }

    private void initVegetables(){
        vegetables = (Button) findViewById(R.id.veg);
        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent to call VegetableActivity
                Intent myIntentA1A2 = new Intent (MainActivity.this,
                        VegetableActivity.class);

                // call Activity2, tell your local listener to wait response
                startActivityForResult(myIntentA1A2, 101);
            }
        });
    }

    private void initMeats(){
        meats = (Button) findViewById(R.id.meat);
        meats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent to call VegetableActivity
                Intent myIntentA1A2 = new Intent (MainActivity.this,
                        MeatActivity.class);

                // call Activity2, tell your local listener to wait response
                startActivityForResult(myIntentA1A2, 102);
            }
        });
    }

    private void initCheckout(){
        order = (Button) findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent to call Activity2
                Intent myIntentA1A2 = new Intent (MainActivity.this,
                        OrderActivity.class);
                // create a container to ship data
                Bundle myData = new Bundle();

                // add <key,value> data items to the container
                myData.putString("size", size);
                myData.putStringArrayList("Vegetables", thevegetables);
                myData.putStringArrayList("Meats", themeats);

                // attach the container to the intent
                myIntentA1A2.putExtras(myData);

                // call Activity2, tell your local listener to wait response
                startActivityForResult(myIntentA1A2, 103);
            }
        });
    }

    private void initRadioButtons() {
        // Find radio buttons
        //RadioButton
        rbutton1 = (RadioButton) findViewById(R.id.small);
        rbutton2 = (RadioButton) findViewById(R.id.medium);
        rbutton3 = (RadioButton) findViewById(R.id.large);
        // Setup click listeners
        rbutton1.setOnClickListener(new RadioButtonClick());
        rbutton2.setOnClickListener(new RadioButtonClick());
        rbutton3.setOnClickListener(new RadioButtonClick());
    }
    public class RadioButtonClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //SeekBar seeker = (SeekBar)findViewById(R.id.TipSlider);
            ImageView img = (ImageView) findViewById(R.id.pizzapic);
            switch (v.getId()) {
                case R.id.small:
                    size = "small";
                    img.setImageResource(R.drawable.small);
                    break;
                case R.id.medium:
                    size = "medium";
                    img.setImageResource(R.drawable.medium);
                    break;
                case R.id.large:
                    size = "large";
                    img.setImageResource(R.drawable.large);
                    break;
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // local listener receiving callbacks from other activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView vegetableView;
        TextView meatView;

        String order;
        ArrayList<String> orderList;

        try	{
            if ((requestCode == 101 ) && (resultCode == Activity.RESULT_OK)){
                orderList = data.getStringArrayListExtra("Vegetables");
                thevegetables = orderList;
                vegetableView = (TextView) findViewById(R.id.vegetablesList);
                String vegtemp = "";
                for (String v : thevegetables) {
                    vegtemp += v + " ";
                }
                vegetableView.setText("Chosen Vegetables: " + vegtemp);
            }
            if ((requestCode == 102 ) && (resultCode == Activity.RESULT_OK)){
                orderList = data.getStringArrayListExtra("Meats");
                themeats = orderList;
                meatView = (TextView) findViewById(R.id.meatList);
                String meattemp = "";
                for (String v : themeats) {
                    meattemp += v + " ";
                }
                meatView.setText("Chosen Vegetables: " + meattemp);
            }

        }
        catch (Exception e) {
            //lblResult.setText("Problems - " + requestCode + " " + resultCode);
        }
    }//onActivityResult
}
