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

/* Obtener el parámetro de búsqueda */
$texto = isset($_GET["q"]) ? $_GET["q"] : "";

/* Llamar al procedimiento almacenado sp_buscar_plantas */
$stmt = $conn->prepare("CALL sp_buscar_plantas(?)");
$stmt->bind_param("s", $texto);
$stmt->execute();
$result = $stmt->get_result();

$plantas = [];
while($row = $result->fetch_assoc()){
    $plantas[] = $row;
}

if(count($plantas) > 0){
    echo json_encode($plantas);
}else{
    echo json_encode([
        "mensaje" => "No se encontraron plantas"
    ]);
}

$stmt->close();
$conn->close();
?>