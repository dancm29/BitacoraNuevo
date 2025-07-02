package com.example.bitacoranuevo;

import android.widget.EditText;

public class ValidadorLongitudMinima extends ValidadorDecorator {
    private EditText campo;
    private int longitudMinima;
    private String mensaje;

    public ValidadorLongitudMinima(Validador validador, EditText campo, int longitudMinima, String mensaje) {
        super(validador);
        this.campo = campo;
        this.longitudMinima = longitudMinima;
        this.mensaje = mensaje;
    }

    @Override
    public boolean validar() {
        if (!super.validar()) return false;

        if (campo.getText().toString().trim().length() < longitudMinima) {
            campo.setError(mensaje);
            campo.requestFocus();
            return false;
        }
        return true;
    }
}
