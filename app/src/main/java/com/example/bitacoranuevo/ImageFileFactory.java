package com.example.bitacoranuevo;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageFileFactory {

    public static File createImageFile(Context context) {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String nombreArchivo = "IMG_" + timeStamp + "_";
            File directorio = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            return File.createTempFile(nombreArchivo, ".jpg", directorio);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
