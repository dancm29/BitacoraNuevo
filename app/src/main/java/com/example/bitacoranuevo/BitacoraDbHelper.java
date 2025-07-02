package com.example.bitacoranuevo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BitacoraDbHelper extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "bitacora.db";
    private static final int DATABASE_VERSION = 2;

    // Instancia Singleton
    private static BitacoraDbHelper instance;

    // Método estático para obtener la instancia
    public static synchronized BitacoraDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new BitacoraDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Constructor privado para impedir múltiples instancias
    BitacoraDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Nombre de la tabla y columnas
    public static final String TABLE_NAME = "arboles";
    public static final String COL_ID = "id";
    public static final String COL_NUMERO = "numero";
    public static final String COL_CIENTIFICO = "cientifico";
    public static final String COL_COMUN = "comun";
    public static final String COL_COORDENADAS = "coordenadas";
    public static final String COL_DIAMETRO = "diametro";
    public static final String COL_ALTURA = "altura";
    public static final String COL_HOJAS = "hojas";
    public static final String COL_ESTADO_HOJAS = "estadoHojas";
    public static final String COL_FLORES = "flores";
    public static final String COL_FRUTOS = "frutos";
    public static final String COL_MADUREZ = "madurez";
    public static final String COL_INTERACCION = "interaccion";
    public static final String COL_ORGANISMO = "organismo";
    public static final String COL_OBSERVACIONES = "observaciones";
    public static final String COL_FECHA_FENOLOGIA = "fechaFenologia";
    public static final String COL_IMAGEN_URI = "imagenUri";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NUMERO + " TEXT, " +
                COL_CIENTIFICO + " TEXT, " +
                COL_COMUN + " TEXT, " +
                COL_COORDENADAS + " TEXT, " +
                COL_DIAMETRO + " TEXT, " +
                COL_ALTURA + " TEXT, " +
                COL_HOJAS + " TEXT, " +
                COL_ESTADO_HOJAS + " TEXT, " +
                COL_FLORES + " TEXT, " +
                COL_FRUTOS + " TEXT, " +
                COL_MADUREZ + " TEXT, " +
                COL_INTERACCION + " TEXT, " +
                COL_ORGANISMO + " TEXT, " +
                COL_OBSERVACIONES + " TEXT, " +
                COL_FECHA_FENOLOGIA + " TEXT, " +
                COL_IMAGEN_URI + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
