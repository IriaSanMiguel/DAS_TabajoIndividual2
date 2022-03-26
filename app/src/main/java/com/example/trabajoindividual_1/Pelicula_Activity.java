package com.example.trabajoindividual_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.Locale;

public class Pelicula_Activity extends AppCompatActivity {
    private miDB db;
    private String titulo;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        db = new miDB(this, 1);
        Intent i = getIntent();
        username = i.getStringExtra("username");
        titulo = i.getStringExtra("tituloPelicula");
    }

}