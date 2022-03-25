package com.example.trabajoindividual_1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

        // Creamos la carpeta para guardar los posters
        Context thethis = this.getBaseContext();

        String file_path = "/posters";

        File dir = new File(file_path);

        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.opcion1: { //Si selecciona cambiar idioma
                cambiarIdioma();
                break;
            }
            case R.id.opcion2: {
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

        // Cambiar el idioma
        String[] languages = {"es", "en"};
        Locale locale = new Locale(languages[index]);
        Locale.setDefault(locale);
        Configuration conf = getBaseContext().getResources().getConfiguration();
        conf.setLocale(locale);
        conf.setLayoutDirection(locale);
        Context context = getBaseContext().createConfigurationContext(conf);
        getBaseContext().getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());

        // Mantener el texto introducido en los EditText
        Intent i = new Intent(this, this.getClass());
        i.putExtra("username", username);

        // Reiniciamos la actividad
        finish();
        startActivity(i);
    }

    public void onClickNuevaPeli(View v){
        Intent i = new Intent(this, NuevaPelicula_Activity.class);
        i.putExtra("username",username);
        finish();
        startActivity(i);
    }
}
