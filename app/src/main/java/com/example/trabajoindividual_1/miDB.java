package com.example.trabajoindividual_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

public class miDB extends SQLiteOpenHelper {
    private static final String nombre_DB = "ReviewsPeliculasUsuarios";
    private Context pcontext;

    public miDB(@Nullable Context context, int version) {
        super(context, nombre_DB, null, version);
        pcontext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Pre:
        //Post: Se han creado todas las tablas de la base de datos correctamente
        sqLiteDatabase.execSQL("CREATE TABLE Usuarios ('NombreUsuario' VARCHAR(255) PRIMARY KEY NOT NULL, 'Nombre'" +
                " VARCHAR(255) NOT NULL, 'Apellido' VARCHAR(255) NOT NULL, 'Contrasena' VARCHAR(255) NOT NULL)");

        sqLiteDatabase.execSQL("CREATE TABLE Peliculas ('Titulo' VARCHAR(255) PRIMARY KEY NOT NULL, 'Director'" +
                " VARCHAR(255) NOT NULL, 'Anio' INTEGER NOT NULL, 'Poster' BLOB NOT NULL, 'PuntuacionMedia' FLOAT NOT NULL)");

        sqLiteDatabase.execSQL("CREATE TABLE Reviews ('Usuario' VARCHAR(255) NOT NULL, 'Pelicula'" +
                " VARCHAR(255) NOT NULL, 'Review' LONGTEXT NOT NULL, 'Puntuacion' INTEGER NOT NULL," +
                "FOREIGN KEY ('Usuario') REFERENCES Usuarios ('NombreUsuario')," +
                "FOREIGN KEY ('Pelicula') REFERENCES Peliculas ('Titulo'))");

        // Creamos algunas películas
        // The Batman
        ContentValues datos_batman = new ContentValues();
        datos_batman.put("Titulo", "The Batman");
        datos_batman.put("Director", "Matt Reeves");
        datos_batman.put("Anio", 2022);
        datos_batman.put("Poster", getByteArray(R.drawable.the_batman));
        datos_batman.put("PuntuacionMedia", 0);
        sqLiteDatabase.insert("Peliculas", null, datos_batman);

        // Uncharted
        ContentValues datos_uncharted = new ContentValues();
        datos_uncharted.put("Titulo", "Uncharted");
        datos_uncharted.put("Director", "Ruben Fleischer");
        datos_uncharted.put("Anio", 2022);
        datos_uncharted.put("Poster", getByteArray(R.drawable.uncharted));
        datos_uncharted.put("PuntuacionMedia", 0);
        sqLiteDatabase.insert("Peliculas", null, datos_uncharted);

        // Dune
        ContentValues datos_dune = new ContentValues();
        datos_dune.put("Titulo", "Dune");
        datos_dune.put("Director", "Denis Villeneuve");
        datos_dune.put("Anio", 2022);
        datos_dune.put("Poster", getByteArray(R.drawable.dune));
        datos_dune.put("PuntuacionMedia", 0);
        sqLiteDatabase.insert("Peliculas", null, datos_dune);

        // King Richard
        ContentValues datos_richard = new ContentValues();
        datos_richard.put("Titulo", "King Richard");
        datos_richard.put("Director", "Reinaldo Marcus Green");
        datos_richard.put("Anio", 2021);
        datos_richard.put("Poster", getByteArray(R.drawable.king_richard));
        datos_richard.put("PuntuacionMedia", 0);
        sqLiteDatabase.insert("Peliculas", null, datos_richard);

        // Clifford
        ContentValues datos_clifford = new ContentValues();
        datos_clifford.put("Titulo", "Clifford");
        datos_clifford.put("Director", "Walt Becker");
        datos_clifford.put("Anio", 2021);
        datos_clifford.put("Poster", getByteArray(R.drawable.clifford));
        datos_clifford.put("PuntuacionMedia", 0);
        sqLiteDatabase.insert("Peliculas", null, datos_clifford);

        // Blade Runner
        ContentValues datos_bladerunner = new ContentValues();
        datos_bladerunner.put("Titulo", "Blade Runner");
        datos_bladerunner.put("Director", "Ridley Scott");
        datos_bladerunner.put("Anio", 1982);
        datos_bladerunner.put("Poster", getByteArray(R.drawable.blade));
        datos_bladerunner.put("PuntuacionMedia", 0);
        sqLiteDatabase.insert("Peliculas", null, datos_bladerunner);


        // Creamos algunos usuarios
        // Todas las contraseñas son 1234
        sqLiteDatabase.execSQL("INSERT INTO Usuarios VALUES ('isanmiguel', 'Iria', 'San Miguel', '81dc9bdb52d04dc20036dbd8313ed055')");
        sqLiteDatabase.execSQL("INSERT INTO Usuarios VALUES ('aitorjus', 'Aitor', 'Perez', '81dc9bdb52d04dc20036dbd8313ed055')");
        sqLiteDatabase.execSQL("INSERT INTO Usuarios VALUES ('iker0610', 'Iker', 'de la Iglesia', '81dc9bdb52d04dc20036dbd8313ed055')");
        sqLiteDatabase.execSQL("INSERT INTO Usuarios VALUES ('PLATASSON', 'Alex', 'Platas', '81dc9bdb52d04dc20036dbd8313ed055')");
        sqLiteDatabase.execSQL("INSERT INTO Usuarios VALUES ('christian', 'Christian', 'Berrocal', '81dc9bdb52d04dc20036dbd8313ed055')");
        sqLiteDatabase.execSQL("INSERT INTO Usuarios VALUES ('pepe125', 'Pepe', 'Perez', '81dc9bdb52d04dc20036dbd8313ed055')");
        sqLiteDatabase.execSQL("INSERT INTO Usuarios VALUES ('58ana_', 'Ana', 'Martinez', '81dc9bdb52d04dc20036dbd8313ed055')");

        // Creamos algunas reviews
        sqLiteDatabase.execSQL("INSERT INTO Reviews VALUES ('isanmiguel', 'The Batman', 'Esta bien, aunque Bruce Wayne es un poco emo.', 3)");
        sqLiteDatabase.execSQL("INSERT INTO Reviews VALUES ('PLATASSON', 'The Batman', 'Muy buen Batman, un Bruce Wayne muy flojo.', 3)");
        sqLiteDatabase.execSQL("INSERT INTO Reviews VALUES ('iker0610', 'The Batman', 'Pese a ser la característica principal del villano a priori principal, " +
                "los acertijos quedan muy en tercer plano y son muy simplones. Alfred podría haber sido bastante mejor... P.D. Hasta los bots de fortnite " +
                "tienen mejor puntería que los guardas del pinguino.', 3)");
        sqLiteDatabase.execSQL("INSERT INTO Reviews VALUES ('pepe125', 'Uncharted', 'Mala, genérica, sin alma, predecible, no destaca enabsolutamente nada, " +
                "es como una lista de clichés uno detrás de otro de lo que una película típica de aventuras debe tener.', 1)");
        sqLiteDatabase.execSQL("INSERT INTO Reviews VALUES ('58ana_', 'Uncharted', 'A mis hijas de menos de 12 años les gustó. Mi opinión es pésima. No hay guión y se nota a los 5 minutos de empezar.', 1)");
        sqLiteDatabase.execSQL("INSERT INTO Reviews VALUES ('christian', 'Dune', 'Profunda, impactante y bien trabajada.', 5)");
        sqLiteDatabase.execSQL("INSERT INTO Reviews VALUES ('aitorjus', 'Dune', 'Literal el 98% de lo que dura la película es arena, y el % restante créditos.', 1)");
        sqLiteDatabase.execSQL("INSERT INTO Reviews VALUES ('58ana_', 'Clifford', 'Para los niños está bien. Se van a divertir un montón con el perro rojo gigante.', 3)");

        // Actaulizamos la puntuación media de las películas a las que les hemos añadido alguna película
        // The Batman
        String[] columnas = new String[]{"Puntuacion"};
        String[] param = new String[]{"The Batman"};
        Cursor cu = sqLiteDatabase.query("Reviews", columnas, "Pelicula=?", param, null, null, null);
        int totalStars = 0;
        int cont = cu.getCount();
        while (cu.moveToNext()) {
            totalStars += cu.getInt(0);
        }
        ContentValues datos = new ContentValues();
        String[] argumentos = new String[]{"The Batman"};
        datos.put("PuntuacionMedia", totalStars / cont);
        sqLiteDatabase.update("Peliculas", datos, "Titulo = ?", argumentos);

        // Uncharted
        columnas = new String[]{"Puntuacion"};
        param = new String[]{"Uncharted"};
        cu = sqLiteDatabase.query("Reviews", columnas, "Pelicula=?", param, null, null, null);
        totalStars = 0;
        cont = cu.getCount();
        while (cu.moveToNext()) {
            totalStars += cu.getInt(0);
        }
        datos = new ContentValues();
        argumentos = new String[]{"Uncharted"};
        datos.put("PuntuacionMedia", totalStars / cont);
        sqLiteDatabase.update("Peliculas", datos, "Titulo = ?", argumentos);

        // Dune
        columnas = new String[]{"Puntuacion"};
        param = new String[]{"Dune"};
        cu = sqLiteDatabase.query("Reviews", columnas, "Pelicula=?", param, null, null, null);
        totalStars = 0;
        cont = cu.getCount();
        while (cu.moveToNext()) {
            totalStars += cu.getInt(0);
        }
        datos = new ContentValues();
        argumentos = new String[]{"Dune"};
        datos.put("PuntuacionMedia", totalStars / cont);
        sqLiteDatabase.update("Peliculas", datos, "Titulo = ?", argumentos);

        // Clifford
        columnas = new String[]{"Puntuacion"};
        param = new String[]{"Clifford"};
        cu = sqLiteDatabase.query("Reviews", columnas, "Pelicula=?", param, null, null, null);
        totalStars = 0;
        cont = cu.getCount();
        while (cu.moveToNext()) {
            totalStars += cu.getInt(0);
        }
        datos = new ContentValues();
        argumentos = new String[]{"Clifford"};
        datos.put("PuntuacionMedia", totalStars / cont);
        sqLiteDatabase.update("Peliculas", datos, "Titulo = ?", argumentos);

    }

