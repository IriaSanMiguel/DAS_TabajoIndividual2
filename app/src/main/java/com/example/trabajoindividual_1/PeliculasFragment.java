package com.example.trabajoindividual_1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.nio.FloatBuffer;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PeliculasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeliculasFragment extends Fragment {
    private miDB db;
    private String titulo;
    private String username;

    public PeliculasFragment() {
        // Required empty public constructor
    }

    public static PeliculasFragment newInstance() {
        return new PeliculasFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new miDB(getActivity(), 1);
        Intent i = getActivity().getIntent();
        username = i.getStringExtra("username");
        titulo = i.getStringExtra("tituloPelicula");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_peliculas, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView textViewTitulo = (TextView) getView().findViewById(R.id.textView_Titulo);
        TextView textViewDirector = (TextView) getView().findViewById(R.id.textView_Director);
        TextView textView_Anio = (TextView) getView().findViewById(R.id.textView_Anio);
        ImageView posterPelicula = (ImageView) getView().findViewById(R.id.posterPelicula);
        RatingBar ratingBar = (RatingBar) getView().findViewById(R.id.ratingBar_Pelicula);


        JSONObject json = db.getDatosPelicula(titulo);

        if (json != null) { // Si no ha habido ningún error
            try {
                textViewTitulo.setText(titulo);
                textView_Anio.setText(json.getString("Anio"));
                textViewDirector.setText(json.getString("Director"));
                byte[] poster = (byte[]) json.get("Poster");
                posterPelicula.setImageBitmap(BitmapFactory.decodeByteArray(poster, 0, poster.length));
                ratingBar.setRating(Float.valueOf(json.getString("PuntuacionMedia")));
            } catch (Exception e) {
                Toast aviso = Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT);
                aviso.show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("username", username);
                getActivity().finish();
                startActivity(intent);
            }
        } else {
            Toast aviso = Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT);
            aviso.show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("username", username);
            getActivity().finish();
            startActivity(intent);
        }
        // Asignamos las funciones onClick de los botones
        Button botonReview = (Button) getView().findViewById(R.id.buttom_hacerResena);
        botonReview.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEscribirReview();
            }
        });

        Button botonVerReviews = (Button) getView().findViewById(R.id.button_verResenas);
        botonVerReviews.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickVerReviews();
            }
        });
        // Cargar preferencias
        cargarPreferencias();
    }

    private void cargarPreferencias(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
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
            Configuration conf = getActivity().getBaseContext().getResources().getConfiguration();
            conf.setLocale(locale);
            conf.setLayoutDirection(locale);
            Context context = getActivity().getBaseContext().createConfigurationContext(conf);
            getActivity().getBaseContext().getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
            Intent i = new Intent(getActivity(), Pelicula_Activity.class);
            i.putExtra("username", username);
            i.putExtra("tituloPelicula", titulo);
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

    private void onClickVerReviews() {
        Intent i = new Intent(getActivity(), ListaReviews_Activity.class);
        i.putExtra("tituloPelicula", titulo);
        startActivity(i);
    }

    private void onClickEscribirReview() {
        Intent i = new Intent(getActivity(), Review_Activity.class);
        i.putExtra("username", username);
        i.putExtra("tituloPelicula", titulo);
        startActivity(i);
    }


}