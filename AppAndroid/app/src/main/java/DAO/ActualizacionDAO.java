package DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ActualizacionDAO {
    private SQLiteDatabase db;

    public ActualizacionDAO(SQLiteDatabase db) { this.db = db; }

    public String obtenerUltimaFecha() {
        String fecha = null;
        Cursor cursor = db.rawQuery(
                "SELECT Fecha FROM Actualizaciones ORDER BY Id_Actualizacion DESC LIMIT 1", null
        );
        if(cursor.moveToFirst()) {
            fecha = cursor.getString(cursor.getColumnIndexOrThrow("Fecha"));
        }
        cursor.close();
        return fecha;
    }

    public long insertar(String version, String fecha) {
        ContentValues cv = new ContentValues();
        cv.put("Version", version);
        cv.put("Fecha", fecha);
        return db.insert("Actualizaciones", null, cv);
    }
}