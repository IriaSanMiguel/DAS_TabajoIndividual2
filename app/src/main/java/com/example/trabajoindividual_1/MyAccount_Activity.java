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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;

public class MyAccount_Activity extends AppCompatActivity {
    miDB db;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        db = new miDB(this, 1);
        // Asignar ActionBar
        setSupportActionBar(findViewById(R.id.toolbar_myAccount));

        //Obtenemos en nombre de usuario
        Intent i = getIntent();
        username = i.getStringExtra("username");
        JSONObject json = db.getDatosUsuario(username);
        if (json == null) { //Si ha ocurrido un error
            Toast aviso = Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT);
            aviso.show();
        } else {
            try {
                TextView usernameTextView = (TextView) findViewById(R.id.textUsername);
                TextView contrasenaTextView = (TextView) findViewById(R.id.textContrasena);
                TextView nombreTextView = (TextView) findViewById(R.id.textNombre);
                TextView apellidoTextView = (TextView) findViewById(R.id.textApellido);

                EditText usernameEditText = (EditText) findViewById(R.id.editarUsername);
                EditText contrasenaEditText = (EditText) findViewById(R.id.editarContrasena);
                EditText nombreEditText = (EditText) findViewById(R.id.editarNombre);
                EditText apellidoEditText = (EditText) findViewById(R.id.editarApellido);

                Button boton = (Button) findViewById(R.id.buttonGuardar);

                // Definimos los textos de los TextViews
                usernameTextView.setText(getString(R.string.usernameActual) + ": " + username);
                contrasenaTextView.setText(getString(R.string.contrasenaActual));
                nombreTextView.setText(getString(R.string.nombreActual) + ": " + json.getString("Nombre"));
                apellidoTextView.setText(getString(R.string.apellidoActual) + ": " + json.getString("Apellido"));

                // Ocultamos los EditText y el botón
                usernameEditText.setVisibility(View.INVISIBLE);
                contrasenaEditText.setVisibility(View.INVISIBLE);
                nombreEditText.setVisibility(View.INVISIBLE);
                apellidoEditText.setVisibility(View.INVISIBLE);
                boton.setVisibility(View.INVISIBLE);

                // Cambiamos el margen izquierdo de los TextViews
                LinearLayout.LayoutParams paramsUsernameTextView = (LinearLayout.LayoutParams) usernameTextView.getLayoutParams();
                LinearLayout.LayoutParams paramsContrasenaTextView = (LinearLayout.LayoutParams) contrasenaTextView.getLayoutParams();
                LinearLayout.LayoutParams paramsNombreTextView = (LinearLayout.LayoutParams) nombreTextView.getLayoutParams();
                LinearLayout.LayoutParams paramsApellidoTextView = (LinearLayout.LayoutParams) apellidoTextView.getLayoutParams();
                paramsUsernameTextView.setMargins(200,0,0,0);
                usernameTextView.setLayoutParams(paramsUsernameTextView);
                paramsContrasenaTextView.setMargins(200,0,0,0);
                contrasenaTextView.setLayoutParams(paramsContrasenaTextView);
                paramsNombreTextView.setMargins(200,0,0,0);
                nombreTextView.setLayoutParams(paramsNombreTextView);
                paramsApellidoTextView.setMargins(200,0,0,0);
                apellidoTextView.setLayoutParams(paramsApellidoTextView);


            } catch (Exception e) {
                Toast aviso = Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT);
                aviso.show();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void onClickUsername(View v) {
        EditText usernameEditText = (EditText) findViewById(R.id.editarUsername);
        TextView usernameTextView = (TextView) findViewById(R.id.textUsername);
        usernameTextView.setText(getString(R.string.cambiarUsername));
        Button boton = (Button) findViewById(R.id.buttonGuardar);
        boton.setVisibility(View.VISIBLE);
        usernameEditText.setVisibility(View.VISIBLE);

    }

    public void onClickContrasena(View v) {
        EditText contrasenaEditText = (EditText) findViewById(R.id.editarContrasena);
        TextView contrasenaTextView = (TextView) findViewById(R.id.textContrasena);
        contrasenaTextView.setText(getString(R.string.cambiarContraseña));
        Button boton = (Button) findViewById(R.id.buttonGuardar);
        boton.setVisibility(View.VISIBLE);
        contrasenaEditText.setVisibility(View.VISIBLE);
    }

    public void onClickNombre(View v) {
        EditText nombreEditText = (EditText) findViewById(R.id.editarNombre);
        TextView nombreTextView = (TextView) findViewById(R.id.textNombre);
        nombreTextView.setText(getString(R.string.cambiarNombre));
        Button boton = (Button) findViewById(R.id.buttonGuardar);
        boton.setVisibility(View.VISIBLE);
        nombreEditText.setVisibility(View.VISIBLE);
    }

    public void onClickApellido(View v) {
        EditText apellidoEditText = (EditText) findViewById(R.id.editarApellido);
        TextView apellidoTextView = (TextView) findViewById(R.id.textApellido);
        apellidoTextView.setText(getString(R.string.cambiarApellido));
        Button boton = (Button) findViewById(R.id.buttonGuardar);
        boton.setVisibility(View.VISIBLE);
        apellidoEditText.setVisibility(View.VISIBLE);
    }

    public void onClickGuardar(View v) {
        EditText usernameEditText = (EditText) findViewById(R.id.editarUsername);
        EditText contrasenaEditText = (EditText) findViewById(R.id.editarContrasena);
        EditText nombreEditText = (EditText) findViewById(R.id.editarNombre);
        EditText apellidoEditText = (EditText) findViewById(R.id.editarApellido);

        Boolean hayAlgoEscrito = false;
        Boolean error = false;

        if (!usernameEditText.getText().toString().equals("")) {
            if(db.existeUsuario(usernameEditText.getText().toString())){ // Si ya existe un usuario con ese username
                usernameEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                Toast aviso = Toast.makeText(this, "Ese nombre de usuario ya está en uso", Toast.LENGTH_SHORT);
                aviso.show();
                return;
            }
            hayAlgoEscrito = true;
            error = !db.updateUsuarioUsername(username, usernameEditText.getText().toString());
            username = usernameEditText.getText().toString();
        }
        if (!contrasenaEditText.getText().toString().equals("")) {
            String contrasena = encriptarContrasena(contrasenaEditText.getText().toString());
            hayAlgoEscrito = true;
            error = !db.updateUsuarioContrasena(username, contrasena);
        }
        if (!nombreEditText.getText().toString().equals("")) {
            hayAlgoEscrito = true;
            error = !db.updateUsuarioNombre(username, nombreEditText.getText().toString());
        }
        if (!apellidoEditText.getText().toString().equals("")) {
            hayAlgoEscrito = true;
            error = !db.updateUsuarioApellido(username, apellidoEditText.getText().toString());
        }
        if (error) { // Si no se ha actualizado algo bien
            Toast aviso = Toast.makeText(this, "Ha ocurrido un error, por favor vuelva a introducir los datos", Toast.LENGTH_SHORT);
            aviso.show();
            return;
        }
        if (!hayAlgoEscrito) { // Si no hay nada escrito
            Toast aviso = Toast.makeText(this, "Por favor rellene los campos antes de guardarlos", Toast.LENGTH_SHORT);
            aviso.show();
            return;
        }
        Toast aviso = Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT);
        aviso.show();
        Intent i = new Intent(this, this.getClass());
        i.putExtra("username", username);
        finish();
        startActivity(i);
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
        startActivity(i);
        finish();
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
}