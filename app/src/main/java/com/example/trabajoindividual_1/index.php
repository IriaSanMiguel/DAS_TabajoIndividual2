<?php
// En este fichero se encuentran todas las consultas, inserciones, eliminaciones o actualizaciones a la tabla Usuarios de la base de datos Xisanmiguel008_ReviewsPeliculas
// que son necesarias para que la aplicación funcione correctamente

$DB_SERVER="127.0.0.1"; #la dirección del servidor
$DB_USER="Xisanmiguel008"; #el usuario para esa base de datos
$DB_PASS="*2xIxrKDcE"; #la clave para ese usuario
$DB_DATABASE="Xisanmiguel008_ReviewsPeliculas"; #la base de datos a la que hay que conectarse

// Establecemos la conexión
$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);

// Comprobamos la conexión
if (mysqli_connect_errno($con)) { // Si ha ocurrido algún error
    echo 'Error de conexion: ' . mysqli_connect_error();
    exit();
}
else{ // Si ha salido todo bien
    $accion = $_GET["accion"];

    // Inicializamos $resultado
    $resultado = NULL;

    switch ($accion){ // Comprobamos qué acción se quiere hacer en la tabla Usuarios
        case "addUsuario": // Añadir un usuario nuevo
            // Obtenemos los parámetros necesarios para la consulta
            $nombreUsuario = $_GET["nombreUsuario"];
            $nombre = $_GET["nombre"];
            $apellido = $_GET["apellido"];
            $contrasena = $_GET["contrasena"];

            // Creamos el hash para la contraseña
            $contrasena_hash = password_hash($contrasena, PASSWORD_DEFAULT);

            // Hacemos la query
            $query = "INSERT INTO Usuarios (NombreUsuario, Nombre, Apellido, Contrasena) VALUES ('$nombreUsuario', '$nombre', '$apellido', '$contrasena_hash')";
            if ($con -> query($query) === TRUE){
                $resultado -> ok = True;
                // Devolvemos el resultadoo
                echo json_encode($resultado);
            }else{
                echo 'Ha ocurrido algún error: ' . mysqli_error($con);
            }
            break;

        case "updateUsuarioUsername": // Cambiar el username de un usuario
            // Obtenemos los parámetros necesarios para el update
            $nombreUsuario = $_GET["nombreUsuario"];
            $nombreUsuarioNuevo = $_GET["nombreUsuarioNuevo"];


            // Hacemos el update
            $res = mysqli_query($con, "UPDATE Usuarios SET NombreUsuario = '$nombreUsuarioNuevo' WHERE NombreUsuario = '$nombreUsuario'");

            if (!$res){ // Si no se ha ejecutado la consulta correctamente
                echo 'Ha ocurrido algún error: ' . mysqli_error($con);
            }else{
                $resultado -> ok = True;
                // Devolvemos el resultadoo
                echo json_encode($resultado);
            }
            break;
        case "updateUsuarioContrasena": // Cambiar la contraseña de un usuario
            // Obtenemos los parámetros necesarios para la update
            $nombreUsuario = $_GET["nombreUsuario"];
            $contrasena = $_GET["contrasena"];

            // Creamos el hash para la contraseña
            $contrasena_hash = password_hash($contrasena, PASSWORD_DEFAULT);

            // Hacemos el update
            $res = mysqli_query($con, "UPDATE Usuarios SET Contrasena = '$contrasena_hash' WHERE NombreUsuario = '$nombreUsuario'");

            if (!$res){ // Si no se ha ejecutado la consulta correctamente
                echo 'Ha ocurrido algún error: ' . mysqli_error($con);
            }else{
                $resultado -> ok = True;
                // Devolvemos el resultadoo
                echo json_encode($resultado);
            }
            break;
        case "updateUsuarioNombre": // Cambiar el nombre de un usuario
            // Obtenemos los parámetros necesarios para la update
            $nombreUsuario = $_GET["nombreUsuario"];
            $nombre = $_GET["nombre"];

            // Hacemos el update
            $res = mysqli_query($con, "UPDATE Usuarios SET Nombre = '$nombre' WHERE NombreUsuario = '$nombreUsuario'");
            if (!$res){ // Si no se ha ejecutado la consulta correctamente
                echo 'Ha ocurrido algún error: ' . mysqli_error($con);
            }else{
                $resultado -> ok = True;
                // Devolvemos el resultadoo
                echo json_encode($resultado);
            }
            break;
        case "updateUsuarioApellido": // Cambiar el apellido de un usuario
            // Obtenemos los parámetros necesarios para la update
            $nombreUsuario = $_GET["nombreUsuario"];
            $apellido = $_GET["apellido"];

            // Hacemos el update
            $res = mysqli_query($con, "UPDATE Usuarios SET Apellido = '$apellido' WHERE NombreUsuario = '$nombreUsuario'");
            if (!$res){ // Si no se ha ejecutado la consulta correctamente
                echo 'Ha ocurrido algún error: ' . mysqli_error($con);
            }else{
                $resultado -> ok = True;
                // Devolvemos el resultadoo
                echo json_encode($resultado);
            }
            break;
        case "getDatosUsuario": // Obtener los datos de un usuario
            // Obtenemos los parámetros necesarios para la update
            $nombreUsuario = $_GET["nombreUsuario"];

            // Hacemos la consulta
            $res = mysqli_query($con, "SELECT * FROM Usuarios WHERE NombreUsuario = '$nombreUsuario'");
            if (!$res){ // Si no se ha ejecutado la consulta correctamente
               echo 'Ha ocurrido algún error: ' . mysqli_error($con);
               }
            else{ // Si se ha ejecutado la consulta correctamente
                // Obtenemos el resultado
                $fila = mysqli_fetch_assoc($res);
                // Guardamos el resultado
                $resultado = array(
                    "username" => $fila["NombreUsuario"],
                    "nombre" => $fila["Nombre"],
                    "apellido" => $fila["Apellido"],
                    "contrasena" => $fila["Contrasena"]
                );
                // Devolvemos el resultadoo
                echo json_encode($resultado);
            }
            break;
        case "tieneEsaContrasena": // Comprobamos si un usuario en concreto tiene la contraseña dada
            // Obtenemos los parámetros
            $nombreUsuario = $_GET["nombreUsuario"];
            $contrasena = $_GET["contrasena"];

            // Hacemos la consulta
            $res = mysqli_query($con, "SELECT Contrasena FROM Usuarios WHERE NombreUsuario = '$nombreUsuario'");
            if (!$res){ // Si no se ha ejecutado la consulta correctamente
               echo 'Ha ocurrido algún error: ' . mysqli_error($con);
               }
            else{ // Si se ha ejecutado la consulta correctamente
                // Obtenemos el resultado
                $fila = mysqli_fetch_assoc($res);

                // Comporbamos si las contraseñas coinciden
                if (password_verify($contrasena, $fila["Contrasena"])){ // Si coinciden
                    $resultado -> resultado = True;
                    // Devolvemos el resultadoo
                    echo json_encode($resultado);
                }else{ // Si no coinciden
                    $resultado -> resultado = False;
                    // Devolvemos el resultadoo
                    echo json_encode($resultado);
                }
            }
            break;
        case "existeUsuario": // Comporbamos si el usuario dado existe o no
            // Obtenemos los parámetros
            $nombreUsuario = $_GET["nombreUsuario"];

            // Hacemos la consulta
            $res = mysqli_query($con, "SELECT * FROM Usuarios WHERE NombreUsuario = '$nombreUsuario'");
            if (!$res){ // Si no se ha ejecutado la consulta correctamente
               echo 'Ha ocurrido algún error: ' . mysqli_error($con);
            }
            else{ // Si se ha ejecutado la consulta correctamente

                // Obtenemos el número de filas
                $num_filas = mysqli_num_rows($res);

                // Miramos si la query ha devuelto algo
                if($num_filas>0){
                    $resultado -> resultado = True;
                    // Devolvemos el resultadoo
                    echo json_encode($resultado);
                }else{
                    $resultado -> resultado = False;
                    // Devolvemos el resultadoo
                    echo json_encode($resultado);
                }
            }
            break;
   }
}
?>