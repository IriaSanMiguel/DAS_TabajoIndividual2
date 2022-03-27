package com.example.trabajoindividual_1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Locale;

public class Principal_Activity extends AppCompatActivity {
    miDB db;
    String username;
    String[] lTitulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_principal);
        db = new miDB(this, 1);
        // Asignar ActionBar
        setSupportActionBar(findViewById(R.id.toolbar3));

        // Obtenemos el nombre del usuario
        Intent i = getIntent();
        username = i.getStringExtra("username");

        // Conseguimos los datos para la ListView
        JSONObject json = db.getInfoPeliculas();
        if (json == null) {
            Toast aviso = Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT);
            aviso.show();
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        } else {
            try {
                lTitulos = (String[]) json.get("lTitulos");
                byte[][] lPosters = (byte[][]) json.get("lPosters");
                float[] lPuntuacionMedia = (float[]) json.get("lPuntuacionMedia");

                // Completamos la ListView
                AdaptorListView eladap = new AdaptorListView(getApplicationContext(), lTitulos, lPosters, lPuntuacionMedia);
                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(eladap);


            } catch (Exception e) {
                Toast aviso = Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT);
                aviso.show();
                Intent intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        }

        // Cargar preferencias
        cargarPreferencias();
    }

    /*############################################################################################################################
    ######################################################## PREFERENCIAS ########################################################
    ##############################################################################################################################*/


    private void cargarPreferencias() {
        /*
        Pre:
        Post: Se han cargado las preferencias
        */

        // Obtenemos las preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("Idioma", "es");
        Boolean yaCargadas = prefs.getBoolean("PrefsCargadas", false);
        if (!yaCargadas) { // Si no se habían cargado antes
            Locale locale;
            switch (idioma) { // Cambiamos el idioma
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

            // Cambiamos el idioma
            Locale.setDefault(locale);
            Configuration conf = getBaseContext().getResources().getConfiguration();
            conf.setLocale(locale);
            conf.setLayoutDirection(locale);
            Context context = getBaseContext().createConfigurationContext(conf);
            getBaseContext().getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
            Intent i = new Intent(this, Principal_Activity.class);
            i.putExtra("username", username);
            finish();
            startActivity(i);
        }
    }

    @Override
    public void onDestroy() {
        /*
        Pre: Se ha cerrado la actividad
        Post: Se han actualizado las preferencias
        */

        // Cuando se cierre la actividad indicamos que las preferencias no están cargadas
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("PrefsCargadas", false);
        editor.apply();
    }

    /*############################################################################################################################
    ######################################################## TOOLBAR #############################################################
    ##############################################################################################################################*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Indicamos la toolbar que se va a usar
        getMenuInflater().inflate(R.menu.principal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        Pre: Se ha seleccionado una de las opciones de la toolbar
        Post: Se ha ejecutado la acción adecuada
        */

        int id = item.getItemId();
        switch (id) {
            case R.id.opcion1: { //Si selecciona cambiar idioma
                cambiarIdioma();
                break;
            }
            case R.id.opcion2: { // Si selecciona ver sus datos
                Intent intent_datos = new Intent(this, MyAccount_Activity.class);
                intent_datos.putExtra("username", username);
                startActivity(intent_datos);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void cambiarIdioma() {
        // Creamos un diálogo para elegir el idioma
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.cambiarIdioma);
        String[] languages = {"Castellano", "English"};
        builder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                actualizarIdioma(i);
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void actualizarIdioma(int index) {
        /*
        Pre: Se ha seleccionado "Cambiar idioma"
        Post: Se ha cambiado el idioma al seleccionado
        */

        String[] languages = {"es", "en"};

        // Mantener el texto introducido en los EditText
        Intent i = new Intent(this, this.getClass());
        i.putExtra("username", username);

        // Actualizamos las preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Idioma", languages[index]);
        editor.apply();

        // Reiniciamos la actividad
        finish();
        startActivity(i);
    }

    /*############################################################################################################################
    ######################################################## ATRÁS #############################################################
    ##############################################################################################################################*/

    @Override
    public void onBackPressed() {
        /*
        Pre: Se ha pulsado el botón "hacia atrás"
        Post: Sale un diálogo preguntando al usuario si quiere salir de esa pantalla
        */

        // Creamos el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Cerrar sesión?")
                .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        builder.create().dismiss();
                    }
                })
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent_finalizar = new Intent(getBaseContext(), MainActivity.class);
                        finish();
                        startActivity(intent_finalizar);
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    /*############################################################################################################################
    ######################################################## FUNCIONALES #########################################################
    ##############################################################################################################################*/

    public void onClickNuevaPeli(View v) {
        /*
        Pre: Se ha pulsado en "Crear nueva película"
        Post: Se ha cambiado a la actividad NuevaPelicula_Activity
        */

        Intent i = new Intent(this, NuevaPelicula_Activity.class);
        i.putExtra("username", username);
        finish();
        startActivity(i);
    }
}
