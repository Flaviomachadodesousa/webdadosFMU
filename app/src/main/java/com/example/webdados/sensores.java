package com.example.webdados;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
    Sensor mLuz, mUmidade, mAcelerometro, mProx, mGravidade;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Location location;
    double latitude, longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);

        //pedirPermissao();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

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

    }
    public void LOCALIZAR(View view) {

        pedirPermissao();

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case 1: {
                if(grantResults.length > 0 &&
                        grantResults[0]== PackageManager.PERMISSION_GRANTED)
                    configurarServico();
                else
                    Toast.makeText(this, "Não vai funcionar!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void PROXIMIDADE(View view) {
        mProx = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorManager.registerListener(new ProxSensor(), mProx, SensorManager.SENSOR_DELAY_FASTEST);
    }

    class ProxSensor implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent event) {
            float vl = event.values[0];
            Log.i("Sensores","Proximidade: "+vl);

        }
    }

    private void pedirPermissao(){
        if( ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 1);
        }
        else
            configurarServico();
    }

    public void configurarServico(){
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    atualizar(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {  }

                @Override
                public void onProviderDisabled(String provider) {  }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }catch(SecurityException ex){
            Log.i("GPS","erro ");
        }
    }

    public void atualizar(Location location){
        double latPoint = location.getLatitude();
        double longPoint = location.getLongitude();

        Log.i("Sensores", "lat "+latPoint);
        Log.i("Sensores", "lon "+longPoint);

    }



}