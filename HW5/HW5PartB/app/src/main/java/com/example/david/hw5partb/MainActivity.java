package com.example.david.hw5partb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private citiesDataSource datasource;
    TextView chosenCity;
    TextView temp;
    String url;
    Spinner spinner;
    Button refresh, stats;
    LinearLayout elements;
    Long startingMillis;
    boolean isSpinnerInitial = true;
    private Pattern tPattern = null;
    private Pattern cityPattern = null;
    private String   tRE = "\\>(\\d+\\,?\\d+)?"; //regular expression
    private String cityRE = "scope=\"row\">(\\w+\\s?\\w+)";
    private ArrayList<cities> spinnerData = new ArrayList<cities>();
    private ArrayList<String> items = new ArrayList<String>();
    private ArrayList<cities> lessthan = new ArrayList<cities>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chosenCity = (TextView)findViewById(R.id.city);
        url = "https://malegislature.gov/District/CensusData";
        spinner = (Spinner) findViewById(R.id.spinner1);
        elements = (LinearLayout) findViewById(R.id.LinearLayout2);
        temp = (TextView) findViewById(R.id.temp);
        refresh = (Button) findViewById(R.id.refresh);
        stats = (Button) findViewById(R.id.stats);

        datasource = new citiesDataSource(this);
        datasource.open();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datasource.deleteAllComments();
                items = new ArrayList<String>();
                datasource = new citiesDataSource(MainActivity.this);
                datasource.open();
                startTask();
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this,
                        MoreStats.class);
                // create a container to ship data
                Bundle myData = new Bundle();

                // add <key,value> data items to the container
                cities max = datasource.increase();
                cities min = datasource.decrease();
                lessthan = datasource.citylist();
                ArrayList<String> citylist = new ArrayList<String>();
                for(cities c : lessthan){
                    citylist.add(c.getCity());
                }
                myData.putSerializable("max", max);
                myData.putSerializable("min", min);

                // attach the container to the intent
                intent.putExtras(myData);
                intent.putStringArrayListExtra("citylist", citylist);

                // call Activity2, tell your local listener to wait response
                startActivityForResult(intent, 101);

            }
        });

        startTask();
    }

    @Override
    protected void onResume() {
        datasource.open();
        datasource.deleteAllComments();
        items = new ArrayList<String>();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    public void displayProgress(String message) { //only used to debug
        TextView oldPop = (TextView) findViewById(R.id.oldPop);
        oldPop.setText(message);
    }
    public void displayAnswer(String oldP, String newP) {
        TextView oldPop = (TextView) findViewById(R.id.oldPop);
        TextView newPop = (TextView) findViewById(R.id.newPop);
        oldPop.setText(oldP);
        newPop.setText(newP);
    }


    public void startTask() {
        // check connectivity
        /*
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            url = "https://malegislature.gov/District/CensusData";
            //url =  "http://www.citytowninfo.com/places/massachusetts";
            //url = "http://en.wikipedia.org/wiki/List_of_municipalities_in_Massachusetts";
        } else {
            city.setText(getString(R.string.noNetworkError));
        }
        */

        doScrape scrapeTask = new doScrape();
        scrapeTask.execute(url);  //this kicks off background task
    }

    //this approach is more powerfull than a straight read of the url
    //since it can interact with the the browser
    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }
            HttpURLConnection httpConn = urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /*
* Background task to scrape the web.
*/
    // Generics:
