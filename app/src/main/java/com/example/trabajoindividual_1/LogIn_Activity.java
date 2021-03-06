package com.example.trabajoindividual_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class LogIn_Activity extends AppCompatActivity {

    miDB db;
    BdRemota rdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asignar ActionBar
        setSupportActionBar(findViewById(R.id.toolbar));

        // Mantenemos el texto en los EditText (si se ha reiniciado la actividad por un cambio en el idioma)
        Intent i = getIntent();
        EditText username = (EditText) findViewById(R.id.editText_nombreUsuario);
        EditText contrasena = (EditText) findViewById(R.id.editText_contrasena);
        String textUsername = i.getStringExtra("usernameText");
        String textCon = i.getStringExtra("contrasenaText");
        username.setText(i.getStringExtra("usernameText"));
        contrasena.setText(i.getStringExtra("contrasenaText"));

        db = new miDB(this, 1);
        rdb = new BdRemota();

        // Cargar preferencias
        cargarPreferencias();
    }

    /*############################################################################################################################
    ######################################################## PREFERENCIAS #########################################################
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

        if (!yaCargadas) { // Si no se hab??an cargado antes
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
            Intent i = new Intent(this, LogIn_Activity.class);

            // Mantener el texto introducido en los EditText
            EditText username = (EditText) findViewById(R.id.editText_nombreUsuario);
            EditText contrasena = (EditText) findViewById(R.id.editText_contrasena);
            i.putExtra("usernameText", username.getText().toString());
            i.putExtra("contrasenaText", contrasena.getText().toString());
            finish();
            startActivity(i);
        }
    }

    @Override
    public void onDestroy() {
        // Cuando se cierre la actividad indicamos que las preferencias no est??n cargadas
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
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        Pre: Se ha seleccionado una de las opciones de la toolbar
        Post: Se ha ejecutado la acci??n adecuada
        */
        int id = item.getItemId();
        switch (id) {
            case R.id.opcion1: { // Si selecciona cambiar idioma
                cambiarIdioma();
                break;
            }
            case R.id.opcion2: { // Si se seleccionan las instrucciones
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                verInstrucciones(prefs.getString("Idioma", "es"));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void verInstrucciones(String idioma) {
        /*
        Pre: Se ha seleccionado mostrar las instrucciones
        Post: Se muestran las instrucciones mediante un di??logo
        */

        // Creamos un di??logo para mostrar las instrucciones
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.intrucciones);

        // Abrimos el fichero correcto seg??n el idioma
        InputStream fich;
        switch (idioma) {
            case ("es"): {
                fich = getResources().openRawResource(R.raw.instrucciones_es);
                break;
            }
            case ("en"): {
                fich = getResources().openRawResource(R.raw.instrucciones_en);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + idioma);
        }
        BufferedReader buff = new BufferedReader(new InputStreamReader(fich));
        try {
            // Leemos el fichero
            String text = "";
            String linea;
            while ((linea = buff.readLine()) != null) {
                text = text + linea + "\n";
            }
            fich.close();
            builder.setMessage(text);
            builder.show();
        } catch (Exception e) {
            Toast aviso = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
            aviso.show();
        }

    }

    private void cambiarIdioma() {
        // Creamos un di??logo para elegir el idioma
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.cambiarIdioma);
        String[] languages = {"Castellano", "English"};
        builder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                actualizarIdioma(i);
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
        EditText username = (EditText) findViewById(R.id.editText_nombreUsuario);
        EditText contrasena = (EditText) findViewById(R.id.editText_contrasena);
        Intent i = new Intent(this, LogIn_Activity.class);
        i.putExtra("usernameText", username.getText().toString());
        i.putExtra("contrasenaText", contrasena.getText().toString());

        // Actualizamos las preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Idioma", languages[index]);
        editor.apply();

        // Reiniciamos la actividad
        finish();
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Indicamos la toolbar que se va a usar
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /*############################################################################################################################
    ######################################################## ATR??S #############################################################
    ##############################################################################################################################*/

    @Override
    public void onBackPressed() {
        /*
        Pre: Se ha pulsado el bot??n "hacia atr??s"
        Post: Sale un di??logo preguntando al usuario si quiere salir de esa pantalla
        */

        // Creamos el di??logo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.preguntasalir))
                .setPositiveButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        builder.create().dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.salir), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent_finalizar = new Intent(Intent.ACTION_MAIN);
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


    public void onClickCrearCuenta(View v) {
        /*
        Pre: Se ha pulsado en "Crear cuenta"
        Post: Se cambiado a la actividad CrearCuentaActivity
        */
        Intent i = new Intent(this, CrearCuentaActivity.class);
        finish();
        startActivity(i);
    }

    public void onClickLogIn(View v) {
        /*
        Pre: Se ha pulsado en "Inicial sesi??n"
        Post: Si no hay ning??n error se ha cambiado a la actividad Principal_Activity
        */

        EditText username = (EditText) findViewById(R.id.editText_nombreUsuario);
        EditText contrasena = (EditText) findViewById(R.id.editText_contrasena);
        String usernameText = username.getText().toString();
        String contrasenaText = contrasena.getText().toString();

        // Comprobar que no est??n vac??os
        if (usernameText.equals("")) {
            username.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            Toast aviso = Toast.makeText(this, getString(R.string.rellenarcampos), Toast.LENGTH_SHORT);
            aviso.show();
        } else if (contrasenaText.equals("")) {
            contrasena.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            Toast aviso = Toast.makeText(this, getString(R.string.rellenarcampos), Toast.LENGTH_SHORT);
            aviso.show();
        } else if (!rdb.existeUsuario(usernameText)) { // Comprobamos si existe un usuario con ese nombre
            username.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            Toast aviso = Toast.makeText(this, getString(R.string.nousuario), Toast.LENGTH_SHORT);
            aviso.show();
        } else {

            // Comprobamos que el usuario tenga esa contrase??a
            if (rdb.tieneEsaContrasena(usernameText, contrasenaText)) {
                rdb.getToken(usernameText);
                Intent i = new Intent(this, Principal_Activity.class);
                i.putExtra("username", usernameText);
                finish();
                startActivity(i);
            } else {
                contrasena.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                username.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                Toast aviso = Toast.makeText(this, getString(R.string.incorrecto), Toast.LENGTH_SHORT);
                aviso.show();
            }
        }
    }
}