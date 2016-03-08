package com.franciscoamado.tempcontroller;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.franciscoamado.tempcontroller.Core.ArCondicionado;
import com.franciscoamado.tempcontroller.Core.MyHttpClient;
import com.franciscoamado.tempcontroller.Core.OnTaskCompleted;
import com.franciscoamado.tempcontroller.Core.Sensor;
import com.franciscoamado.tempcontroller.Core.Wrapper;
import android.view.View.OnClickListener;


public class MainActivity extends ActionBarActivity implements OnTaskCompleted, OnClickListener, AdapterView.OnItemSelectedListener {

    public static double temperaturaDesejada = 0.0;
    public Wrapper wrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.velocidade_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.velocidade_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        wrapper = new Wrapper();

        final RelativeLayout loadPanel = (RelativeLayout)findViewById(R.id.loadingPanel);
        loadPanel.postDelayed(new Runnable() {
            public void run() {
                loadPanel.setVisibility(View.GONE);
            }
        }, 2000);

        MyHttpClient myHttpClient = new MyHttpClient();
        myHttpClient.execute(this, "");

        TextView viewTempDesejada = (TextView)findViewById(R.id.tempDesejadaValor);
        viewTempDesejada.setOnClickListener(this);

        TextView viewArCondicionadoEstado = (TextView)findViewById(R.id.arCondicionadoEstado);
        viewArCondicionadoEstado.setOnClickListener(this);

        ((Button)findViewById(R.id.button_ok)).setOnClickListener(this);
        ((Button)findViewById(R.id.button_mais)).setOnClickListener(this);
        ((Button)findViewById(R.id.button_menos)).setOnClickListener(this);

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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(Wrapper wrapper) {
        if(!wrapper.isEmpty()) {
            this.wrapper = wrapper;
            // Substituir as TextView's pelos valores dos sensores
            Sensor sensorInterno = (Sensor)(wrapper.getSensores().get(0));
            Sensor sensorExterno = (Sensor)(wrapper.getSensores().get(1));
            ArCondicionado arCondicionado = wrapper.getArCondicionado();


            // Temperatura desejada
            if(temperaturaDesejada == 0) {
                ((TextView) (findViewById(R.id.tempDesejadaValor))).setText(String.valueOf(sensorInterno.getTemperatura()) + "ºC");
            } else {
                ((TextView) (findViewById(R.id.tempDesejadaValor))).setText(String.valueOf(temperaturaDesejada) + "ºC");
            }

            // Temperatura Interna
            ((TextView) (findViewById(R.id.tempInterior))).setText(String.valueOf(sensorInterno.getTemperatura()));
            ((TextView) (findViewById(R.id.tempInteriorTexto))).setText(String.valueOf(sensorInterno.getNome()));
            ((TextView) (findViewById(R.id.tempInteriorTipo))).setText(String.valueOf(sensorInterno.getLocalizacao()));
            ((TextView) (findViewById(R.id.tempInteriorHumidade))).setText(String.valueOf(sensorInterno.getHumidade()) + "%");

            // Temperatura Externa
            ((TextView) (findViewById(R.id.tempExterior))).setText(String.valueOf(sensorExterno.getTemperatura()));
            ((TextView) (findViewById(R.id.tempExteriorTexto))).setText(String.valueOf(sensorExterno.getNome()));
            ((TextView) (findViewById(R.id.tempExteriorTipo))).setText(String.valueOf(sensorExterno.getLocalizacao()));
            ((TextView) (findViewById(R.id.tempExteriorHumidade))).setText(String.valueOf(sensorExterno.getHumidade()) + "%");

            // Ar Condicionado
            if(arCondicionado.getEstado().equalsIgnoreCase("ON")){
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.arCondicionadoAction);
                linearLayout.setVisibility(View.VISIBLE);
                ((TextView)(findViewById(R.id.arCondicionadoAccao))).setVisibility(View.VISIBLE);
            } else {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.arCondicionadoAction);
                linearLayout.setVisibility(View.GONE);
                ((TextView)(findViewById(R.id.arCondicionadoAccao))).setVisibility(View.GONE);
            }
            ((TextView) (findViewById(R.id.arCondicionadoEstado))).setText(String.valueOf(arCondicionado.getEstado()));
            ((TextView) (findViewById(R.id.arCondicionadoAccao))).setText(String.valueOf(arCondicionado.getAccao()));
        }
    }

    /**
     * Quando clica no ON/OFF acede ao arduino para ligar ou desligar o Ar Condicionado e atualizar a informação
     * Ou quando clica no valor da temperatura desejada atualiza a informação
     * Ou quando clica no "+" ou "-" incrementa ou decrementa a temperaturadesejada
     * Ou quando clica no "OK" acede ao arduino para enviar a temperaturadesejada e atualizar a informação
     * @param view
     */
    @Override
    public void onClick(View view) {
        double constanteTemp = 0.5;
        MyHttpClient myHttpClient = new MyHttpClient();
        if (R.id.tempDesejadaValor == view.getId()) {                   // Atualizar os valores
            myHttpClient.execute(this, "");
        } else if (R.id.arCondicionadoEstado == view.getId()) {         // Ligar ArCondicionado
            myHttpClient.execute(this, "http://192.168.1.100/?ACT=1");
        } else if(R.id.button_ok == view.getId()) {                     // Enviar temperaturadesejada
            if(temperaturaDesejada != 0){
                myHttpClient.execute(this, "http://192.168.1.100/?ACT=2&TMP=" + temperaturaDesejada);
            }
        } else if(R.id.button_mais == view.getId()){
            double soma = 0;
            if(temperaturaDesejada == 0){
                Sensor sensorInterno = (Sensor)(wrapper.getSensores().get(0));
                soma = sensorInterno.getTemperatura() + constanteTemp;
                temperaturaDesejada = soma;
            } else if(temperaturaDesejada <= 30){
                soma = temperaturaDesejada + constanteTemp;
                temperaturaDesejada = soma;
            }
            ((TextView) (findViewById(R.id.tempDesejadaValor))).setText(String.valueOf(temperaturaDesejada) + "ºC");
        } else if(R.id.button_menos == view.getId()){
            double sub = 0;
            if(temperaturaDesejada == 0){
                Sensor sensorInterno = (Sensor)(wrapper.getSensores().get(0));
                sub = sensorInterno.getTemperatura() - constanteTemp;
                temperaturaDesejada = sub;
            } else if(temperaturaDesejada >= 15){
                sub = temperaturaDesejada - constanteTemp;
                temperaturaDesejada = sub;
            }
            ((TextView) (findViewById(R.id.tempDesejadaValor))).setText(String.valueOf(temperaturaDesejada) + "ºC");
        }
    }

    /**
     * Quando o item do spinner é seleccionado ele acede ao arduino para definir a velocidade do Ar Condicionado: Auto, Vel1 ou Vel2
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MyHttpClient myHttpClient = new MyHttpClient();
        if (position == 0) {
            myHttpClient.execute(this, "http://192.168.1.100/?ACT=3&VEL=AUTO");
        } else if (position == 1) {
            myHttpClient.execute(this, "http://192.168.1.100/?ACT=3&VEL=VEL1");
        } else if (position == 2) {
            myHttpClient.execute(this, "http://192.168.1.100/?ACT=3&VEL=VEL2");
        } else {
            myHttpClient.execute(this, "");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