    private byte[] getByteArray(int drawable) {
        // Conseguimos el Bitmap del drawable
        Bitmap bitmapimage = BitmapFactory.decodeResource(pcontext.getResources(), drawable);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapimage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Usuarios");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Peliculas");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Reviews");
        onCreate(sqLiteDatabase);
    }

    public JSONObject getReviewsDePelicula(String titulo) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = new String[]{"Usuario", "Review", "Puntuacion"};
        String[] param = new String[]{titulo};
        Cursor cu = db.query("Reviews", columnas, "Pelicula=?", param, null, null, null);

        String[] lUsers = new String[cu.getCount()];
        String[] lReviews = new String[cu.getCount()];
        float[] lRatings = new float[cu.getCount()];
        try {
            JSONObject json = new JSONObject();
            while (cu.moveToNext()) {
                lUsers[cu.getPosition()] = cu.getString(0);
                lReviews[cu.getPosition()] = cu.getString(1);
                lRatings[cu.getPosition()] = cu.getFloat(2);
            }
            json.put("lUsers", lUsers);
            json.put("lReviews", lReviews);
            json.put("lRatings", lRatings);
            return json;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean addUsuario(String nombreUsuario, String nombre, String apellido, String contrasena) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues datos = new ContentValues();
            datos.put("NombreUsuario", nombreUsuario);
            datos.put("Nombre", nombre);
            datos.put("Apellido", apellido);
            datos.put("Contrasena", contrasena);
            db.insert("Usuarios", null, datos);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean addPelicula(String titulo, String director, int anio, byte[] poster) {
        try {

            SQLiteDatabase db = getWritableDatabase();
            ContentValues datos = new ContentValues();
            datos.put("Titulo", titulo);
            datos.put("Director", director);
            datos.put("Anio", anio);
            datos.put("Poster", poster);
            datos.put("PuntuacionMedia", 0);
            db.insert("Peliculas", null, datos);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    public boolean addReview(String nombreUsuario, String titulo, String review, float puntuacion) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues datos = new ContentValues();
            datos.put("Pelicula", titulo);
            datos.put("Usuario", nombreUsuario);
            datos.put("Review", review);
            datos.put("Puntuacion", puntuacion);
            db.insert("Reviews", null, datos);
            db.close();
            actualizarPuntuacionPelicula(titulo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void actualizarPuntuacionPelicula(String pelicula) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = new String[]{"Puntuacion"};
        String[] param = new String[]{pelicula};
        Cursor cu = db.query("Reviews", columnas, "Pelicula=?", param, null, null, null);
        int totalStars = 0;
        int cont = cu.getCount();
        while (cu.moveToNext()) {
            totalStars += cu.getInt(0);
        }
        ContentValues datos = new ContentValues();
        String[] argumentos = new String[]{pelicula};
        datos.put("PuntuacionMedia", (float) totalStars / cont);
        db.update("Peliculas", datos, "Titulo = ?", argumentos);
        db.close();
    }

    public boolean tieneEsaContrasena(String usuario, String contrasena) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = new String[]{"Contrasena"};
        String[] param = new String[]{usuario};
        Cursor cu = db.query("Usuarios", columnas, "NombreUsuario=?", param, null, null, null);
        cu.moveToNext();
        String contrasenaCorrecta = cu.getString(0);
        if (contrasenaCorrecta.equals(contrasena)) {
            return true;
        }
        return false;

    }

    public boolean existePelicula(String titulo){
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = new String[]{"Titulo"};
        String[] param = new String[]{"Titulo"};
        Cursor cu = db.query("Peliculas", columnas, "Titulo=?", param, null, null, null);
        if (!cu.moveToNext()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean existeUsuario(String nombreUsuario) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = new String[]{"NombreUsuario"};
        String[] param = new String[]{nombreUsuario};
        Cursor cu = db.query("Usuarios", columnas, "NombreUsuario=?", param, null, null, null);
        if (!cu.moveToNext()) {
            return false;
        } else {
            return true;
        }
    }


    public JSONObject getDatosUsuario(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = new String[]{"Nombre", "Apellido"};
        String[] param = new String[]{username};
        Cursor cu = db.query("Usuarios", columnas, "NombreUsuario=?", param, null, null, null);
        try {
            JSONObject json = new JSONObject();
            cu.moveToNext();
            for (int i = 0; i < 2; i++) {
                json.put(cu.getColumnName(i), cu.getString(i));
            }
            return json;
        } catch (Exception e) {
            return null;
        }
    }

    public JSONObject getDatosPelicula(String titulo) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = new String[]{"Director", "Anio", "PuntuacionMedia"};
        String[] param = new String[]{titulo};
        Cursor cu = db.query("Peliculas", columnas, "Titulo=?", param, null, null, null);
        try {
            JSONObject json = new JSONObject();
            cu.moveToNext();
            json.put(cu.getColumnName(0), cu.getString(0));
            json.put(cu.getColumnName(1), cu.getString(1));
            json.put("Poster", getPosterDePeliculas(titulo));
            json.put(cu.getColumnName(2), cu.getString(2));
            return json;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updateUsuarioUsername(String username, String usernameNuevo) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues datos = new ContentValues();
            String[] argumentos = new String[]{username};
            datos.put("NombreUsuario", usernameNuevo);
            db.update("Usuarios", datos, "NombreUsuario = ?", argumentos);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateUsuarioContrasena(String username, String contrasena) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues datos = new ContentValues();
            String[] argumentos = new String[]{username};
            datos.put("Contrasena", contrasena);
            db.update("Usuarios", datos, "NombreUsuario = ?", argumentos);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateUsuarioNombre(String username, String nombre) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues datos = new ContentValues();
            String[] argumentos = new String[]{username};
            datos.put("Nombre", nombre);
            db.update("Usuarios", datos, "NombreUsuario = ?", argumentos);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateUsuarioApellido(String username, String apellido) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues datos = new ContentValues();
            String[] argumentos = new String[]{username};
            datos.put("Apellido", apellido);
            db.update("Usuarios", datos, "NombreUsuario = ?", argumentos);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public JSONObject getInfoPeliculas() {
        JSONObject json = new JSONObject();
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = new String[]{"Titulo", "PuntuacionMedia"};
        Cursor cu = db.query("Peliculas", columnas, null, null, null, null, null);

        String[] lTitulos = new String[cu.getCount()];
        float[] lPuntuacionMedia = new float[cu.getCount()];
        byte[][] lPosters = new byte[cu.getCount()][500];

        while (cu.moveToNext()) {
            lTitulos[cu.getPosition()] = cu.getString(0);
            lPuntuacionMedia[cu.getPosition()] = cu.getFloat(1);
            lPosters[cu.getPosition()] = getPosterDePeliculas(cu.getString(0));
        }
        try {
            json.put("lTitulos", lTitulos);
            json.put("lPosters", lPosters);
            json.put("lPuntuacionMedia", lPuntuacionMedia);
            return json;
        } catch (Exception e) {
            return null;
        }
    }

    private byte[] getPosterDePeliculas(String titulo) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = new String[]{"Poster"};
        String[] param = new String[]{titulo};
        Cursor cu = db.query("Peliculas", columnas, "Titulo=?", param, null, null, null);
        if (cu instanceof SQLiteCursor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ((SQLiteCursor) cu).setWindow(new CursorWindow(null, 1024 * 1024 * 10));
        }
        cu.moveToNext();
        return cu.getBlob(0);
    }

    public String yaHaHechoReview(String usuario, String pelicula) {
        // Si el usuario no ha hceho una review a esa película se devolverá null, en caso contrario se devolverá el texto de la review

        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = new String[]{"Review"};
        String[] param = new String[]{usuario, pelicula};
        Cursor cu = db.query("Reviews", columnas, "Usuario=? AND Pelicula=?", param, null, null, null);
        if (!cu.moveToNext()) {
            return null;
        } else {
            return cu.getString(0);
        }
    }

    public boolean actualizarReview(String usuario, String pelicula, String review, float puntuacion) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues datos = new ContentValues();
            String[] argumentos = new String[]{usuario, pelicula};
            datos.put("Review", review);
            datos.put("Puntuacion", puntuacion);
            db.update("Reviews", datos, "Usuario=? AND Pelicula=?", argumentos);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
