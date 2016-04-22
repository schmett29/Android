package com.example.david.todohw4partb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    class ViewHolder {
        CheckBox checkBox;
        Button showDetail;
    }

    MyCustomAdapter mArrayAdapter = null;

    private static String LOG_TAG = "David";
    String myFileName = "exampleListObject";

    EditText mEdittext;
    Button mAddButton, mClearButton, mDeleteAll;
    ListView mListview;

    ArrayList<MainList> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // create all View objects
        mEdittext = (EditText) findViewById(R.id.myName);
        mAddButton = (Button) findViewById(R.id.listAddButton);
        mClearButton = (Button) findViewById(R.id.listClearButton);
        mDeleteAll = (Button) findViewById(R.id.listdeleteAll);
        mListview = (ListView) findViewById(R.id.mylist);
    }

    @Override
    public void onStart() {
        super.onStart();

        // get Object from file
        Object obj = getObjectFromFile(this, myFileName);

        // if obj returns something, check it's type
        if (obj != null && obj instanceof ArrayList) {
            // use the existing List id it exists
            myList = (ArrayList<MainList>) obj;
        } else {
            // create new List if one doesn't exist
            myList = new ArrayList<MainList>();
        }

        // create array adapter for populating ListView
        mArrayAdapter = new MyCustomAdapter(this, R.layout.activity_main_list, myList);
        mListview.setAdapter(mArrayAdapter);


        // add 'Add' Button onclick listener
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get string from edittext
                MainList enteredString = new MainList(mEdittext.getText().toString(), false);

                // check to make sure the string is not empty
                if (!enteredString.getTitle().isEmpty()) {

                    // add the string to the list
                    myList.add(enteredString);

                    // reset the EditText
                    mEdittext.setText("");

                    // update the ListView display
                    mArrayAdapter.notifyDataSetChanged();

                }

            }
        });

        // add 'Clear' Button onclick listener
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirm delete")
                        .setMessage("Are you sure you want to delete?")
                        .setNegativeButton(android.R.string.cancel,new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                // do the acknowledged action, beware, this is run on UI thread
                                for(int i=myList.size()-1; i>=0;i--){
                                    if(myList.get(i).isSelected()){
                                        myList.get(i).setSelected(false);
                                    }
                                }
                                mArrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do the acknowledged action, beware, this is run on UI thread
                                for (int i = myList.size() - 1; i >= 0; i--) {
                                    if (myList.get(i).isSelected()) {
                                        myList.remove(i);
                                    }
                                }
                                mArrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .create()
                        .show();

                // clear the list contents
            }

        });

        mDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = myList.size() - 1; i >= 0; i--) {
                    myList.remove(i);
                }
                mArrayAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();

        // save the list object to file
        saveObjectToFile(this, myFileName, myList);
    }

    public static void saveObjectToFile(Context context, String fileName, Object obj) {

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();

        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "saveObjectToFile FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "saveObjectToFile IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(LOG_TAG, "saveObjectToFile Exception: " + e.getMessage());
        }
    }

    public static Object getObjectFromFile(Context context, String filename) {

        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Object object = ois.readObject();
            ois.close();

            return object;

        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "getObjectFromFile FileNotFoundException: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "getObjectFromFile IOException: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, "getObjectFromFile ClassNotFoundException: " + e.getMessage());
            return null;
        } catch (Exception e) {// Catch exception if any
            Log.e(LOG_TAG, "getBookmarksFromFile Exception: " + e.getMessage());
            return null;
        }
    }

    private class MyCustomAdapter extends ArrayAdapter<MainList> {

        private ArrayList<MainList> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<MainList> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = countryList;
            //this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            CheckBox checkBox;
            Button showDetail;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.activity_main_list, null);

                holder = new ViewHolder();
                holder.showDetail = (Button) convertView.findViewById(R.id.show);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        MainList country = (MainList) cb.getTag();
                        /*
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_SHORT).show();
                        */
                        country.setSelected(cb.isChecked());

                    }
                });

                holder.showDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button) v;
                        MainList mainlist = (MainList) b.getTag();
                        // create intent to call Activity2
                        Intent intent = new Intent (MainActivity.this,
                                ActivityList.class);
                        // create a container to ship data
                        Bundle myData = new Bundle();

                        // add <key,value> data items to the container
                        myData.putString("title", mainlist.getTitle());
                        myData.putInt("position", position);
                        myData.putSerializable("mainlist", mainlist);

                        // attach the container to the intent
                        intent.putExtras(myData);

                        // call Activity2, tell your local listener to wait response
                        startActivityForResult(intent, 102);
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MainList country = countryList.get(position);
            holder.checkBox.setText(country.getTitle());  //set text of checkbox
            holder.checkBox.setChecked(country.isSelected());
            holder.checkBox.setTag(country);
            holder.showDetail.setTag(country);
            return convertView;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String newtitle;
        MainList mainlist;
        int pos;

        try	{
            if ((requestCode == 102 ) && (resultCode == Activity.RESULT_OK)){
                newtitle = data.getStringExtra("title");
                mainlist = (MainList) data.getSerializableExtra("mainlist");
                mainlist.setTitle(newtitle);
                pos = data.getIntExtra("position",-1);
                myList.set(pos, mainlist);
                // update the ListView display
                //mArrayAdapter.notifyDataSetChanged();
                saveObjectToFile(this, myFileName, myList);

            }
        }
        catch (Exception e) {
            //lblResult.setText("Problems - " + requestCode + " " + resultCode);
        }
    }//onActivityResult
}
