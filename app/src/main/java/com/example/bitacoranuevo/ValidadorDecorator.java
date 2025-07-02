package com.example.bitacoranuevo;

public abstract class ValidadorDecorator implements Validador {
    protected Validador validador;

    public ValidadorDecorator(Validador validador) {
        this.validador = validador;
    }

    @Override
    public boolean validar() {
        return validador.validar();
    }
}
