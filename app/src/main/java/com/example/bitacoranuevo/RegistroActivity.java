package com.example.bitacoranuevo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scrollView = new ScrollView(this);
        LinearLayout layoutPrincipal = new LinearLayout(this);
        layoutPrincipal.setOrientation(LinearLayout.VERTICAL);
        layoutPrincipal.setPadding(16, 16, 16, 16);

        TextView titulo = new TextView(this);
        titulo.setText("Registros guardados");
        titulo.setTextSize(20);
        titulo.setTextColor(getResources().getColor(android.R.color.white));
        layoutPrincipal.addView(titulo);

        BitacoraDbHelper dbHelper = new BitacoraDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(BitacoraDbHelper.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            CardView card = new CardView(this);
            card.setRadius(12);
            card.setCardElevation(6);
            card.setUseCompatPadding(true);
            card.setContentPadding(24, 24, 24, 24);
            LinearLayout layoutRegistro = new LinearLayout(this);
            layoutRegistro.setOrientation(LinearLayout.VERTICAL);

            StringBuilder registro = new StringBuilder();
            registro.append("Número: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_NUMERO))).append("\n");
            registro.append("Científico: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_CIENTIFICO))).append("\n");
            registro.append("Común: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_COMUN))).append("\n");
            registro.append("Coordenadas: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_COORDENADAS))).append("\n");
            registro.append("Diámetro: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_DIAMETRO))).append("\n");
            registro.append("Altura: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_ALTURA))).append("\n");
            registro.append("Hojas: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_HOJAS))).append("\n");
            registro.append("Estado hojas: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_ESTADO_HOJAS))).append("\n");
            registro.append("Flores: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_FLORES))).append("\n");
            registro.append("Frutos: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_FRUTOS))).append("\n");
            registro.append("Madurez: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_MADUREZ))).append("\n");
            registro.append("Interacción: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_INTERACCION))).append("\n");
            registro.append("Organismo: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_ORGANISMO))).append("\n");
            registro.append("Observaciones: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_OBSERVACIONES))).append("\n");
            registro.append("Fecha fenología: ").append(cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_FECHA_FENOLOGIA)));

            TextView datos = new TextView(this);
            datos.setText(registro.toString());
            datos.setTextColor(getResources().getColor(android.R.color.black));
            layoutRegistro.addView(datos);

            String uri = cursor.getString(cursor.getColumnIndexOrThrow(BitacoraDbHelper.COL_IMAGEN_URI));
            if (uri != null && !uri.isEmpty()) {
                ImageView imageView = new ImageView(this);
                imageView.setImageURI(Uri.parse(uri));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 500);
                params.topMargin = 16;
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                layoutRegistro.addView(imageView);
            }

            card.addView(layoutRegistro);
            layoutPrincipal.addView(card);

            // Separación entre tarjetas
            TextView espacio = new TextView(this);
            espacio.setHeight(30);
            layoutPrincipal.addView(espacio);
        }

        cursor.close();
        db.close();

        scrollView.addView(layoutPrincipal);
        setContentView(scrollView);
    }
}
