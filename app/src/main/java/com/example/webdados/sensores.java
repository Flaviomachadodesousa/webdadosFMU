package com.example.webdados;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class sensores extends AppCompatActivity {
    SensorManager mSensorManager;
    Sensor mLuz, mUmidade, mAcelerometro;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Location location;
    //private LocationManager locationManager;
    double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);

        Log.i("Mensagem", "Ola mundo");
        //pedirPermissao();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Button postRequest = findViewById(R.id.postRequest);

    }


    public void ListarSensores(View view) {
        List<Sensor> lista = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        Iterator<Sensor> iterator = lista.iterator();
        String sensores = " ";
        while (iterator.hasNext()) {
            Sensor sensor = iterator.next();
            sensores += " - " + sensor.getName() + "\n";
        }
        Log.i("Sensores","Meus sensores: \n"+sensores);

        Toast toast = Toast.makeText(getApplicationContext(), "Enviado para Web Service Todos os sensores do meu celular", Toast.LENGTH_LONG);
        toast.show();
    }

    /*                  INICIO LUMINOSIDADE                          */
    public void LUMINOSIDADE(View view) {
        mLuz = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(new LuzSensor(), mLuz, SensorManager.SENSOR_DELAY_FASTEST);

        Toast toast = Toast.makeText(getApplicationContext(), "Enviado para Web Service Lumininosidade", Toast.LENGTH_LONG);
        toast.show();
    }



    static class LuzSensor implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            float vl = event.values[0];
            Log.i("Sensores","Luminosidade: "+vl);
        }
    }

    /*              FIM LUMINOSIDAD                                 */
    // -------------------------------------------------------------//

    /*                INICIO UMIDADE                          */
    public void UMIDADE(View view) {
        mUmidade = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(new Umidade(), mUmidade, SensorManager.SENSOR_DELAY_FASTEST);

        Toast toast = Toast.makeText(getApplicationContext(), "Enviado para Web Service Umidade", Toast.LENGTH_LONG);
        toast.show();
    }

    static class Umidade implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        public void onSensorChanged(SensorEvent event) {
            float vl = event.values[0];
            Log.i("Sensores","Umidade: "+vl);
        }
    }
    /*              FIM UMIDADE                                 */
    //----------------------------------------------------------//

    public void PROXIMIDADE(View view) {

        Toast toast = Toast.makeText(getApplicationContext(), "Enviado para Web Service Proximidade", Toast.LENGTH_LONG);
        toast.show();
    }


    public void postRequest(View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(sensores.this);
        String url = "https://webandroidservice.000webhostapp.com/send.php?path=sensores";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params=new HashMap<String, String>();
                params.put("dado","valor");
                return params;
            }

            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }

        };

        requestQueue.add(stringRequest);

        /*
        Toast toast = Toast.makeText(getApplicationContext(), "Tentando enviar", Toast.LENGTH_LONG);
        toast.show();

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject actualdata = new JSONObject();
        try {
            actualdata.put("test","teste");
            actualdata.put("teste",24);
        } catch (JSONException e){
            Log.d("OKHTTPS","JSON ERRO");
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON,actualdata.toString());
        Request newReq = new Request.Builder()
                .url("https://webandroidservice.000webhostapp.com/send.php?path=sensores")
                .post(body)
                .build();
        try {
            Response response = client.newCall(newReq).execute();
        }catch (IOException e){
            e.printStackTrace();
        }*/
    }

}