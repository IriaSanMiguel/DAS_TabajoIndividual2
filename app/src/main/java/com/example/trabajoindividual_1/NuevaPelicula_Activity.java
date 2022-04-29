package com.example.trabajoindividual_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NuevaPelicula_Activity extends AppCompatActivity {
    private miDB db;
    private String username;
    private static final int SELECT_FILE = 1;
    private static final int CAMERA = 2;
    private static final int PERMISO_CAMERA = 10;
    private Bitmap poster;
    private String nombrePoster;

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

                    // Creamos un nombre único al poster de la película
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    nombrePoster = "IMG_" + timeStamp + "_";

                    if (db.addPelicula(textViewTitulo.getText().toString(), textViewDirector.getText().toString(), Integer.parseInt(textViewAnio.getText().toString()), nombrePoster)) { // Sis e ha añadido la película correctamente
                        // Guardamos la imagen
                        guardarImagen();
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


    public void selectImage(View v) {
        /*
        Pre: Se ha pulsado sobre la ImageView
        Post: Se ha abierto la galería/cámara/google drive para que el usuario pueda añadir el poster de la película
        */

        // Creamos un diálogo para elegir el idioma
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.cambiarIdioma);
        String[] languages = {"Galería", "Cámara", "Google Drive"};
        builder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0: // Si se ha elegido la galería
                        saveGalleryImage();
                        dialogInterface.dismiss();
                        break;
                    case 1: // Si se ha elegido la cámara
                        saveCameraImage();
                        dialogInterface.dismiss();
                        break;
                    case 2: // Si se ha elegido seleccionar la foto desde google drive
                        saveImagenGoogleDrive();
                        dialogInterface.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    private void saveImagenGoogleDrive() {
        /*
        Pre: Se ha pulsado sobre la ImageView y se ha seleccionado elegir una imagen de GoogleDrive
        Post: Se ha abierto Google Drive para que el usuario pueda seleccionar una foto al póster de la película
        */



    }

    private boolean comprobarPlayServices(){
        /*
        Pre: Se ha seleccionado Google Drive para seleccionar una imagen
        Post: True si Google Play Services está disponible, False en caso contrario
        */

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);
        if (code == ConnectionResult.SUCCESS) {
            return true;
        }
        else {
            if (api.isUserResolvableError(code)){
                api.getErrorDialog(this, code, 58).show();
            }
            return false;
        }
    }


    private void saveCameraImage() {
        /*
        Pre: Se ha pulsado sobre la ImageView y se ha seleccionado sacar una nueva imagen con la cámara
        Post: Se ha abierto la cámara para que el usuario saque una foto al póster de la película
        */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { // Si el permiso no está concedido
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) { // Si no nos ha dado el permiso
                Toast aviso = Toast.makeText(this, getString(R.string.permiso), Toast.LENGTH_SHORT);
                aviso.show();
            }
            // Pedimos el permiso
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISO_CAMERA);
        } else { // Si tenemos permiso
            // Creamos el intent
            Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intentCamara, CAMERA);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISO_CAMERA: {
                // Si la petición se cancela, granResults estará vacío
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISO CONCEDIDO, EJECUTAR LA FUNCIONALIDAD
                    // Creamos el intent
                    Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentCamara, CAMERA);
                }
                return;
            }
        }
    }


    private void saveGalleryImage() {
        /*
        Pre: Se ha pulsado sobre la ImageView y se ha seleccionado elegir imagen desde la galería
        Post: Se ha abierto la galería para que el usuario pueda añadir el poster de la película
        */

        // Abrimos la galería
        Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intentGaleria, getString(R.string.introducirImagen)), SELECT_FILE);
    }

    private void guardarImagen() {
        /*
        Pre: El usuario ha seleccionado una imagen (ya sea desde la cámara, la galería o google drive)
        Post: Se guarda la imagen en el servidor
        */
        // Creamos un nuevo Thread
        Thread thread = new Thread() {
            public void run() {

                // Creamos un byte[] del poster
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                poster.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] posterByteArray = stream.toByteArray();
                try {
                    String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/isanmiguel008/WEB/uploadImage.php";
                    URL destino = new URL(direccion);

                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", nombrePoster, RequestBody.create(posterByteArray))
                            .build();
                    Request request = new Request.Builder().url(destino).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) { // Si la petición se ha ejecutado correctamento
                        Log.d("http", "OK");
                    } else { // Si algo ha salido mal
                        Log.d("http", "Algo ha salido mal");
                        Log.d("http", response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.run();
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
        if (codeReq == SELECT_FILE) { // Si coincide con el código de la request asignado a seleccionar una foto de la galería
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
                    // Guardamos la imagen en el servidor
                    Log.d("http", "Guardamos imagen en el servidor");
                }
            }
        } else if (codeReq == CAMERA) {// Si coincide con el código de la request asignado a sacar una foto con la cámara
            if (codeRes == Activity.RESULT_OK) { // Si la petición ha sido exitosa
                Bundle extras = intent.getExtras();
                poster = (Bitmap) extras.get("data");

                // Guardamos la imagen en el ImageView
                ImageView imageView = (ImageView) findViewById(R.id.imageView2);
                imageView.setImageBitmap(poster);
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