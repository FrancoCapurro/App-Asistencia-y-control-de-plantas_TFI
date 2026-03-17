package API;


import Entidad.DatosPlanta;
import Entidad.Actualizacion;
import Entidad.ImagenPlanta;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class APIPlanta {

    private static final String KEY = "TFI2026g17";
    private static final String URL_ACTUALIZACION = "https://apiplantas-production.up.railway.app/actualizacion.php?key=" + KEY;
    private static final String URL_BUSCAR = "https://apiplantas-production.up.railway.app/buscar_planta.php?key=" + KEY + "&q=";

    // Obtener última actualización
    public static Actualizacion obtenerUltimaActualizacion() {
        try {
            String json = fetchJson(URL_ACTUALIZACION);
            JSONObject obj = new JSONObject(json);
            Actualizacion act = new Actualizacion();
            act.setVersion(obj.optString("Version", ""));
            act.setFecha(obj.optString("Fecha", ""));
            return act;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Buscar planta por nombre o ID
    public static List<DatosPlanta> buscarPlanta(String query) {
        List<DatosPlanta> lista = new ArrayList<>();
        try {
            String json = fetchJson(URL_BUSCAR + query);

            if (json.trim().startsWith("[")) {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    lista.add(parseDatosPlanta(obj));
                }
            } else if (json.trim().startsWith("{")) {
                JSONObject obj = new JSONObject(json);
                if (obj.has("mensaje")) return lista; // No hay resultados
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private static DatosPlanta parseDatosPlanta(JSONObject obj) {
        DatosPlanta d = new DatosPlanta();
        d.setIdDato(obj.optInt("Id_dato", 0));
        d.setNombreComun(obj.optString("Nombre_Comun", ""));
        d.setNombreCientifico(obj.optString("Nombre_Cientifico", ""));
        d.setTemperaturaMin(obj.optInt("Temperatura_Min", 0));
        d.setTemperaturaMax(obj.optInt("Temperatura_Max", 0));
        d.setHumedad(obj.optInt("Humedad", 0));
        d.setCantidadAgua(obj.optInt("Cantidad_Agua", 0));
        d.setFrecuenciaRiego(obj.optInt("Frecuencia_Riego", 0));
        d.setTipoRiego(obj.optString("Tipo_Riego", "Raiz"));
        d.setEstado(true);

        ImagenPlanta img = new ImagenPlanta();
        img.setUrl(obj.optString("Imagen", ""));
        img.setRutaLocal(null);
        img.setDescargada(false);
        d.setImagen(img);

        return d;
    }

    private static String fetchJson(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream in = conn.getInputStream();
        InputStreamReader reader = new InputStreamReader(in);
        StringBuilder sb = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) sb.append((char) ch);
        reader.close();
        return sb.toString();
    }
}