// 1. String: Type of reference(s) passed to doInBackground()
// 2. String: Type of reference passed to onProgressUpdate()
// 3. String: Type of reference returned by doInBackground()
// this Value passed to onPostExecute()
    private class doScrape extends AsyncTask<String, String, String> {
        String waitMsg = "Wait\nPopulating Data";
        private final ProgressDialog dialog = new ProgressDialog(
                MainActivity.this);

        // Executed on main UI thread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage(waitMsg);
            this.dialog.setCancelable(false); //don't dismiss on outside touches
            this.dialog.show();

            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            /*
            ConnectivityManager Class that answers queries about the state of network connectivity.
            It also notifies applications when network connectivity changes.

            The getActiveNetworkInfo() method of ConnectivityManager returns a NetworkInfo instance
            representing the first connected network interface it can find or null if none if the
            interfaces are connected. Checking if this method returns null should be enough to tell
            if an internet connection is available.
             */

            if (networkInfo != null && networkInfo.isConnected()) {
                url = "https://malegislature.gov/District/CensusData";
                //url =  "http://www.citytowninfo.com/places/massachusetts";
                //url = "http://en.wikipedia.org/wiki/List_of_municipalities_in_Massachusetts";
            } else {
//                chosenCity.setText(getString(R.string.noNetworkError));
                try {
                    // thread to sleep for 100 milliseconds
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println(e);
                }
                //onDestroy();
                onStop();
            }

        }

        // Run on background thread.
        @Override
        protected String doInBackground(String... arguments) {
            // Extract arguments
            String urlIn = arguments[0];
            String s="",ss="",thecity="";
            BufferedReader in = null;
            InputStream ins = null;

            if (tPattern == null)
                tPattern = Pattern.compile(tRE); // slow, only do once, and not on UI thread
            if(cityPattern == null){
                cityPattern = Pattern.compile(cityRE); // slow, only do once, and not on UI thread
            }
            try {
                ins = openHttpConnection(urlIn);
                in = new BufferedReader(new InputStreamReader(ins));

                /*  //this approach only reads in a stream
                URL url = new URL(urlIn);
                in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));
                */

                String inputLine;
                int count = 0;
                while ((inputLine = in.readLine()) != null && count < 351) {
                    Matcher amatch = cityPattern.matcher(inputLine);
                    if (amatch.find()) {//find city
                        thecity = amatch.group();
                        thecity = thecity.substring(12,thecity.length());
                        inputLine = in.readLine(); //skip a line
                        inputLine = in.readLine(); //get old population data

                        Matcher m = tPattern.matcher(inputLine);
                        if (m.find()) {
                            s = m.group(1);
                        }
                        inputLine = in.readLine(); //skip a line
                        inputLine = in.readLine(); //get new population data

                        Matcher mm = tPattern.matcher(inputLine);
                        if (mm.find()) {
                            ss = mm.group(1);
                        }
                        cities returncity = new cities(thecity, s, ss);
                        int tempop1 = 0;
                        int tempop2 = 0;
                        int tempdiff = 0;
                        try {
                            NumberFormat usFormat = NumberFormat.getNumberInstance(java.util.Locale.US);
                            tempop1 = usFormat.parse(returncity.getPop1()).intValue();
                            tempop2 = usFormat.parse(returncity.getPop2()).intValue();
                            tempdiff = tempop2 - tempop1;
//                            Log.e("Adding", "" + returncity.getCity() + " - " + tempdiff);
                        } catch(ParseException e) {
                            Log.e("Exception", "NumberFormat Exception");
                        }

//                        Log.e("Adding City", returncity.toString());
                        datasource.createCity(returncity.getCity(), tempop1, tempop2,tempdiff);
                        count++;
//                        spinnerData.add(returncity);
                        //return (s + ";" + ss);  //a string with both values
                    }
                }
                Log.e("end count", "" + count);
                return getString(R.string.unknown);  // never found the pattern
            } catch (IOException e) {
                Log.e("ScrapeTemperatures", "Unable to open url: " + url);
                return getString(R.string.unknown);
            } catch (Exception e) {
                Log.e("ScrapeTemperatures", e.toString());
                return getString(R.string.unknown);
            } finally {
                if (in != null)
                    try {
                        in.close();
                    } catch (IOException e) {
                        // ignore, we tried and failed to close, limp along anyway
                    }
            }
        }

        // Executed on main UI thread.
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String message = values[0];
            dialog.setMessage(waitMsg + message);
            displayProgress(message);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            String[] parts = result.split("\\;");
//            String oldp = parts[0];
//            String newP = parts[1];
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            spinnerData = datasource.getAllComments();

            for(cities c : spinnerData){
                items.add(c.getCity());
            }

            // bind array to UI control to show one-line from array
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    MainActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    items);

            // bind everything together
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(MainActivity.this);
        }
    }
    // next two methods implement the spinner's listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {

        if(isSpinnerInitial)
        {
            isSpinnerInitial = false;
            return;
        }
        else  {
            if(temp.getVisibility() == View.VISIBLE){
                temp.setVisibility(View.INVISIBLE);
            }
            // echo on the textbox the user's selection
            elements.setVisibility(View.VISIBLE);
            cities showCity = spinnerData.get(position);
            chosenCity.setText(showCity.getCity());
            displayAnswer(showCity.getPop1(),showCity.getPop2());
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        datasource.open();

        try	{
            if ((requestCode == 101 ) && (resultCode == Activity.RESULT_OK)){

            }
        }
        catch (Exception e) {
            //lblResult.setText("Problems - " + requestCode + " " + resultCode);
        }
    }//onActivityResult
}
