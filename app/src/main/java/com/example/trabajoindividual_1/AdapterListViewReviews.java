package com.example.trabajoindividual_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class AdapterListViewReviews extends BaseAdapter {
    private String[] lUsers;
    private String[] lReviews;
    private float[] lRatings;
    private LayoutInflater inflater;
    private Context contexto;

    public AdapterListViewReviews(Context pcontext, String[] pUsers, String[] pReviews, float[] pRatings) {
        /*
        Pre: Un contexto, una lista de los nombres de los usuarios, de las reviews y de las puntuaciones
        Post: Se ha creado el ListView con las reviews de cada usuario y su puntuación
        */
        contexto = pcontext;
        lUsers = pUsers;
        lReviews = pReviews;
        lRatings = pRatings;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lUsers.length;
    }

    @Override
    public Object getItem(int i) {
        return lReviews[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        /*
        Pre: El índice de la review, la view y la viewGroup
        Post: Se ha creado correctamente el listView
        */

        view = inflater.inflate(R.layout.listview_review, null);

        // Escribimos los datos

        // Nombre del usuario
        TextView textView_username = (TextView) view.findViewById(R.id.textView_username);
        textView_username.setText(lUsers[i]);

        // Review
        TextView textView_review = (TextView) view.findViewById(R.id.textView_review);
        textView_review.setText(lReviews[i]);

        // Puntuación rating bar
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratinfBar_reviews);
        ratingBar.setRating(lRatings[i]);

        return view;
    }
    @Override
    public boolean isEnabled(int position) {
        // Bloqueamos el click en el elemento
        return false;
    }
}
