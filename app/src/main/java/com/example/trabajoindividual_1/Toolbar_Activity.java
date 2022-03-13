package com.example.trabajoindividual_1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Toolbar_Activity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.principal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.opcion1:{ //Si selecciona cambiar idioma
                cambiarIdioma();
                break;
            }case R.id.opcion2:{
                Intent intent_datos = new Intent(this, MyAccount_Activity.class);
                startActivity(intent_datos); }
        }
        return super.onOptionsItemSelected(item);
    }

    public void cambiarIdioma(){
        // Creamos un diálogo para elegir el idioma
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

        // Reiniciamos la actividad
        finish();
        startActivity(i);


    }

}
