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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityList extends ListActivity {

    class ViewHolder {
        CheckBox checkBox;
        Button showDetail, editDetail;
    }

    MyCustomAdapter mArrayAdapter = null;
    private ToDoDataSource datasource;

    private static String LOG_TAG = "David";
    String title, returntitle;
    MainList mainlist;

    EditText mEdittext, description;
    Button mAddButton, mClearButton, mClearAll, back;
    ListView mListview;
    int position;

    List<ToDo> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        // create all View objects
        mEdittext = (EditText) findViewById(R.id.myEditText);
        description = (EditText) findViewById(R.id.description);
        mAddButton = (Button) findViewById(R.id.myAddButton);
        mClearButton = (Button) findViewById(R.id.myClearButton);
        mListview = (ListView) findViewById(android.R.id.list);
        mClearAll = (Button) findViewById(R.id.deleteAll);
        back = (Button) findViewById(R.id.back);

        // pick call made to Activity2 via Intent
        Intent myLocalIntent = getIntent();

        // look into the bundle sent to Activity2 for data items
        Bundle myBundle =  myLocalIntent.getExtras();
        title = myBundle.getString("title");
        mainlist = (MainList) myBundle.getSerializable("mainlist");
        position = myBundle.getInt("position");
        String dataCode = myBundle.getString("dataCode");

        datasource = new ToDoDataSource(this, title);
        datasource.open();
        datasource.createTable();

        myList = datasource.getAllComments();

        mArrayAdapter = new MyCustomAdapter(this, R.layout.todo_info, (ArrayList<ToDo>)myList);
        setListAdapter(mArrayAdapter);


        // add 'Add' Button onclick listener
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get string from edittext
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
                String currentDateandTime = sdf.format(new Date());
                ToDo enteredString = new ToDo(mEdittext.getText().toString(), description.getText().toString(), currentDateandTime, false);
                // check to make sure the string is not empty
                if (!enteredString.getTitle().isEmpty()) {

                    // add the string to the list
                    //myList.add(enteredString);
                    enteredString = datasource.createToDo(mEdittext.getText().toString(), description.getText().toString(), currentDateandTime);
                    mArrayAdapter.add(enteredString);

                    // reset the EditText
                    mEdittext.setText("");
                    description.setText("");

                    // update the ListView display
                    mArrayAdapter.notifyDataSetChanged();

                }
            }
        });

        // add 'Clear' Button onclick listener
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ActivityList.this)
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
                                        ToDo todo = (ToDo) getListAdapter().getItem(i);
                                        datasource.deleteComment(todo);
                                        mArrayAdapter.remove(todo);
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

        mClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getListAdapter().getCount() > 0) {
                    datasource.deleteAllComments();
                    mArrayAdapter.clear();
                }
            }
        });

        // return sending an OK signal to calling activity
        //setResult(Activity.RESULT_OK, myLocalIntent);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returntitle = datasource.getTabe();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("title", returntitle);
                intent.putExtra("mainlist", mainlist);
                intent.putExtra("position", position);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }


    private class MyCustomAdapter extends ArrayAdapter<ToDo> {

        private ArrayList<ToDo> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<ToDo> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = countryList;
            //this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            CheckBox checkBox;
            Button showDetail, editDetail;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.todo_info, null);

                holder = new ViewHolder();
                holder.showDetail = (Button) convertView.findViewById(R.id.show);
                holder.editDetail = (Button) convertView.findViewById(R.id.edit);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        ToDo country = (ToDo) cb.getTag();
                        country.setSelected(cb.isChecked());

                    }
                });

                holder.showDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button) v;
                        ToDo todo = (ToDo) b.getTag();
                        AlertDialog alertDialog = new AlertDialog.Builder(ActivityList.this).create();
                        alertDialog.setTitle("" + todo.getTitle());
                        alertDialog.setMessage("Description: " + todo.getDescription() + "\n" + "Date Created: " + todo.getDatecreated());
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });

                holder.editDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button) v;
                        ToDo todo = (ToDo) b.getTag();
                        // create intent to call Activity2
                        Intent intent = new Intent (ActivityList.this,
                                EditDetails.class);
                        // create a container to ship data
                        Bundle myData = new Bundle();

                        // add <key,value> data items to the container
                        myData.putString("title", todo.getTitle());
                        myData.putString("description", todo.getDescription());
                        myData.putString("date", todo.getDatecreated());
                        myData.putInt("position", position);
                        myData.putSerializable("todo", todo);

                        // attach the container to the intent
                        intent.putExtras(myData);

                        // call Activity2, tell your local listener to wait response
                        startActivityForResult(intent, 101);
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ToDo country = countryList.get(position);
            holder.checkBox.setText(country.getTitle());  //set text of checkbox
            holder.checkBox.setChecked(country.isSelected());
            holder.checkBox.setTag(country);
            holder.editDetail.setTag(country);
            holder.showDetail.setTag(country);
            return convertView;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        datasource.open();
        String newtitle, newdescription;
        ToDo todo;
        int pos;

        try	{
            if ((requestCode == 101 ) && (resultCode == Activity.RESULT_OK)){
                newtitle = data.getStringExtra("title");
                newdescription = data.getStringExtra("description");
                todo = (ToDo) data.getSerializableExtra("todo");
                todo.setTitle(newtitle);
                todo.setDescription(newdescription);
                datasource.updateComment(todo.getID(), todo.getTitle(), todo.getDescription());

                // update the ListView display
                mArrayAdapter.clear();
                mArrayAdapter.addAll(datasource.getAllComments());
                mArrayAdapter.notifyDataSetChanged();

            }
        }
        catch (Exception e) {
            //lblResult.setText("Problems - " + requestCode + " " + resultCode);
        }
    }//onActivityResult
}
