package com.example.trabajoindividual_1;

import android.os.StrictMode;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;

public class BdRemota {
    private String direccion;
    private HttpURLConnection urlConnection;
    int responseCode;

    public BdRemota(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    public boolean addUsuario(String username, String nombre, String apellido, String contrasena) {
        /*
        Pre: El username, el nombre del usuario, su apellido y la contraseña
        GET: Se ha creado el usuario correctamente
        */


        direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/isanmiguel008/WEB/?accion=addUsuario&nombreUsuario=" + username + "&nombre=" + nombre + "&apellido=" + apellido + "&contrasena=" + contrasena;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            // Configuramos la petición
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-ww-form-urlencoded");

            //Ejecutamos la llamada
            if (urlConnection.getResponseCode() == 200) { // Si ha salido bien
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                // Obtenemos el resultado
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                if (!result.contains("Ha ocurrido algún error:")) { // Si se ha actualizado correctamente
                    return true;
                }
            }
            // Si algo ha salido mal
            return false;

        } catch (Exception e) {
            return false;
        }

    }

    public boolean updateUsuarioUsername(String nombreUsuario, String nombreUsuarioNuevo) {
        /*
        Pre: El username de un usuario actual y el nuevo username
        GET: Se ha cambiado el username correctamente
        */

        direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/isanmiguel008/WEB/?accion=updateUsuarioUsername&nombreUsuario=" + nombreUsuario + "&nombreUsuarioNuevo=" + nombreUsuarioNuevo;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            // Configuramos la petición
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-ww-form-urlencoded");

            //Ejecutamos la llamada
            if (urlConnection.getResponseCode() == 200) { // Si ha salido bien
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                // Obtenemos el resultado
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                if (!result.contains("Ha ocurrido algún error:")) { // Si se ha actualizado correctamente
                    return true;
                }
            }
            // Si algo ha salido mal
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateUsuarioContrasena(String nombreUsuario, String contrasena) {
        /*
        Pre: El username y la nueva contraseña
        GET: Se ha cambiado la contraseña correctamente
        */

        direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/isanmiguel008/WEB/?accion=updateUsuarioContrasena&nombreUsuario=" + nombreUsuario + "&updateUsuarioContrasena=" + contrasena;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            // Configuramos la petición
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-ww-form-urlencoded");

            //Ejecutamos la llamada
            if (urlConnection.getResponseCode() == 200) { // Si ha salido bien
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                // Obtenemos el resultado
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                if (!result.contains("Ha ocurrido algún error:")) { // Si se ha actualizado correctamente
                    return true;
                }
            }
            // Si algo ha salido mal
            return false;

        } catch (Exception e) {
            return false;
        }

    }

    public boolean updateUsuarioNombre(String nombreUsuario, String nombre) {
        /*
        Pre: El username y el nuevo nombre
        GET: Se ha cambiado el nombre correctamente
        */

        direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/isanmiguel008/WEB/?accion=updateUsuarioNombre&nombreUsuario=" + nombreUsuario + "&nombre=" + nombre;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            // Configuramos la petición
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-ww-form-urlencoded");

            //Ejecutamos la llamada
            if (urlConnection.getResponseCode() == 200) { // Si ha salido bien
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                // Obtenemos el resultado
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                if (!result.contains("Ha ocurrido algún error:")) { // Si se ha actualizado correctamente
                    return true;
                }
            }
            // Si algo ha salido mal
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateUsuarioApellido(String nombreUsuario, String apellido) {
        /*
        Pre: El username y el nuevo apellido
        GET: Se ha cambiado el apellido correctamente
        */

        direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/isanmiguel008/WEB/?accion=updateUsuarioApellido&nombreUsuario=" + nombreUsuario + "&apellido=" + apellido;

        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            // Configuramos la petición
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-ww-form-urlencoded");

            //Ejecutamos la llamada
            if (urlConnection.getResponseCode() == 200) { // Si ha salido bien
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                // Obtenemos el resultado
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                if (!result.contains("Ha ocurrido algún error:")) { // Si se ha actualizado correctamente
                    return true;
                }
            }
            // Si algo ha salido mal
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public JSONObject getDatosUsuario(String nombreUsuario) {
        /*
        Pre: El username
        GET: Un JSON con todos los datos del usuario
        */

        direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/isanmiguel008/WEB/?accion=getDatosUsuario&nombreUsuario=" + nombreUsuario;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            // Configuramos la petición
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-ww-form-urlencoded");

            //Ejecutamos la llamada
            if (urlConnection.getResponseCode() == 200) { // Si ha salido bien
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                // Obtenemos el resultado
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                if (!result.contains("Ha ocurrido algún error:")) { // Si no ha ocurrido ningún error
                    // Se convierte el resultado a JSON y se devuelve
                    return new JSONObject(result.toString());
                }
            }
            // Si algo ha salido mal
            return null;

        } catch (Exception e) {
            return null;
        }
    }

    public Boolean tieneEsaContrasena(String nombreUsuario, String contrasena) {
        /*
        Pre: El username y la contraseña introducidos
        GET: Si la contraseña de ese username coincide se devuelve true, false en caso contrario
        */

        direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/isanmiguel008/WEB/?accion=tieneEsaContrasena&nombreUsuario=" + nombreUsuario + "&contrasena=" + contrasena;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            // Configuramos la petición
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-ww-form-urlencoded");

            //Ejecutamos la llamada
            if (urlConnection.getResponseCode() == 200) { // Si ha salido bien
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                // Obtenemos el resultado
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                if (!result.contains("Ha ocurrido algún error:")) { // Si no ha ocurrido ningún error
                    // Se convierte el resultado a JSON
                    JSONObject json = new JSONObject(result.toString());
                    if ((Boolean) json.getBoolean("resultado")) { // Si coinciden las contraseñas
                        return true;
                    }
                }
            }
            // Si algo ha salido mal o las contraseñas no coinciden
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public Boolean existeUsuario(String nombreUsuario) {
        /*
        Pre: El username y la contraseña introducidos
        GET: Si la contraseña de ese username coincide se devuelve true, false en caso contrario
        */


        direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/isanmiguel008/WEB/?accion=existeUsuario&nombreUsuario=" + nombreUsuario;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            // Configuramos la petición
            urlConnection.setRequestMethod("GET");

            //Ejecutamos la llamada
            if (urlConnection.getResponseCode() == 200) { // Si ha salido bien
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                // Obtenemos el resultado
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                if (!result.contains("Ha ocurrido algún error:")) { // Si no ha ocurrido ningún error
                    // Se convierte el resultado a JSON
                    JSONObject json = new JSONObject(result.toString());
                    if ((Boolean) json.get("resultado")) { // Si existe el usuario
                        return true;
                    }
                }
            }
            // Si algo ha salido mal o el usuario no existe
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
