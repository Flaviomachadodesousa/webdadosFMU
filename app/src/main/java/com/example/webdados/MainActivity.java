package com.example.webdados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void brasileiro(View view) {
        /* Toast toast = Toast.makeText(getApplicationContext(), "Portugues", Toast.LENGTH_LONG);
        toast.show(); */

        setLocale("pt");


    }

    public void espanhol(View view) {
        /* Toast toast = Toast.makeText(getApplicationContext(), "Espanhol", Toast.LENGTH_LONG);
        toast.show(); */

        setLocale("es");


    }

    public void ingles(View view) {
        /* Toast toast = Toast.makeText(getApplicationContext(), "Inglês", Toast.LENGTH_LONG);
        toast.show(); */

        setLocale("en");

    }

    public void frances(View view) {
        /* Toast toast = Toast.makeText(getApplicationContext(), "Francês", Toast.LENGTH_LONG);
        toast.show(); */

        setLocale("fr");

    }

    private void setLocale(String linguagem){
        Locale meuLocal = new Locale(linguagem);
        Resources res = getResources(); //Pegando todos recursos do celular.
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration(); // Pegando catraca de configuracao.
        conf.locale = meuLocal; // Troca a localização de escrita.

        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());

        Intent intent = new Intent(this, sensores.class); // Chama outra tela.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
