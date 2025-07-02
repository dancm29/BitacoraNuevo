package com.example.bitacoranuevo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 101;
    private Uri imagenUriSeleccionada = null;
    private ImageView imageViewPreview;
    private Button botonSeleccionarFoto;

    EditText numero, cientifico, comun, coordenadas, diametro, altura;
    EditText porcentajeHojas, porcentajeFlores, porcentajeFrutos;
    EditText observaciones, organismo;
    Spinner estadoHojas, madurezFruto, interaccion;
    TextView fechaFenologia;
    Button guardarBtn, verRegistrosBtn;

    BitacoraDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = BitacoraDbHelper.getInstance(this); // Usando Singleton

        numero = findViewById(R.id.numero);
        cientifico = findViewById(R.id.cientifico);
        comun = findViewById(R.id.comun);
        coordenadas = findViewById(R.id.coordenadas);
        diametro = findViewById(R.id.diametro);
        altura = findViewById(R.id.altura);
        porcentajeHojas = findViewById(R.id.porcentajeHojas);
        porcentajeFlores = findViewById(R.id.porcentajeFlores);
        porcentajeFrutos = findViewById(R.id.porcentajeFrutos);
        observaciones = findViewById(R.id.observaciones);
        botonSeleccionarFoto = findViewById(R.id.botonSeleccionarFoto);
        imageViewPreview = findViewById(R.id.imageViewPreview);
        guardarBtn = findViewById(R.id.guardarBtn);
        verRegistrosBtn = findViewById(R.id.verRegistrosBtn);
        estadoHojas = findViewById(R.id.estadoHojas);
        madurezFruto = findViewById(R.id.madurezFruto);
        interaccion = findViewById(R.id.interaccion);
        organismo = findViewById(R.id.organismo);
        fechaFenologia = findViewById(R.id.fechaFenologia);

        botonSeleccionarFoto.setOnClickListener(v -> mostrarOpcionesFoto());

        configurarSpinners();
        configurarListeners();

        guardarBtn.setOnClickListener(this::guardarDatos);

        verRegistrosBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        fechaFenologia.setOnClickListener(v -> mostrarSelectorFecha());
    }

    private void mostrarSelectorFecha() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                    fechaFenologia.setText(fechaSeleccionada);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void configurarSpinners() {
        String[] estadoHojasOptions = {"Hojas verdes", "Hojas amarillentas", "Hojas marchitas"};
        String[] madurezFrutoOptions = {"Muy inmaduro", "Ligeramente inmaduro", "Maduro"};
        String[] interaccionOptions = {"Ninguna", "Depredación", "Mutualismo", "Parasitismo", "Comensalismo"};

        estadoHojas.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, estadoHojasOptions));
        madurezFruto.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, madurezFrutoOptions));
        interaccion.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, interaccionOptions));
    }

    private void configurarListeners() {
        porcentajeHojas.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                int val = parsePorcentaje(porcentajeHojas.getText().toString());
                estadoHojas.setVisibility(val > 0 ? View.VISIBLE : View.GONE);
            }
        });

        porcentajeFrutos.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                int val = parsePorcentaje(porcentajeFrutos.getText().toString());
                madurezFruto.setVisibility(val > 0 ? View.VISIBLE : View.GONE);
            }
        });

        interaccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = interaccion.getSelectedItem().toString();
                organismo.setVisibility("Ninguna".equals(selected) ? View.GONE : View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private int parsePorcentaje(String val) {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void mostrarOpcionesFoto() {
        String[] opciones = {"Tomar foto", "Elegir de galería"};
        new AlertDialog.Builder(this)
                .setTitle("Seleccionar imagen")
                .setItems(opciones, (dialog, which) -> {
                    if (which == 0) abrirCamara();
                    else abrirGaleria();
                })
                .show();
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File fotoArchivo = crearArchivoTemporal();
            if (fotoArchivo != null) {
                imagenUriSeleccionada = FileProvider.getUriForFile(this, getPackageName() + ".provider", fotoArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUriSeleccionada);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        }
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private File crearArchivoTemporal() {
        File archivo = ImageFileFactory.createImageFile(this); // Factory Method
        if (archivo == null) {
            Toast.makeText(this, "Error al crear archivo de imagen", Toast.LENGTH_SHORT).show();
        }
        return archivo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) imageViewPreview.setImageURI(imagenUriSeleccionada);
            else if (requestCode == REQUEST_GALLERY && data != null) {
                imagenUriSeleccionada = data.getData();
                imageViewPreview.setImageURI(imagenUriSeleccionada);
            }
        }
    }

    private void guardarDatos(View view) {
        // ✅ Validación con Decorator
        Validador validador = new ValidadorVacio(numero, "Campo requerido");
        validador = new ValidadorLongitudMinima(validador, numero, 3, "Mínimo 3 caracteres");

        if (!validador.validar()) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BitacoraDbHelper.COL_NUMERO, numero.getText().toString());
        values.put(BitacoraDbHelper.COL_CIENTIFICO, cientifico.getText().toString());
        values.put(BitacoraDbHelper.COL_COMUN, comun.getText().toString());
        values.put(BitacoraDbHelper.COL_COORDENADAS, coordenadas.getText().toString());
        values.put(BitacoraDbHelper.COL_DIAMETRO, diametro.getText().toString());
        values.put(BitacoraDbHelper.COL_ALTURA, altura.getText().toString());
        values.put(BitacoraDbHelper.COL_HOJAS, porcentajeHojas.getText().toString());
        values.put(BitacoraDbHelper.COL_ESTADO_HOJAS, estadoHojas.getSelectedItem().toString());
        values.put(BitacoraDbHelper.COL_FLORES, porcentajeFlores.getText().toString());
        values.put(BitacoraDbHelper.COL_FRUTOS, porcentajeFrutos.getText().toString());
        values.put(BitacoraDbHelper.COL_MADUREZ, madurezFruto.getSelectedItem().toString());
        values.put(BitacoraDbHelper.COL_INTERACCION, interaccion.getSelectedItem().toString());
        values.put(BitacoraDbHelper.COL_ORGANISMO, organismo.getText().toString());
        values.put(BitacoraDbHelper.COL_OBSERVACIONES, observaciones.getText().toString());
        values.put(BitacoraDbHelper.COL_FECHA_FENOLOGIA, fechaFenologia.getText().toString());
        values.put(BitacoraDbHelper.COL_IMAGEN_URI, imagenUriSeleccionada != null ? imagenUriSeleccionada.toString() : null);

        long id = db.insert(BitacoraDbHelper.TABLE_NAME, null, values);

        Snackbar.make(view, "\u2705 Registro guardado correctamente (ID: " + id + ")", Snackbar.LENGTH_LONG).show();

        new AlertDialog.Builder(this)
                .setTitle("¿Deseas registrar otro árbol?")
                .setMessage("Los datos fueron guardados correctamente.")
                .setPositiveButton("Sí", (dialog, which) -> resetFormulario())
                .setNegativeButton("No", (dialog, which) -> finish())
                .show();
    }

    private void resetFormulario() {
        EditText[] campos = {numero, cientifico, comun, coordenadas, diametro, altura, porcentajeHojas, porcentajeFlores, porcentajeFrutos, observaciones, organismo};
        for (EditText campo : campos) campo.setText("");
        estadoHojas.setVisibility(View.GONE);
        madurezFruto.setVisibility(View.GONE);
        organismo.setVisibility(View.GONE);
        imageViewPreview.setImageDrawable(null);
        imagenUriSeleccionada = null;
        fechaFenologia.setText("Seleccionar fecha");
    }
}
