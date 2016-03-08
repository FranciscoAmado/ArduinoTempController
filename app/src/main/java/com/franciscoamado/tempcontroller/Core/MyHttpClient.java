package com.franciscoamado.tempcontroller.Core;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Francisco on 19/08/2015.
 */
public class MyHttpClient extends AsyncTask {

    private Wrapper wrapper;
    private final String USER_AGENT = "Mozilla/5.0";
    private String jsonString = "";
    private String jsonFinal = "";
    private OnTaskCompleted listener;

    // HTTP GET request
    private void sendGet(String url) throws Exception {
        this.wrapper = new Wrapper();

        if(url.isEmpty()) {
            url = "http://192.168.1.100/";
        }

        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());
        jsonString = result.toString();

    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            this.listener = (OnTaskCompleted)params[0];
            String url = (String)params[1];
            this.sendGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!jsonString.isEmpty()){
            try {
                readJsonString(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return this.wrapper;
    }

    @Override
    protected void onPostExecute(Object o) {
        this.listener.onTaskCompleted(this.wrapper);
    }

    public void readJsonString(String json) throws IOException, JSONException {
        JSONObject jObject = new JSONObject(json);
        JSONArray jso3= new JSONArray(jObject.getString("Arduino"));
        for (int i=0; i < jso3.length(); i++)
        {
            JSONObject jsoni = new JSONObject(jso3.getString(i));
            try
            {
                if( i == 0 ) {  // "Sensores"
                    JSONArray jsona = new JSONArray(jsoni.getString("Sensores"));
                    for (int j = 0; j < jsona.length(); j++) {
                        JSONObject jsonFinal = new JSONObject(jsona.getString(j));
                        Sensor sensor = new Sensor(
                                jsonFinal.getString("nome"),
                                jsonFinal.getString("localizacao"),
                                jsonFinal.getDouble("temperatura"),
                                jsonFinal.getDouble("humidade")
                        );

                        wrapper.addSensor(sensor);
                    }
                } else if ( i == 1 ) { // "ArCondicionado"
                    JSONObject jsonFinal = new JSONObject(jsoni.getString("ArCondicionado"));
                    ArCondicionado arCondicionado = new ArCondicionado(
                            jsonFinal.getString("nome"),
                            jsonFinal.getString("estado"),
                            jsonFinal.getString("accao")
                    );
                    wrapper.setArCondicionado(arCondicionado);
                }
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}