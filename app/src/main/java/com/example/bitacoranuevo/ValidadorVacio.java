package com.example.bitacoranuevo;

import android.widget.EditText;

public class ValidadorVacio implements Validador {
    private EditText campo;
    private String mensaje;

    public ValidadorVacio(EditText campo, String mensaje) {
        this.campo = campo;
        this.mensaje = mensaje;
    }

    @Override
    public boolean validar() {
        if (campo.getText().toString().trim().isEmpty()) {
            campo.setError(mensaje);
            campo.requestFocus();
            return false;
        }
        return true;
    }
}
