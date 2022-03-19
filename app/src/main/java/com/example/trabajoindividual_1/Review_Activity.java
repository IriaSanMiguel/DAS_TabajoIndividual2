package com.example.trabajoindividual_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
                        Toast aviso = Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT);
                        aviso.show();
                    }
                }else { // Si es la primera review que hace el usuario a esa película
                    if(db.addReview(username, pelicula, editText.getText().toString(),ratingBar.getRating())){
                        Intent intent = new Intent(this, Principal_Activity.class);
                        intent.putExtra("username", username);
                        finish();
                        startActivity(intent);
                    }else{
                        Toast aviso = Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT);
                        aviso.show();
                    }
                }

            }else{
                ratingBar.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                Toast aviso = Toast.makeText(this, "Por favor dele una puntacióna  la película", Toast.LENGTH_SHORT);
                aviso.show();
            }
        }else{
            editText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            Toast aviso = Toast.makeText(this, "Por favor escriba una reseña", Toast.LENGTH_SHORT);
            aviso.show();
        }
    }
}