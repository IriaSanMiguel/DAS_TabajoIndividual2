package com.example.trabajoindividual_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class NuevaPelicula_Activity extends AppCompatActivity {
    private miDB db;
    private String username;
    private static final int SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_pelicula);
        db = new miDB(this, 1);
        Intent i = getIntent();
        username = i.getStringExtra("username");
    }

    public void onClickCrearNuevaPeli(View v) {
        EditText textViewTitulo = (EditText) findViewById(R.id.editTextTitulo);
        EditText textViewDirector = (EditText) findViewById(R.id.editTextDirector);
        EditText textViewAnio = (EditText) findViewById(R.id.editTextAnio);
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        if (imageView.getDrawable() != null) { // Si se ha cargado una foto
            if (textViewDirector.getText().toString() != "" && textViewTitulo.getText().toString() != "" && textViewAnio.getText().toString() != "") { // Si se han introducido los datos para el resto de campos
                db.addPelicula(textViewTitulo.getText().toString(), textViewDirector.getText().toString(), Integer.getInteger(textViewAnio.getText().toString()), getByteArray(imageView));
            }else {
                Toast aviso = Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_SHORT);
                aviso.show();
            }
            Toast aviso = Toast.makeText(this, "Por favor introduzca una imagen", Toast.LENGTH_SHORT);
            aviso.show();
        }
        Intent i = new Intent(this, Principal_Activity.class);
        i.putExtra("username", username);
        finish();
        startActivity(i);
    }

    private byte[] getByteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void saveImage(View v){
        // Abrimos la galería
        Intent intentGaleria = new Intent();
        intentGaleria.setType("image/*");
        intentGaleria.setAction(Intent.ACTION_GET_CONTENT);
        startActiv
    }
}