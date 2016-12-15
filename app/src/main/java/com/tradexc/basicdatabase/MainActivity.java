package com.tradexc.basicdatabase;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
/*
Main Java Class that has all of the programming information for this Activity
XML Layouts are stored in " app > res > layout > activity_main.xml " for this layout
 */
public class MainActivity extends AppCompatActivity {
    //create the objects to be used
    protected ListView lvMain;
    protected TextView tvMain;
    private ArrayList<ItemGroupItem>itemGroupList;

    //Main method that allows for the creation of the Activity (page)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//binds the layout here

        //link the objects to the objects on the XML view
        lvMain = (ListView)findViewById(R.id.lvMainDatabaseReturn);
        tvMain = (TextView)findViewById(R.id.tvMainTitle);

        //change the text in the Main text displayed in the layout
        tvMain.setText("Main Database Pull");

        /*
        Calls to run my custom AsyncTask (AsyncTasks are good when processing data as it will
        operate in the background and should not slow down UI
         */
        GetItemTableData getData = new GetItemTableData();
        //basic exception handling
        try {
            //make the task run, the ".get()" forces the task to do now
           itemGroupList = getData.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace(); //display stack trace on error
        } catch (ExecutionException e) {
            e.printStackTrace(); //display stack trace on error
        }

        /*
        List View is what will display all of the information, A ListAdapter must be assigned to the
        List View to pass it the data to the XML view to be seen.
         */
        MainListAdapter listAdapter = new MainListAdapter(itemGroupList);
        lvMain.setAdapter(listAdapter);

    }


    /**
     * Main processing task that performs an AsyncTask to read all of the data from the web
     */
    protected class GetItemTableData extends AsyncTask<String,String,ArrayList>{

        //Connect to the localhost php document (android uses 10.0.2.2 for localhost connections)
        private String urlVal = "http://10.0.2.2//getItemGroupTable.php";

        //not used but will call processes before the main task happens
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected ArrayList doInBackground(String... strings) {
            ArrayList<ItemGroupItem> itemGroupList = new ArrayList<>();
            String inputString = "";
            String testValue = "";

            try{
                URL url = new URL(urlVal); //create the URL object

                HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();

                urlConnect.setConnectTimeout(6000); //sets the timeout in miliseconds
                try{
                    InputStream in = new BufferedInputStream(urlConnect.getInputStream());
                    inputString = readStream(in);
                }finally {
                    urlConnect.disconnect();
                }
            }catch(Exception ex){
                ex.getStackTrace();
            }

            try{
                JSONArray json = new JSONArray(inputString);

                for(int i = 0; i < json.length();i++){
                    itemGroupList.add(new ItemGroupItem(
                            json.getJSONObject(i).getString(DBManager.itemGroupColitemgroupcode),
                            json.getJSONObject(i).getString(DBManager.itemGroupColitemgroupname),
                            json.getJSONObject(i).getString(DBManager.itemGroupColitemgroupidnum),
                            json.getJSONObject(i).getString(DBManager.itemGroupColitemgroupstatus),
                            json.getJSONObject(i).getString(DBManager.itemGroupColitemgrouptype)

                            ));
                }

            }catch (JSONException ex){
                ex.getStackTrace();
            }

            publishProgress("size: "+itemGroupList.size());
            return itemGroupList;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            System.out.println("stream data: "+values[0]);
            super.onProgressUpdate(values);
        }

        private String readStream(InputStream inputStream){

            BufferedReader buReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String total = "";
            try{
                while((line = buReader.readLine()) !=null){
                    total +=line;
                }
                inputStream.close();
            }catch(IOException ex){
                ex.getStackTrace();
            }

            return total;
        }

    }

    private class MainListAdapter extends BaseAdapter{
        ArrayList<ItemGroupItem>itemGroupList;
        public MainListAdapter(ArrayList<ItemGroupItem> itemGroupList){
            this.itemGroupList = itemGroupList;
            System.out.println("From List Adapter size : "+itemGroupList.size());
        }

        @Override
        public int getCount() {
            return itemGroupList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater myInflater = getLayoutInflater();
            View myView = myInflater.inflate(R.layout.item_group_item,null);

            final ItemGroupItem item = itemGroupList.get(i);

            TextView tvItemCode = (TextView)myView.findViewById(R.id.tvItemGroupCode);
            tvItemCode.setText(item.itemgroupcode);

            TextView tvItemName = (TextView)myView.findViewById(R.id.tvItemGroupName);
            tvItemName.setText(item.itemgroupname);

            TextView tvItemIDnum= (TextView)myView.findViewById(R.id.tvItemGroupIdNum);
            tvItemIDnum.setText(item.itemgroupnum);

            TextView tvItemStatus = (TextView)myView.findViewById(R.id.tvItemGroupStatus);
            tvItemStatus.setText(item.itemgroupstatus);




            return myView;
        }
    }























}
