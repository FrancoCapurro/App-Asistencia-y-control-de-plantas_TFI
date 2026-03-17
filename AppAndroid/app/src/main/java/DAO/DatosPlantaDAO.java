package DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Entidad.DatosPlanta;
import Entidad.ImagenPlanta;

public class DatosPlantaDAO {

    private SQLiteDatabase db;

    public DatosPlantaDAO(SQLiteDatabase db) {
        this.db = db;
    }

    // Insertar un registro de DatosPlanta
    public long insertar(DatosPlanta d) {
        ContentValues cv = new ContentValues();
        cv.put("Id_dato", d.getIdDato());
        cv.put("Nombre_Comun", d.getNombreComun());
        cv.put("Nombre_Cientifico", d.getNombreCientifico());
        cv.put("Temperatura_Min", d.getTemperaturaMin());
        cv.put("Temperatura_Max", d.getTemperaturaMax());
        cv.put("Humedad", d.getHumedad());
        cv.put("Cantidad_Agua", d.getCantidadAgua());
        cv.put("Frecuencia_Riego", d.getFrecuenciaRiego());
        cv.put("Tipo_Riego", d.getTipoRiego());
        cv.put("Estado", d.isEstado() ? 1 : 0);

        // Guardar URL o ruta local de la imagen
        if (d.getImagen() != null) {
            cv.put("Imagen", d.getImagen().getRutaLocal() != null ? d.getImagen().getRutaLocal() : d.getImagen().getUrl());
        } else {
            cv.put("Imagen", "");
        }

        return db.insert("Datos_Plantas", null, cv);
    }

    // Listar todos los datos de planta
    public List<DatosPlanta> listar() {
        List<DatosPlanta> lista = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM Datos_Plantas", null);
        while (c.moveToNext()) {
            lista.add(parseDatosPlanta(c));
        }
        c.close();
        return lista;
    }

    // Buscar por ID
    public DatosPlanta buscarPorId(int idDato) {
        Cursor c = db.rawQuery("SELECT * FROM Datos_Plantas WHERE Id_dato = ?", new String[]{String.valueOf(idDato)});
        DatosPlanta d = null;
        if (c.moveToFirst()) {
            d = parseDatosPlanta(c);
        }
        c.close();
        return d;
    }

    // Método adicional para InicioActivity
    public List<DatosPlanta> obtenerTodas() {
        // Esto es igual que listar(), pero se llama así para claridad en InicioActivity
        return listar();
    }

    // Auxiliar para parsear un cursor en un objeto DatosPlanta
    private DatosPlanta parseDatosPlanta(Cursor c) {
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
        d.setEstado(c.getInt(c.getColumnIndexOrThrow("Estado")) == 1);

        ImagenPlanta img = new ImagenPlanta();
        String ruta = c.getString(c.getColumnIndexOrThrow("Imagen"));
        img.setRutaLocal(ruta);
        img.setUrl(ruta);
        img.setDescargada(false);
        d.setImagen(img);

        return d;
    }
}