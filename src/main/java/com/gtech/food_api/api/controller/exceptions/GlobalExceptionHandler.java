package com.gtech.food_api.api.controller.exceptions;

import com.gtech.food_api.domain.service.exceptions.BusinessException;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// ResponseEntityException Handler para tratar os erros padroes do Spring
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionType type = ExceptionType.ENTITY_NOT_FOUND; // enum para identificar o tipo de erro criada, cria Uri e title
        String detail = ex.getMessage();

        ExceptionsDTO body = createBuilder(status, type, detail).build();

        // handleExceptionInternal é um metodo da classe ResponseEntityExceptionHandler que trata os erros padroes do Spring
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionType type = ExceptionType.BUSINESS_BAD_REQUEST;
        String detail = ex.getMessage();

        ExceptionsDTO body = createBuilder(status, type, detail).build();

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<?> handleEntityInUseException(EntityInUseException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionType type = ExceptionType.ENTITY_IN_USE;
        String detail = ex.getMessage();

        ExceptionsDTO body = createBuilder(status, type, detail).build();

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    // sobreescreve o metodo padrao para custumizar o body da resposta
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        // Criar um corpo padrão de erro quando o Spring não fornece nenhum body
        if (body == null) {
            body = ExceptionsDTO.builder()
                    .title(((HttpStatus) statusCode).getReasonPhrase() ) // mensagem padrao do spring error
                    .status(statusCode.value())
                    .build();
        } else if (body instanceof String) { // Converter mensagens de erro em texto puro (String) para o DTO padrão da API
            body = ExceptionsDTO.builder()
                    .title((String) body)
                    .status(statusCode.value())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    // classe auxiliar para criar o body da resposta
    public ExceptionsDTO.ExceptionsDTOBuilder createBuilder(HttpStatus status, ExceptionType type, String detail) {
        return ExceptionsDTO.builder()
                .type(type.getUri())
                .title(type.getTitle())
                .status(status.value())
                .detail(detail);
    }
}
