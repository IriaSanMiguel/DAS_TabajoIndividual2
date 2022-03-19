package com.example.trabajoindividual_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

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