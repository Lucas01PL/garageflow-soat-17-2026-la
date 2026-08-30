package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

public class InvalidDocumentException extends BusinessException {

    public InvalidDocumentException(String document) {
        super(String.format("'%s' is not a valid CPF or CNPJ.", document));
    }

}
