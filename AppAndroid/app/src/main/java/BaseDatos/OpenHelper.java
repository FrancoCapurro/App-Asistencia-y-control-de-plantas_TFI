package BaseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Plantas.db";
    private static final int DB_VERSION = 1;

    public OpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
        db.enableWriteAheadLogging();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tabla Actualizaciones
        db.execSQL(
                "CREATE TABLE Actualizaciones (" +
                        "Id_Actualizacion INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Version TEXT NOT NULL," +
                        "Fecha TEXT DEFAULT CURRENT_TIMESTAMP" +
                        ");"
        );

        // Tabla Datos_Plantas
        db.execSQL(
                "CREATE TABLE Datos_Plantas (" +
                        "Id_dato INTEGER PRIMARY KEY," +
                        "Nombre_Comun TEXT NOT NULL," +
                        "Nombre_Cientifico TEXT NOT NULL," +
                        "Temperatura_Min INTEGER NOT NULL," +
                        "Temperatura_Max INTEGER NOT NULL," +
                        "Humedad INTEGER NOT NULL," +
                        "Cantidad_Agua INTEGER NOT NULL," +
                        "Frecuencia_Riego INTEGER NOT NULL," +
                        "Tipo_Riego TEXT NOT NULL CHECK(Tipo_Riego IN ('Raiz','Pulverizar hojas'))," +
                        "Estado INTEGER NOT NULL DEFAULT 1 CHECK(Estado IN (0,1))," +
                        "Imagen TEXT" +
                        ");"
        );

        // Tabla Plantas
        db.execSQL(
                "CREATE TABLE Plantas (" +
                        "Id_Planta INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Nombre_Propio TEXT," +
                        "Id_dato INTEGER NOT NULL," +
                        "Estado INTEGER NOT NULL DEFAULT 1 CHECK(Estado IN (0,1))," +
                        "Ubicacion INTEGER NOT NULL DEFAULT 1 CHECK(Ubicacion IN (0,1))," +
                        "Fecha TEXT DEFAULT (date('now'))," +
                        "Observaciones TEXT DEFAULT NULL," +
                        "FOREIGN KEY(Id_dato) REFERENCES Datos_Plantas(Id_dato)" +
                        ");"
        );

        // Tabla Riegos
        db.execSQL(
                "CREATE TABLE Riegos (" +
                        "Id_Actualizacion INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Id_Planta INTEGER NOT NULL," +
                        "Fecha TEXT DEFAULT CURRENT_TIMESTAMP," +
                        "FOREIGN KEY(Id_Planta) REFERENCES Plantas(Id_Planta)" +
                        ");"
        );

        // INDICES
        db.execSQL("CREATE INDEX idx_nombre_comun ON Datos_Plantas(Nombre_Comun);");
        db.execSQL("CREATE INDEX idx_nombre_cientifico ON Datos_Plantas(Nombre_Cientifico);");
        db.execSQL("CREATE INDEX idx_estado_datos ON Datos_Plantas(Estado);");
        db.execSQL("CREATE INDEX idx_id_dato ON Plantas(Id_dato);");
        db.execSQL("CREATE INDEX idx_estado_plantas ON Plantas(Estado);");
        db.execSQL("CREATE INDEX idx_id_planta_riegos ON Riegos(Id_Planta);");

        // VISTA
        db.execSQL(
                "CREATE VIEW Vista_Plantas_Completa AS " +
                        "SELECT " +
                        "p.Id_Planta," +
                        "p.Nombre_Propio," +
                        "p.Ubicacion," +
                        "p.Fecha," +
                        "p.Observaciones," +
                        "p.Estado AS Estado_Planta," +
                        "d.Id_dato," +
                        "d.Nombre_Comun," +
                        "d.Nombre_Cientifico," +
                        "d.Temperatura_Min," +
                        "d.Temperatura_Max," +
                        "d.Humedad," +
                        "d.Cantidad_Agua," +
                        "d.Frecuencia_Riego," +
                        "d.Tipo_Riego," +
                        "d.Imagen," +
                        "d.Estado AS Estado_Dato " +
                        "FROM Plantas p " +
                        "JOIN Datos_Plantas d ON p.Id_dato = d.Id_dato;"
        );

        // TRIGGER BORRADO LOGICO
        db.execSQL(
                "CREATE TRIGGER trg_delete_planta " +
                        "BEFORE DELETE ON Plantas " +
                        "FOR EACH ROW " +
                        "BEGIN " +
                        "UPDATE Plantas SET Estado = 0 WHERE Id_Planta = OLD.Id_Planta;" +
                        "SELECT RAISE(IGNORE);" +
                        "END;"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Solo se reinicia Actualizaciones; los datos de usuario se conservan
        db.execSQL("DROP TABLE IF EXISTS Actualizaciones;");
        onCreate(db);
    }
}