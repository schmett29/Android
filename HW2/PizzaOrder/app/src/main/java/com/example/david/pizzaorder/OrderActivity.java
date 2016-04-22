package com.example.david.pizzaorder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    Button done;
    String size="";
    ArrayList<String> thevegetables = new ArrayList<String>();
    ArrayList<String> themeats = new ArrayList<String>();
    String vegstring = "No Vegetables";
    String meatstring = "No Meats";
    String returnstring = "The total cost is: $";
    Double cost = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        TextView toppings = (TextView) findViewById(R.id.toppings);

        // pick call made to Activity2 via Intent
        Intent myLocalIntent = getIntent();

        // look into the bundle sent to Activity2 for data items
        Bundle myBundle =  myLocalIntent.getExtras();
        size = myBundle.getString("size");
        thevegetables = myBundle.getStringArrayList("Vegetables");
        themeats = myBundle.getStringArrayList("Meats");

        // operate on the input data
        calculate();
        returnstring += cost;

        // for illustration purposes. show data received & result
        if(!thevegetables.isEmpty()){
            vegstring = "Vegetables: ";
            for(String s : thevegetables){
                vegstring+= s + ", ";
            }
            vegstring.substring(0, vegstring.length()-3);
        }
        if(!themeats.isEmpty()){
            meatstring = "Meats: ";
            for(String s : themeats){
                meatstring+= s + ", ";
            }
            meatstring.substring(0, meatstring.length()-3);
        }
        toppings.setText("Toppings on your " + size.toString() + " pizza: \n" + vegstring + "\n" + meatstring);

        // return sending an OK signal to calling activity
        //setResult(Activity.RESULT_OK, myLocalIntent);

        done = (Button) findViewById(R.id.order);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Thanks, your order was: " + cost, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    private void calculate(){
        if(size.equals("small")){
            cost += 5.00;
            if(!thevegetables.isEmpty()){
                cost += thevegetables.size()*1.00;
            }
            if(!themeats.isEmpty()){
                cost += themeats.size()*2.00;
            }
        }
        else if(size.equals("medium")){
            cost += 7.00;
            if(!thevegetables.isEmpty()){
                cost += thevegetables.size()*2.00;
            }
            if(!themeats.isEmpty()){
                cost += themeats.size()*4.00;
            }
        }
        else if(size.equals("large")) {
            cost += 10.00;
            if(!thevegetables.isEmpty()){
                cost += thevegetables.size()*3.00;
            }
            if(!themeats.isEmpty()){
                cost += themeats.size()*6.00;
            }
        }

        TextView costView = (TextView) findViewById(R.id.cost);
        costView.setText("Your order costs: " + cost);

    }
}
