package com.example.trabajoindividual_1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaReviewsFragment extends ListFragment {
    private String[] lUsers;
    private String[] lReviews;
    private float[] lRatings;
    String pelicula;

    public ListaReviewsFragment() {
        // Required empty public constructor
    }


    public static ListaReviewsFragment newInstance(String param1, String param2) {
        ListaReviewsFragment fragment = new ListaReviewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*
        Pre: Un Bundle
        Post: Se ha creado el fragment de la ListView
        */

        super.onCreate(savedInstanceState);
        miDB db = new miDB(getActivity(), 1);
        pelicula = getActivity().getIntent().getStringExtra("tituloPelicula");
        JSONObject json = db.getReviewsDePelicula(pelicula);
        // Obtenemos los datos necesarios
        if (json != null) {
            try {
                lUsers = (String[]) json.get("lUsers");
                lReviews = (String[]) json.get("lReviews");
                lRatings = (float[]) json.get("lRatings");
            } catch (JSONException e) {
                Toast aviso = Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT);
                aviso.show();
                Intent intent = new Intent(getActivity(), Principal_Activity.class);
                getActivity().finish();
                startActivity(intent);
            }
        } else {
            Toast aviso = Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT);
            aviso.show();
            Intent intent = new Intent(getActivity(), Principal_Activity.class);
            getActivity().finish();
            startActivity(intent);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Asignamos el ListView Adapter y le pasamos los datos necesarios para crear la ListView
        setListAdapter(new AdapterListViewReviews(getContext(), lUsers, lReviews, lRatings));
    }
}