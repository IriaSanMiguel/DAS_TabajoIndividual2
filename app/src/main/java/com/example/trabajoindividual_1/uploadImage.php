<?php
// Obtenemos el poster
$poster = $_FILES['file'];
// Obtenemos el nombre del poster
$nombrePoster = $_FILES['file']['name'];
if (move_uploaded_file($_FILES['file']['tmp_name'], "./imagenes/$nombrePoster.jpg")){ // Si se ha subido la imagen correctamente
    header("HTTP/1.1 200 OK");
}else{ // Si ha habido algún problema
    header("HTTP/1.1 404 Not Found");
}
?>