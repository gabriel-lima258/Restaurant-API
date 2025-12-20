package com.gtech.food_api.infra.service.report;

// exception para lidar com erros de relatório na camada de infra
public class ReportException extends RuntimeException {
    public ReportException(String message) {
        super(message);
    }

    public ReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
