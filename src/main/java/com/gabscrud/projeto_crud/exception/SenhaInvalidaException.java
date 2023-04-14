package com.gabscrud.projeto_crud.exception;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException() {
        super("Usuário e/ou Senha incorreto(s)");
    }
}
