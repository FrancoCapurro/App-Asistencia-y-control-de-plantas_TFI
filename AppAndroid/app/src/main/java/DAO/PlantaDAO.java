package DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import Entidad.Planta;
import Entidad.DatosPlanta;
import Entidad.ImagenPlanta;

public class PlantaDAO {

    private SQLiteDatabase db;

    public PlantaDAO(SQLiteDatabase db) {
        this.db = db;
    }

    // Insertar planta
    public long insertar(Planta p) {
        ContentValues cv = new ContentValues();
        cv.put("Nombre_Propio", p.getNombrePropio());
        cv.put("Id_dato", p.getDatos().getIdDato());
        cv.put("Estado", p.isEstado() ? 1 : 0);
        cv.put("Ubicacion", p.isInterior() ? 1 : 0);
        cv.put("Fecha", p.getFecha());
        cv.put("Observaciones", p.getObservaciones());
        return db.insert("Plantas", null, cv);
    }

    // Listar todas las plantas activas
    public List<Planta> listarActivas() {
        List<Planta> lista = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM Vista_Plantas_Completa WHERE Estado_Planta = 1", null);

        while(c.moveToNext()) {
            Planta p = new Planta();
            p.setIdPlanta(c.getInt(c.getColumnIndexOrThrow("Id_Planta")));
            p.setNombrePropio(c.getString(c.getColumnIndexOrThrow("Nombre_Propio")));
            p.setInterior(c.getInt(c.getColumnIndexOrThrow("Ubicacion")) == 1);
            p.setEstado(c.getInt(c.getColumnIndexOrThrow("Estado_Planta")) == 1);
            p.setFecha(c.getString(c.getColumnIndexOrThrow("Fecha")));
            p.setObservaciones(c.getString(c.getColumnIndexOrThrow("Observaciones")));

            // Mapear DatosPlanta
            DatosPlanta d = new DatosPlanta();
            d.setIdDato(c.getInt(c.getColumnIndexOrThrow("Id_dato")));
            d.setNombreComun(c.getString(c.getColumnIndexOrThrow("Nombre_Comun")));
            d.setNombreCientifico(c.getString(c.getColumnIndexOrThrow("Nombre_Cientifico")));
            d.setTemperaturaMin(c.getInt(c.getColumnIndexOrThrow("Temperatura_Min")));
            d.setTemperaturaMax(c.getInt(c.getColumnIndexOrThrow("Temperatura_Max")));
            d.setHumedad(c.getInt(c.getColumnIndexOrThrow("Humedad")));
            d.setCantidadAgua(c.getInt(c.getColumnIndexOrThrow("Cantidad_Agua")));
            d.setFrecuenciaRiego(c.getInt(c.getColumnIndexOrThrow("Frecuencia_Riego")));
            d.setTipoRiego(c.getString(c.getColumnIndexOrThrow("Tipo_Riego")));
            d.setEstado(c.getInt(c.getColumnIndexOrThrow("Estado_Dato")) == 1);

            // Mapear ImagenPlanta
            ImagenPlanta img = new ImagenPlanta();
            img.setRutaLocal(c.getString(c.getColumnIndexOrThrow("Imagen")));
            img.setUrl(c.getString(c.getColumnIndexOrThrow("Imagen")));
            img.setDescargada(false);
            d.setImagen(img);

            p.setDatos(d);

            lista.add(p);
        }

        c.close();
        return lista;
    }
}