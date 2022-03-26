package com.example.trabajoindividual_1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;

public class CrearCuentaActivity extends AppCompatActivity {

    miDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccount);
        db = new miDB(this, 1);

        // Asignar ActionBar
        setSupportActionBar(findViewById(R.id.toolbar2));

        // Mantenemos el texto en los EditText (si se ha reiniciado la actividad por un cambio en el idioma)
        Intent i = getIntent();
        EditText username = (EditText) findViewById(R.id.editText_username);
        EditText contrasena1 = (EditText) findViewById(R.id.editText_password1);
        EditText contrasena2 = (EditText) findViewById(R.id.editText_password2);
        EditText nombre = (EditText) findViewById(R.id.editText_nombre);
        EditText apellido = (EditText) findViewById(R.id.editText_apellido);
        username.setText(i.getStringExtra("usernameText"));
        contrasena1.setText(i.getStringExtra("contrasena1Text"));
        contrasena2.setText(i.getStringExtra("contrasena2Text"));
        nombre.setText(i.getStringExtra("nombreText"));
        apellido.setText(i.getStringExtra("apellidoText"));
        // Cargar preferencias
        cargarPreferencias();
    }

    private void cargarPreferencias(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
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
            Configuration conf = getBaseContext().getResources().getConfiguration();
            conf.setLocale(locale);
            conf.setLayoutDirection(locale);
            Context context = getBaseContext().createConfigurationContext(conf);
            getBaseContext().getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
            Intent i = new Intent(this, CrearCuentaActivity.class);
            // Mantener el texto introducido en los EditText
            EditText username = (EditText) findViewById(R.id.editText_username);
            EditText contrasena1 = (EditText) findViewById(R.id.editText_password1);
            EditText contrasena2 = (EditText) findViewById(R.id.editText_password2);
            EditText nombre = (EditText) findViewById(R.id.editText_nombre);
            EditText apellido = (EditText) findViewById(R.id.editText_apellido);
            i.putExtra("usernameText", username.getText().toString());
            i.putExtra("contrasena1Text", contrasena1.getText().toString());
            i.putExtra("contrasena2Text", contrasena2.getText().toString());
            i.putExtra("nombreText", nombre.getText().toString());
            i.putExtra("apellidoText", apellido.getText().toString());
            finish();
            startActivity(i);
        }
    }

    @Override
    public void onDestroy() {
        // Cuando se cierre la actividad indicamos que las preferencias no están cargadas
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("PrefsCargadas", false);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.preguntasalir))
                .setPositiveButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        builder.create().dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.salir), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent_salir = new Intent(getBaseContext(), MainActivity.class);
                        finish();
                        startActivity(intent_salir);
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void actualizarIdioma(int index) {
        String[] languages = {"es", "en"};
        // Actualizamos las preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Idioma", languages[index]);
        editor.apply();

        // Mantener el texto introducido en los EditText
        EditText username = (EditText) findViewById(R.id.editText_username);
        EditText contrasena1 = (EditText) findViewById(R.id.editText_password1);
        EditText contrasena2 = (EditText) findViewById(R.id.editText_password2);
        EditText nombre = (EditText) findViewById(R.id.editText_nombre);
        EditText apellido = (EditText) findViewById(R.id.editText_apellido);
        Intent i = new Intent(this, CrearCuentaActivity.class);
        i.putExtra("usernameText", username.getText().toString());
        i.putExtra("contrasena1Text", contrasena1.getText().toString());
        i.putExtra("contrasena2Text", contrasena2.getText().toString());
        i.putExtra("nombreText", nombre.getText().toString());
        i.putExtra("apellidoText", apellido.getText().toString());

        // Reiniciamos la actividad
        finish();
        startActivity(i);
    }

    private void cambiarIdioma() {
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

    private boolean hayVacios(String username, String password1, String password2, String nombre, String apellido) {
        //Pre:
        //Post: Devuelve true si hay algún campo que no se ha rellenado

        if (username.equals("") || password1.equals("") || password2.equals("") || nombre.equals("") || apellido.equals("")) {
            return true;
        }
        return false;
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

    public void onClickCrearCuenta(View v) {
        //Pre: Se ha pulsado el botón "Crear cuenta"
        //Post: Si están todos los campos correctamente se habrá creado un nuevo usuario

        // Obtenemos todos los EditText
        EditText username = (EditText) findViewById(R.id.editText_username);
        EditText password1 = (EditText) findViewById(R.id.editText_password1);
        EditText password2 = (EditText) findViewById(R.id.editText_password2);
        EditText nombre = (EditText) findViewById(R.id.editText_nombre);
        EditText apellido = (EditText) findViewById(R.id.editText_apellido);

        // Comprobamos si hay algún campo vacío
        if (hayVacios(username.getText().toString(), password1.getText().toString(), password2.getText().toString(), nombre.getText().toString(), apellido.getText().toString())) {
            Toast aviso = Toast.makeText(this, getString(R.string.rellenarcampos), Toast.LENGTH_SHORT);
            aviso.show();
        } else if (db.existeUsuario(username.getText().toString())) { // Comprobamos que no exista un usuario con ese nombre
            username.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            Toast aviso = Toast.makeText(this, getString(R.string.usuarioyaenuso), Toast.LENGTH_SHORT);
            aviso.show();
        } else if (!password1.getText().toString().equals(password2.getText().toString())) { // Comprobamos que las dos contraseñas coincidan
            password2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            Toast aviso = Toast.makeText(this, getString(R.string.contrasenasnocoinciden), Toast.LENGTH_SHORT);
            aviso.show();
        } else {
            // Obtenemos la contraseña ya encritada
            String contrasena = encriptarContrasena(password1.getText().toString());

            //Comprobamos que la contraseña se ha encriptado correctamente
            if (!contrasena.equals(null)) {

                // Añadimos el usuario a la base de datos
                db.addUsuario(username.getText().toString(), nombre.getText().toString(), apellido.getText().toString(), contrasena);

                // Creamos una notificación local indicándole al usuario que se ha creado correctamente su cuenta
                NotificationManager elManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "IdCanal");
                NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal", NotificationManager.IMPORTANCE_DEFAULT);

                elCanal.setDescription("Canal por el que se notificará que se ha creado correctamente la cuenta");
                elCanal.enableLights(true);
                elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                elCanal.enableVibration(true);

                elManager.createNotificationChannel(elCanal);
                elBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.baseline_thumb_up_24))
                        .setSmallIcon(R.drawable.baseline_thumb_up_24)
                        .setContentTitle(getString(R.string.cuentacreada))
                        .setContentText(getString(R.string.cuentacreadacorrectamente))
                        .setVibrate(new long[]{0, 1000, 500, 1000})
                        .setAutoCancel(true);
                elManager.notify(1, elBuilder.build());

                // Volvemos a la pantalla de inicio
                Intent i = new Intent(this, MainActivity.class);
                finish();
                startActivity(i);
            } else {
                Toast avisoError = Toast.makeText(this, getString(R.string.errorencriptar), Toast.LENGTH_SHORT);
                avisoError.show();
            }


        }
    }
}
