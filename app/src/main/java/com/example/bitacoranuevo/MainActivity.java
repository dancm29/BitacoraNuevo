package com.example.bitacoranuevo;

import android.app.AlertDialog;
import android.content.Intent;
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
    Button guardarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        estadoHojas = findViewById(R.id.estadoHojas);
        madurezFruto = findViewById(R.id.madurezFruto);
        interaccion = findViewById(R.id.interaccion);
        organismo = findViewById(R.id.organismo);

        botonSeleccionarFoto.setOnClickListener(v -> mostrarOpcionesFoto());

        configurarSpinners();
        configurarListeners();

        guardarBtn.setOnClickListener(this::guardarDatos);
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
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String nombreArchivo = "IMG_" + timeStamp + "_";
            File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            return File.createTempFile(nombreArchivo, ".jpg", directorio);
        } catch (IOException e) {
            Toast.makeText(this, "Error al crear archivo de imagen", Toast.LENGTH_SHORT).show();
            return null;
        }
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
        String num = numero.getText().toString().trim();
        if (num.isEmpty()) {
            numero.setError("Campo requerido");
            numero.requestFocus();
            return;
        }

        String fileName = "bitacora_" + num + "_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date()) + ".txt";

        String contenido = "| Número | " + num + "\n" +
                "| Nombre científico | " + cientifico.getText().toString() + "\n" +
                "| Nombre común | " + comun.getText().toString() + "\n" +
                "| Coordenadas | " + coordenadas.getText().toString() + "\n" +
                "| Diámetro del fuste | " + diametro.getText().toString() + " cm\n" +
                "| Altura | " + altura.getText().toString() + " m\n" +
                "| % de hojas | " + porcentajeHojas.getText().toString() + "%\n" +
                "| Estado de hojas | " + estadoHojas.getSelectedItem().toString() + "\n" +
                "| % de flores | " + porcentajeFlores.getText().toString() + "%\n" +
                "| % de frutos | " + porcentajeFrutos.getText().toString() + "%\n" +
                "| Madurez del fruto | " + madurezFruto.getSelectedItem().toString() + "\n" +
                "| Interacción interespecífica | " + interaccion.getSelectedItem().toString() + "\n" +
                "| Organismo | " + organismo.getText().toString() + "\n" +
                "| Observaciones | " + observaciones.getText().toString() + "\n";

        try {
            File file = new File(getExternalFilesDir(null), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(contenido.getBytes());
            fos.close();

            if (imagenUriSeleccionada != null) {
                File imgFile = new File(getExternalFilesDir(null), "foto_" + num + ".jpg");
                InputStream is = getContentResolver().openInputStream(imagenUriSeleccionada);
                FileOutputStream os = new FileOutputStream(imgFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) os.write(buffer, 0, length);
                is.close();
                os.close();
            }

            Snackbar.make(view, "📄 ¡Registro exitoso!", Snackbar.LENGTH_LONG).show();

            new AlertDialog.Builder(this)
                    .setTitle("¿Deseas registrar otro árbol?")
                    .setMessage("Los datos fueron guardados en: \n" + file.getAbsolutePath())
                    .setPositiveButton("Sí", (dialog, which) -> resetFormulario())
                    .setNegativeButton("No", (dialog, which) -> finish())
                    .show();

        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void resetFormulario() {
        EditText[] campos = {numero, cientifico, comun, coordenadas, diametro, altura, porcentajeHojas, porcentajeFlores, porcentajeFrutos, observaciones, organismo};
        for (EditText campo : campos) campo.setText("");
        estadoHojas.setVisibility(View.GONE);
        madurezFruto.setVisibility(View.GONE);
        organismo.setVisibility(View.GONE);
        imageViewPreview.setImageDrawable(null);
    }
}
