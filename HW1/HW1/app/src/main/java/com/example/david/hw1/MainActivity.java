package com.example.david.hw1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public String str ="";
    public String operator = "";
    Character op = 'q';
    int i,num,numtemp;
    TextView showResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showResult = (TextView)findViewById(R.id.result);
    }

    public void clickDigit(View view) {
        int buttonId = view.getId();
        switch(buttonId){
            case R.id.Btn2:
                delete();
                break;
            case R.id.Btn3:
                insert(7);
                break;
            case R.id.Btn4:
                insert(8);
                break;
            case R.id.Btn5:
                insert(9);
                break;
            case R.id.Btn6:
                operator = "/";
                perform();
                break;
            case R.id.Btn7:
                insert(4);
                break;
            case R.id.Btn8:
                insert(5);
                break;
            case R.id.Btn9:
                insert(6);
                break;
            case R.id.Btn10:
                operator = "*";
                perform();
                break;
            case R.id.Btn11:
                insert(1);
                break;
            case R.id.Btn12:
                insert(2);
                break;
            case R.id.Btn13:
                insert(3);
                break;
            case R.id.Btn14:
                operator = "-";
                perform();
                break;
            case R.id.Btn15:
                insert('.');
                break;
            case R.id.Btn16:
                insert(0);
                break;
            case R.id.Btn17:
                calculate();
            case R.id.Btn18:
                operator = "+";
                perform();
                break;
        }
    }

    private void delete() {
        // TODO Auto-generated method stub
        if(str.charAt(str.length()-1) == ' '){
            str = str.replace(str.substring(str.length()-3, str.length()-1), "");
        }
        else{
            str = str.replace(str.substring(str.length()-1), "");
        }
        showResult.setText(str);
    }
    private void insert(int j) {
        // TODO Auto-generated method stub
        str = str+Integer.toString(j);
        //num = Integer.valueOf(str).intValue();
        showResult.setText(str);
    }
    private void perform() {
        // TODO Auto-generated method stub
        str = str+" " + operator + " ";
        //numtemp = num;
        showResult.setText(str);
    }
    private void calculate() {
        // TODO Auto-generated method stub
        str ="";
        operator ="";
        num = 0;
        numtemp = 0;
        showResult.setText(str);
    }
}
