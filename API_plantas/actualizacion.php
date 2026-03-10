<?php
header("Content-Type: application/json");

require_once "conexion.php";

/* validar API KEY */
if(!isset($_GET["key"]) || $_GET["key"] !== API_KEY){
    http_response_code(401);
    echo json_encode([
        "error" => "Acceso no autorizado"
    ]);
    exit;
}

/* Llamar al procedimiento almacenado sp_actualizaciones */
$sql = "CALL sp_actualizaciones()";
$result = $conn->query($sql);

if($result && $result->num_rows > 0){
    echo json_encode($result->fetch_assoc());
}else{
    echo json_encode([
        "mensaje" => "No hay actualizaciones registradas"
    ]);
}

$conn->close();
?>