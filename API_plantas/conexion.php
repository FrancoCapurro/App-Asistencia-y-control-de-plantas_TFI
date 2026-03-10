<?php
$host = "mysql.railway.internal";
$user = "root";
$pass = "MasUXwdSWFgHXVadkPDArNFBYRtjDzLz";
$db   = "railway";
$port = 3306;

define("API_KEY", "TFI2026g17");

$conn = new mysqli($host, $user, $pass, $db, $port);
if ($conn->connect_error) {
    die("Error de conexión: " . $conn->connect_error);
}

// Forzar utf8mb4 y la misma collation que la tabla
$conn->set_charset("utf8mb4");
$conn->query("SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci");
?>