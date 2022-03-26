package com.example.trabajoindividual_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Review_Activity extends AppCompatActivity {
    String username;
    String pelicula;
    String reviewText;
    miDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        db = new miDB(this, 1);

        // Obtenemos el nombre del usuario
        Intent i = getIntent();
        username = i.getStringExtra("username");
        pelicula= i.getStringExtra("tituloPelicula");

        // Asignar texto al TextView
        TextView textView = (TextView) findViewById(R.id.textViewReview);
        textView.setText(getString(R.string.review,pelicula));

        // Comprobamos si el usuario ya ha hecho una review a esa película
        reviewText = db.yaHaHechoReview(username, pelicula);
        if (reviewText!=null){
            EditText editText = (EditText) findViewById(R.id.editText_Resena);
            editText.setText(reviewText);
        }

        // Cargar preferencias
        cargarPreferencias();
    }

    private void cargarPreferencias(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("Idioma","es");
        Boolean yaCargadas = prefs.getBoolean("PrefsCargadas", false);
        if (!yaCargadas){
            Locale locale;
            switch (idioma){
                case "es":{
                    locale = new Locale("es");
                    break;
                } case "en":{
                    locale = new Locale("en");
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + idioma);
            }

            // Actualizamos las preferencias
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("PrefsCargadas", true);
            editor.apply();

            Locale.setDefault(locale);
            Configuration conf = getBaseContext().getResources().getConfiguration();
            conf.setLocale(locale);
            conf.setLayoutDirection(locale);
            Context context = getBaseContext().createConfigurationContext(conf);
            getBaseContext().getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
            Intent i = new Intent(this, Review_Activity.class);
            i.putExtra("username", username);
            i.putExtra("tituloPelicula", pelicula);
            finish();
            startActivity(i);
        }
    }

    @Override
    public void onDestroy() {
        // Cuando se cierre la actividad indicamos que las preferencias no están cargadas
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("PrefsCargadas", false);
        editor.apply();
    }

    public void onClick(View v){
        EditText editText = (EditText) findViewById(R.id.editText_Resena);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        if (!editText.getText().toString().equals("")){
            if (ratingBar.getRating() != 0){
                if(reviewText!=null){ // Si ya existía una review de ese usuario para esa película
                    if(db.actualizarReview(username,pelicula,editText.getText().toString(),ratingBar.getRating())){
                        Intent intent = new Intent(this, Principal_Activity.class);
                        intent.putExtra("username", username);
                        finish();
                        startActivity(intent);
                    }else{
                        Toast aviso = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                        aviso.show();
                    }
                }else { // Si es la primera review que hace el usuario a esa película
                    if(db.addReview(username, pelicula, editText.getText().toString(),ratingBar.getRating())){
                        Intent intent = new Intent(this, Principal_Activity.class);
                        intent.putExtra("username", username);
                        finish();
                        startActivity(intent);
                    }else{
                        Toast aviso = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                        aviso.show();
                    }
                }

            }else{
                ratingBar.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                Toast aviso = Toast.makeText(this, getString(R.string.puntuacion), Toast.LENGTH_SHORT);
                aviso.show();
            }
        }else{
            editText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            Toast aviso = Toast.makeText(this, getString(R.string.resena), Toast.LENGTH_SHORT);
            aviso.show();
        }
    }
}