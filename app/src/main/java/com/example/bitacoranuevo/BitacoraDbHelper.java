package com.example.bitacoranuevo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BitacoraDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "bitacora.db";
    public static final int DATABASE_VERSION = 1;

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
    public static final String COL_IMAGEN_URI = "imagenUri";

    public BitacoraDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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
                COL_IMAGEN_URI + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
