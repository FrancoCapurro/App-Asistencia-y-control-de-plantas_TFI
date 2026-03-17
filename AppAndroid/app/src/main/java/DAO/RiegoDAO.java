package DAO;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import BaseDatos.OpenHelper;

public class RiegoDAO {

    private OpenHelper openHelper;

    public RiegoDAO(OpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    public long registrarRiego(int idPlanta) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Id_Planta", idPlanta);
        cv.put("Fecha", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        long id = db.insert("Riegos", null, cv);
        db.close();
        return id;
    }
}