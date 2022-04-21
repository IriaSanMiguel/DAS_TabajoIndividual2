<?php
$base=$_POST['imagen'];
$binary=base64_decode($base);
header('Content-Type: bitmap; charset=utf-8');
$file = fopen($_POST["nombre"], 'w+');
fwrite($file, $binary);
fclose($file);
?>