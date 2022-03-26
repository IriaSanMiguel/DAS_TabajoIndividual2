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

    // TODO: Rename and change types and number of parameters
    public static ListaReviewsFragment newInstance(String param1, String param2) {
        ListaReviewsFragment fragment = new ListaReviewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        miDB db = new miDB(getActivity(), 1);
        pelicula = getActivity().getIntent().getStringExtra("tituloPelicula");
        JSONObject json = db.getReviewsDePelicula(pelicula);
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

        // Cargar preferencias
        cargarPreferencias();
    }

    private void cargarPreferencias() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        String idioma = prefs.getString("Idioma", "es");
        Boolean yaCargadas = prefs.getBoolean("PrefsCargadas", false);
        if (!yaCargadas) {
            Locale locale;
            switch (idioma) {
                case "es": {
                    locale = new Locale("es");
                    break;
                }
                case "en": {
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
            Configuration conf = getActivity().getBaseContext().getResources().getConfiguration();
            conf.setLocale(locale);
            conf.setLayoutDirection(locale);
            Context context = getActivity().getBaseContext().createConfigurationContext(conf);
            getActivity().getBaseContext().getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
            Intent i = new Intent(getActivity(), ListaReviews_Activity.class);
            i.putExtra("tituloPelicula", pelicula);
            getActivity().finish();
            startActivity(i);
        }
    }

    @Override
    public void onDestroy() {
        // Cuando se cierre la actividad indicamos que las preferencias no están cargadas
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("PrefsCargadas", false);
        editor.apply();
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
        setListAdapter(new AdapterListViewReviews(getContext(), lUsers, lReviews, lRatings));

    }
}