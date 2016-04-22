package com.example.david.simpletipscalcultor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    TextView totalView;
    double total = 0.00;
    TextView tipAmountView;
    double tipamount = 0.00;
    TextView tipPercentageView;
    int tipPercentage = 0;
    SeekBar percentSeeker;
    RadioGroup percentRadios;
    RadioButton ten;
    RadioButton fifteen;
    RadioButton twenty;
    Spinner people;
    EditText billAmount;
    Button round;
    Button reset;
    String thebill = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalView = (TextView) findViewById(R.id.total2);
        tipAmountView = (TextView) findViewById(R.id.tipamount2);
        tipPercentageView = (TextView) findViewById(R.id.tippercent2);
        percentSeeker = (SeekBar) findViewById(R.id.seekbar);
        percentRadios = (RadioGroup) findViewById(R.id.percentradio);
        people = (Spinner) findViewById(R.id.peoplespinner);
        final ArrayList<String> numspinner = new ArrayList<String>(Arrays.asList("1", "2", "3","4","5","6","7","8"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, numspinner);
        people.setAdapter(adapter);
        billAmount = (EditText) findViewById(R.id.amount);
        ten = (RadioButton) findViewById(R.id.ten);
        fifteen = (RadioButton) findViewById(R.id.fifteen);
        twenty = (RadioButton) findViewById(R.id.twenty);
        round = (Button) findViewById(R.id.round);
        reset = (Button) findViewById(R.id.reset);

        percentSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!billAmount.getText().toString().isEmpty()){
                    total = Double.parseDouble(billAmount.getText().toString());
                    percentRadios.clearCheck();
                    tipPercentage = progress;
                    tipPercentageView.setText(Integer.toString(tipPercentage) + "%");
                    tipamount = total * (tipPercentage / 100.00);
                    double returntotal = 0.00;
                    returntotal += total + tipamount;
                    totalView.setText("$"+returntotal);
                    tipAmountView.setText(Double.toString(tipamount));
                    people.setSelection(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Do Nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Do Nothing
            }
        });

        percentRadios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(!billAmount.getText().toString().isEmpty()){
                    total = Double.parseDouble(billAmount.getText().toString());
                    if (ten.isChecked()) {
                        fifteen.setChecked(false);
                        twenty.setChecked(false);
                        tipPercentage = 10;
                        tipamount = total * 0.10;
                    } else if (fifteen.isChecked()) {
                        ten.setChecked(false);
                        twenty.setChecked(false);
                        tipPercentage = 15;
                        tipamount = total * 0.15;
                    } else if (twenty.isChecked()) {
                        fifteen.setChecked(false);
                        ten.setChecked(false);
                        tipPercentage = 20;
                        tipamount = total * 0.20;
                    }
                    double returntotal = 0.00;
                    returntotal += total + tipamount;
                    totalView.setText("$" + returntotal);
                    tipPercentageView.setText(Integer.toString(tipPercentage) + "%");
                    percentSeeker.setProgress(tipPercentage);
                    tipAmountView.setText("$" + tipamount);
                    people.setSelection(0);
                }
            }
        });

        people.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!billAmount.getText().toString().isEmpty()){
                    int num = Integer.parseInt(parent.getSelectedItem().toString());
                    total = Double.parseDouble(billAmount.getText().toString());
                    double returntotal = 0.00;
                    returntotal += total;
                    returntotal /= num;

                    double returntip = 0.00;
                    returntip += tipamount;
                    returntip /= num;
                    tipAmountView.setText(Double.toString(returntip));
                    returntotal+=returntip;
                    totalView.setText("$" + returntotal);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

        billAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                thebill = s.toString();
                if (thebill.isEmpty()) {
                    total = 0.00;
                } else {
                    total = Double.parseDouble(thebill);
                }
                totalView.setText("$" + total);
            }

            @Override
            public void afterTextChanged(Editable s) {
                thebill = s.toString();
                if (thebill.isEmpty()) {
                    total = 0.00;
                } else {
                    total = Double.parseDouble(thebill);
                }
                totalView.setText("$" + total);
                reset();
            }
        });

        round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = totalView.getText().toString();
                temp = temp.substring(1,temp.length()-1);
                double tempdub = Double.parseDouble(temp);
                totalView.setText("$" + Double.toString(Math.ceil(tempdub)));
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                total = 0.00;
                totalView.setText("$0.00");
                thebill="";
                billAmount.setText("");
            }
        });

    }

    private void reset(){
        percentSeeker.setProgress(0);
        tipAmountView.setText("$0.00");
        tipPercentageView.setText("0%");
        people.setSelection(0);

    }
}
