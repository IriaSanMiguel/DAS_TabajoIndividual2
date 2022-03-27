package com.example.trabajoindividual_1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import java.io.ByteArrayInputStream;

public class AdaptorListView extends BaseAdapter {
    private String[] peliculas;
    private byte[][] posters;
    private float[] puntuaciones;
    private LayoutInflater inflater;
    private Context contexto;

    public AdaptorListView(Context pcontext, String[] nombresPeliculas, byte[][] imagenesPeliculas, float[] ppuntuaciones) {
        /*
        Pre: Un contexto, una lista de los nombres de las películas, sus posters y la puntuación que tienen
        Post: Se ha creado el ListView con las películas disponibles
        */
        contexto = pcontext;
        peliculas = nombresPeliculas;
        posters = imagenesPeliculas;
        puntuaciones = ppuntuaciones;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return peliculas.length;
    }

    @Override
    public Object getItem(int i) {
        return peliculas[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        /*
        Pre: El índice de la pelicula, la view y la viewGroup
        Post: Se ha creado correctamente el listView
        */

        view = inflater.inflate(R.layout.listview_peliculas, null);

        // Escribimos los datos

        // Nombre de la película
        TextView titulo = (TextView) view.findViewById(R.id.textViewTituloPeli);
        titulo.setText(peliculas[i]);

        // Póster de la película
        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        image.setImageBitmap(BitmapFactory.decodeByteArray(posters[i], 0, posters[i].length));

        // Puntuación de la película
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar_listView);
        ratingBar.setRating(puntuaciones[i]);

        // Asignamos las acciones a los botones
        Button botonReview = (Button) view.findViewById(R.id.button_review);
        botonReview.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Cuando se clicke este botón se irá a la actividad Review_Activity para que el usuario escriba una reseña
                Intent intent = ((Activity) viewGroup.getContext()).getIntent();
                String username = intent.getStringExtra("username");
                Intent intent_reviews = new Intent(viewGroup.getContext(), Review_Activity.class);
                intent_reviews.putExtra("username", username);
                intent_reviews.putExtra("tituloPelicula", peliculas[i]);
                viewGroup.getContext().startActivity(intent_reviews);
            }
        });

        Button botonDescripcion = (Button) view.findViewById(R.id.button_descripcion);
        botonDescripcion.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Cuando se clcike en este botón se irá a la actividad Pelicula_Activity para que el usuario pueda ver la información de la película
                Intent intent = ((Activity) viewGroup.getContext()).getIntent();
                String sdf = intent.getStringExtra("username");
                Intent intent_reviews = new Intent(viewGroup.getContext(), Pelicula_Activity.class);
                intent_reviews.putExtra("username", sdf);
                intent_reviews.putExtra("tituloPelicula", peliculas[i]);
                viewGroup.getContext().startActivity(intent_reviews);
            }
        });

        return view;
    }
}
