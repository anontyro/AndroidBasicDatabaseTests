package com.tradexc.basicdatabase;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    protected ListView lvMain;
    protected TextView tvMain;
    protected Button buReadDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMain = (ListView)findViewById(R.id.lvMainDatabaseReturn);
        tvMain = (TextView)findViewById(R.id.tvMainTitle);
        buReadDB = (Button)findViewById(R.id.buReadDB);

        tvMain.setText("Main Database Pull");

        GetItemTableData getData = new GetItemTableData();
        try {
            getData.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void buttonClick(View view) {

    }


    protected class GetItemTableData extends AsyncTask<String,String,ArrayList>{
        //Connect to the localhost php document (android uses 10.0.2.2 for localhost connections)
        private String urlVal = "http://10.0.2.2//getItemGroupTable.php";

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
                testValue =DBManager.itemGroupColitemgroupid+": "+ json.getJSONObject(0).getString(DBManager.itemGroupColitemgroupid+"\n");
                testValue +=DBManager.itemGroupColitemgroupidnum+": "+ json.getJSONObject(0).getString(DBManager.itemGroupColitemgroupidnum+"\n");
                testValue +=DBManager.itemGroupColitemgroupcode+": "+ json.getJSONObject(0).getString(DBManager.itemGroupColitemgroupcode+"\n");
                testValue +=DBManager.itemGroupColitemgroupname+": "+ json.getJSONObject(0).getString(DBManager.itemGroupColitemgroupname+"\n");
                testValue +=DBManager.itemGroupColitemgroupstatus+": "+ json.getJSONObject(0).getString(DBManager.itemGroupColitemgroupstatus+"\n");
                testValue +=DBManager.itemGroupColitemgrouptype+": "+ json.getJSONObject(0).getString(DBManager.itemGroupColitemgrouptype+"\n");

            }catch (JSONException ex){
                ex.getStackTrace();
            }

            publishProgress(testValue);
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

}
