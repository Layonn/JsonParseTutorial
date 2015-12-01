package rest.http.org.jsonparsetutorial;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


//
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
//


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //Variáveis
    JSONObject jsonobject;
    JSONArray jsonarray;
    ListView listview;
    ListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap <String, String>> arrayList;
    static String RANK = "rank";
    static String COUNTRY = "country";
    static String POPULATION = "population";
    static String FLAG = "flag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Execute DownloadJSON AsyncTask
        new DownloadJSON().execute();
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

    //Download JSON Async
    private class DownloadJSON extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Criando ProgressDialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
            //Titulo do ProgressDialog
            mProgressDialog.setTitle("Android JSON Parse");
            //Mensagem do ProgressDialog
            mProgressDialog.setMessage("Buscando...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params){

            //Criando array
            //arrayList = new ArrayList<HashMap<String, String>>();

            ArrayList<String> items = new ArrayList<String>();
            try {
                URL url = new URL("http://192.168.0.15/rest");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                //
                String next;
                while ((next = bufferedReader.readLine()) != null){
                    JSONArray ja = new JSONArray(next);

                    for(int i=0; i<ja.length(); i++){
                        JSONObject jo = (JSONObject) ja.get(i);
                        items.add(jo.getString("text"));
                    }
                }
/*
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);

                    //Obtendo os JSON Objects
                    map.put("rank", jsonobject.getString("rank"));
                    map.put("country", jsonobject.getString("country"));
                    map.put("population", jsonobject.getString("population"));
                    map.put("flag", jsonobject.getString("flag"));

                    //Colocando os JSON Objects dentro do array
                    arrayList.add(map);
                }*/
            }catch (MalformedURLException e) {
                Log.e("Erro URL", e.getMessage());
                e.printStackTrace();
            }catch (IOException e){
                Log.e("Erro IO", e.getMessage());
                e.printStackTrace();
            }catch (JSONException e){
                Log.e("Erro JSON", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args){
            //Localizando o listview no listview_main.xml
            listview = (ListView) findViewById(R.id.list);

            //Passando os resultados no ListViewAdapter.java
            //adapter = new ListViewAdapter(MainActivity.this, arraylist);

            //colocando o adapter para o ListView
            //listview.setAdapter(adapter);
            //Fechando o progressDialog
            mProgressDialog.dismiss();

        }
    }
}
