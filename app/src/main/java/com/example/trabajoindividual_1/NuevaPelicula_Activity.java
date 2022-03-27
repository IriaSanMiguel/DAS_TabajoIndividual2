package com.example.trabajoindividual_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Locale;

public class NuevaPelicula_Activity extends AppCompatActivity {
    private miDB db;
    private String username;
    private static final int SELECT_FILE = 1;
    private Bitmap poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_pelicula);
        db = new miDB(this, 1);
        Intent i = getIntent();
        username = i.getStringExtra("username");
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
            Intent i = new Intent(this, NuevaPelicula_Activity.class);
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
    ######################################################## FUNCIONALES #########################################################
    ##############################################################################################################################*/


    public void onClickCrearNuevaPeli(View v) {
        /*
        Pre: Se ha pulsado en el botón "Crear nueva película"
        Post: Se ha creado la nueva película
        */

        // Cargamos los elementos
        EditText textViewTitulo = (EditText) findViewById(R.id.editTextTitulo);
        EditText textViewDirector = (EditText) findViewById(R.id.editTextDirector);
        EditText textViewAnio = (EditText) findViewById(R.id.editTextAnio);
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);

        if (imageView.getDrawable() != null) { // Si se ha cargado una foto
            if (textViewDirector.getText().toString() != "" && textViewTitulo.getText().toString() != "" && textViewAnio.getText().toString() != "") { // Si se han introducido los datos para el resto de campos
                if (db.existePelicula(textViewTitulo.getText().toString())) { // Si ya existe una película con ese nombre
                    textViewTitulo.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    Toast aviso = Toast.makeText(this, getString(R.string.peliculayaexiste), Toast.LENGTH_SHORT);
                    aviso.show();
                    return;
                } else { // Si no existe la película
                    if (db.addPelicula(textViewTitulo.getText().toString(), textViewDirector.getText().toString(), Integer.parseInt(textViewAnio.getText().toString()), getByteArray(imageView))) { // Sis e ha añadido la película correctamente
                        Toast aviso = Toast.makeText(this, getString(R.string.peliculaCreada), Toast.LENGTH_SHORT);
                        aviso.show();
                    } else {
                        Toast aviso = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                        aviso.show();
                        Intent intentError = new Intent(this, Principal_Activity.class);
                        intentError.putExtra("usename", username);
                        finish();
                        startActivity(intentError);
                    }
                }
            } else {
                Toast aviso = Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_SHORT);
                aviso.show();
                return;
            }
        } else {
            Toast aviso = Toast.makeText(this, getString(R.string.introducirimagen), Toast.LENGTH_SHORT);
            aviso.show();
            return;
        }
        // Volvemos a la pantalla principal
        Intent i = new Intent(this, Principal_Activity.class);
        i.putExtra("username", username);
        finish();
        startActivity(i);
    }

    private byte[] getByteArray(ImageView imageView) {
        /*
        Pre: Un ImageView
        Post: Devuelve un byte[] de la imagen del ImageView proporcionado
        */

        // Obtenemos el bitmap
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        // Devolvemos el byte[]
        return byteArrayOutputStream.toByteArray();
    }

    public void saveImage(View v) {
        /*
        Pre: Se ha pulsado sobre la ImageView
        Post: Se ha abierto la galería para que el usuario pueda añadir el poster de la película
        */

        // Abrimos la galería
        Intent intentGaleria = new Intent();
        intentGaleria.setType("image/*");
        intentGaleria.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentGaleria, getString(R.string.introducirImagen)), SELECT_FILE);
    }

    protected void onActivityResult(int codeReq, int codeRes, Intent intent) {
        /*
        Pre: El código de la request, el código de la response y el intent
        Post: Se ha obtenido la imagen seleccionada de la galería anteriormente y se ha guardado en el ImageView
        */

        // Este método se utiliza para obtener la salida de cualquier actividad que se llame desde aquí
        // sólo se cogerá la imagen seleccionada de la galería anteriormente
        super.onActivityResult(codeReq, codeRes, intent);
        Uri image;
        if (codeReq == SELECT_FILE) { // Si coincide con el código de la request asignado anteriormente
            if (codeRes == Activity.RESULT_OK) { // Si la petición ha sido exitosa
                image = intent.getData();
                if (image.getPath() != null) { // Si el path a la imagen no el null
                    InputStream inputStream = null;
                    try {
                        inputStream = getContentResolver().openInputStream(image);
                    } catch (Exception e) {
                        Toast aviso = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT);
                        aviso.show();
                        Intent intentError = new Intent(this, Principal_Activity.class);
                        intentError.putExtra("usename", username);
                        finish();
                        startActivity(intentError);
                    }
                    // Transformamos la URI de la imagen a bitmap
                    poster = BitmapFactory.decodeStream(inputStream);

                    // Guardamos la imagen en el ImageView
                    ImageView imageView = (ImageView) findViewById(R.id.imageView2);
                    imageView.setImageBitmap(poster);
                }
            }
        }
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
        builder.setMessage(getString(R.string.salirsinguardar))
                .setPositiveButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        builder.create().dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.salir), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent_salir = new Intent(getBaseContext(), Principal_Activity.class);
                        intent_salir.putExtra("username", username);
                        finish();
                        startActivity(intent_salir);
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }
}