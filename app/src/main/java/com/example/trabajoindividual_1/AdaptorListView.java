package com.example.trabajoindividual_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    public AdaptorListView(Context pcontext, String[] nombresPeliculas, int[] imagenesPeliculas, float[] ppuntuaciones){
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

        return view;
    }
}
