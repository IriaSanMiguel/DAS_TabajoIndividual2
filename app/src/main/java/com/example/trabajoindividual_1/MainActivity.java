package com.example.trabajoindividual_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    miDB db;

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
        username.setText(i.getStringExtra("usernameText"));
        contrasena.setText(i.getStringExtra("contrasenaText"));

        db = new miDB(this, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.opcion1:{ //Si selecciona cambiar idioma
                cambiarIdioma();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void cambiarIdioma(){
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
        EditText username = (EditText) findViewById(R.id.editText_nombreUsuario);
        EditText contrasena = (EditText) findViewById(R.id.editText_contrasena);
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("usernameText", username.getText().toString());
        i.putExtra("contrasenaText", contrasena.getText().toString());

        // Reiniciamos la actividad
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que quieres salir?")
                .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        builder.create().dismiss();
                    }
                })
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent_finalizar = new Intent(Intent.ACTION_MAIN);
                        finish();
                        startActivity(intent_finalizar);
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

        public void onClickCrearCuenta(View v) {
        Intent i = new Intent(this, CrearCuentaActivity.class);
        finish();
        startActivity(i);
    }

    public void onClickLogIn(View v) {


        EditText username = (EditText) findViewById(R.id.editText_nombreUsuario);
        EditText contrasena = (EditText) findViewById(R.id.editText_contrasena);
        String usernameText = username.getText().toString();
        String contrasenaText = contrasena.getText().toString();

        // Comprobar que no estén vacíos
        if (usernameText.equals("")) {
            username.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            Toast aviso = Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_SHORT);
            aviso.show();
        } else if (contrasenaText.equals("")) {
            contrasena.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            Toast aviso = Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_SHORT);
            aviso.show();
        } else if (!db.existeUsuario(usernameText)) { // Comprobamos si existe un usuario con ese nombre
            username.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            Toast aviso = Toast.makeText(this, "No existe ningún usuario con ese nombre", Toast.LENGTH_SHORT);
            aviso.show();
        } else {
            //Encriptamos la contraseña
            String contrasenaEncriptada = encriptarContrasena(contrasenaText);

            // Comprobamos que el usuario tenga esa contraseña
            if (db.tieneEsaContrasena(usernameText, contrasenaEncriptada)) {
                Intent i = new Intent(this, Principal_Activity.class);
                i.putExtra("username", usernameText);
                finish();
                startActivity(i);
            } else {
                contrasena.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                username.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                Toast aviso = Toast.makeText(this, "Nombre de usuario o contraseña incorrecta", Toast.LENGTH_SHORT);
                aviso.show();
            }
        }
    }

    private String encriptarContrasena(String contrasena) {
        try {
            // Encriptamos la contraseña
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(contrasena.getBytes(StandardCharsets.UTF_8));

            // Pasamos a bytes
            byte[] bytes = messageDigest.digest();

            // Pasamos a formato hexadecimal
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Devolvemos la contraseña ya encritada
            return stringBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }
}