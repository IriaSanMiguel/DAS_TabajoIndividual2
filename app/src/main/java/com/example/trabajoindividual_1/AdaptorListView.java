package com.example.trabajoindividual_1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class AdaptorListView extends BaseAdapter {
    private String[] peliculas;
    private int[] posters;
    private float[] puntuaciones;
    private LayoutInflater inflater;
    private Context contexto;

    public AdaptorListView(Context pcontext, String[] nombresPeliculas, int[] imagenesPeliculas, float[] ppuntuaciones) {
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

        view = inflater.inflate(R.layout.listview_peliculas, null);

        // Nombre de la película
        TextView titulo = (TextView) view.findViewById(R.id.textViewTituloPeli);
        titulo.setText(peliculas[i]);

        // Póster de la película
        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        image.setImageResource(posters[i]);

        // Puntuación de la película
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar_listView);
        ratingBar.setRating(puntuaciones[i]);

        Button botonReview = (Button) view.findViewById(R.id.button_review);
        botonReview.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = ((Activity) viewGroup.getContext()).getIntent();
                String sdf = intent.getStringExtra("username");
                Intent intent_reviews = new Intent(viewGroup.getContext(), Review_Activity.class);
                intent_reviews.putExtra("username", sdf);
                intent_reviews.putExtra("tituloPelicula", peliculas[i]);
                viewGroup.getContext().startActivity(intent_reviews);
            }
        });

        Button botonDescripcion = (Button) view.findViewById(R.id.button_descripcion);
        botonDescripcion.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View view) {
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
