package com.gtech.food_api.api.controller.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.gtech.food_api.domain.service.exceptions.BusinessException;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// ResponseEntityException Handler para tratar os erros padroes do Spring
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String ERROR_MESSAGE = "Occured an unexpected internal error, try again, if the problem persists, contact the support.";

    // classe para tratar erro geral do sistema
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionType type = ExceptionType.SYSTEM_ERROR;
        String detail = ERROR_MESSAGE;

        // Importante colocar o printStackTrace (pelo menos por enquanto, que não estamos
        // fazendo logging) para mostrar a stacktrace no console
        // Se não fizer isso, você não vai ver a stacktrace de exceptions que seriam importantes
        // para você durante, especialmente na fase de desenvolvimento
        ex.printStackTrace();

        ExceptionsDTO body = createBuilder(status, type, detail).build();
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionType type = ExceptionType.RESOURCE_NOT_FOUND; // enum para identificar o tipo de erro criada, cria Uri e title
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

        ExceptionsDTO body = createBuilder(status, type, detail)
        .userMessage(detail)
        .build();

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    // classe para sobreescrever o padrão, caso seja null ou string devolver padrão da Spring
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        // Criar um corpo padrão de erro quando o Spring não fornece nenhum body
        if (body == null) {
            body = ExceptionsDTO.builder()
                    .timestamp(LocalDateTime.now())
                    .title(((HttpStatus) statusCode).getReasonPhrase() ) // mensagem padrao do spring error
                    .status(statusCode.value())
                    .userMessage(ERROR_MESSAGE)
                    .build();
        } else if (body instanceof String) { // Converter mensagens de erro em texto puro (String) para o DTO padrão da API
            body = ExceptionsDTO.builder()
                    .timestamp(LocalDateTime.now())
                    .title((String) body)
                    .status(statusCode.value())
                    .userMessage(ERROR_MESSAGE)
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ExceptionType type = ExceptionType.INVALID_DATA;
        String detail = "One or more fields are invalid. Fill in the correct data and try again.";

        // bindingResult armazena os erros de validacao
        BindingResult bindingResult = ex.getBindingResult();

        List<ExceptionsDTO.Field> fields = bindingResult
        .getFieldErrors() // lista de erros de validacao
        .stream() // stream para percorrer a lista de erros
        .map(fieldError -> ExceptionsDTO.Field.builder() // map para converter o erro de validacao em um DTO
            .name(fieldError.getField()) // nome do campo que deu erro
            .userMessage(fieldError.getDefaultMessage()) // detalhe do erro para o usuario final
        .build()) // converter o erro de validacao em um DTO
        .collect(Collectors.toList()); // converter a lista de erros de validacao em uma lista de DTOs

        ExceptionsDTO body = createBuilder((HttpStatus) status, type, detail)
        .userMessage(detail)
        .fields(fields)
        .build();

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    // sobreescreve erro de body nao legivel (json invalido)
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // pega a causa raiz da exceção
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        // verifica se o erro é de formato invalido
        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
        // verifica se o erro é de propriedade nao encontrada
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
        }

        ExceptionType type = ExceptionType.MESSAGE_NOT_READABLE;
        String detail = "The request body is not valid. Fix the sintax and try again.";

        ExceptionsDTO body = createBuilder((HttpStatus) status, type, detail).build();

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    // classe para tratar erro de preenchimento de valores invalidos do body
    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        // ex: "restaurant.kitchen.id"
        String path = joinPath(ex.getPath());

        ExceptionType type = ExceptionType.MESSAGE_NOT_READABLE;
        String detail = String.format("Property field '%s' has an invalid value '%s'. Expected type: %s, fix and try again.", 
        path, // field name
        ex.getValue(), // value invalid
        ex.getTargetType().getSimpleName()); // expected type

        ExceptionsDTO body = createBuilder((HttpStatus) status, type, detail)
        .userMessage(ERROR_MESSAGE)
        .build();

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    // classe para tratar erro de binding de propriedade no JSON body, ex: "restaurant" = { "carro" : "123" }
    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String path = joinPath(ex.getPath());

        ExceptionType type = ExceptionType.MESSAGE_NOT_READABLE;

        String detail = String.format("Property field '%s' does not exist. Fix or remove the property and try again.", path);
        ExceptionsDTO body = createBuilder((HttpStatus) status, type, detail)
        .userMessage(ERROR_MESSAGE)
        .build();

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    // classe mãe para tratar erro de tipo de argumento path variable nao compativel
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    // classe para tratar erro de tipo de argumento nao compativel
    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String path = ex.getName(); // nome do parametro que causou o erro, ex: "id"
        ExceptionType type = ExceptionType.INVALID_PATH_VARIABLE;
        String detail = String.format("The url path '%s' has an invalid value '%s'. Fix and insert a valid value with type %s.", 
        path, 
        ex.getValue(), 
        ex.getRequiredType().getSimpleName());

        ExceptionsDTO body = createBuilder((HttpStatus) status, type, detail).build();

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    // classe para tratar erro de endpoint nao encontrado, ex: "http://localhost:8080/carro121
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        ExceptionType type = ExceptionType.RESOURCE_NOT_FOUND;
        String detail = String.format("The resource '%s' that was tried to be accessed does not exist.", ex.getRequestURL());
        ExceptionsDTO body = createBuilder((HttpStatus) status, type, detail)
        .userMessage(detail)
        .build();

        return handleExceptionInternal(ex, body, headers, status, request);
    }
    
    // classe auxiliar para criar o body da resposta error
    public ExceptionsDTO.ExceptionsDTOBuilder createBuilder(HttpStatus status, ExceptionType type, String detail) {
        return ExceptionsDTO.builder()
                .type(type.getUri()) // uri do tipo de erro, ex: "https://foodapi.com.br/entity-not-found"
                .title(type.getTitle()) // titulo do tipo de erro, ex: "Entity not found"
                .status(status.value()) // status do erro, ex: 404
                .detail(detail) // detalhe do erro, ex: "Entity with id 1 not found"
                .timestamp(LocalDateTime.now()); // timestamp do erro, ex: "2025-01-01T00:00:00Z"
    }

    // classe auxiliar para concatenar o path da exceção
    // concatena o path da exceção, ex: "restaurant.id" para "restaurant.id.name"
    private String joinPath(List<Reference> path) {
        return path.stream()
        .map(Reference::getFieldName)
        .collect(Collectors.joining("."));
    }

}
