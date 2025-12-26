package com.gtech.food_api.api.V1.controller.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.gtech.food_api.core.validation.ValidationException;
import com.gtech.food_api.domain.service.exceptions.BusinessException;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.ResourceNotFoundException;

import java.time.OffsetDateTime;
import java.util.List;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Classe centralizadora de tratamento de exceções da aplicação.
 * 
 * @ControllerAdvice: Faz com que esta classe intercepte exceções lançadas em qualquer Controller.
 * Estende ResponseEntityExceptionHandler para sobrescrever handlers padrão do Spring.
 * 
 * Como funciona:
 * - Quando uma exceção é lançada em qualquer Controller, o Spring busca um método @ExceptionHandler
 *   correspondente nesta classe
 * - Se encontrar, executa o método e retorna uma resposta HTTP padronizada
 * - Se não encontrar, usa os handlers padrão do Spring ou o catch-all (Exception.class)
 */
@ControllerAdvice
public class GlobalExceptionHandlerV1 extends ResponseEntityExceptionHandler {

    // ==========================================
    // CONSTANTES E DEPENDÊNCIAS
    // ==========================================

    /** Mensagem genérica de erro interno do sistema */
    public static final String ERROR_MESSAGE = "Ocorreu um erro interno inesperado. Tente novamente. Se o problema persistir, entre em contato com o suporte.";

    /** MessageSource para buscar mensagens internacionalizadas do arquivo messages.properties */
    @Autowired
    private MessageSource messageSource;

    // ==========================================
    // HANDLER GERAL (CATCH-ALL)
    // ==========================================

    /**
     * Handler genérico que captura TODAS as exceções não tratadas especificamente.
     * 
     * Este é o último recurso quando nenhum outro handler específico consegue tratar a exceção.
     * 
     * Quando é usado:
     * - Exceções inesperadas do sistema
     * - Erros não mapeados anteriormente
     * - Falhas internas da aplicação
     * 
     * @param ex Exceção genérica capturada
     * @param request Requisição HTTP que causou o erro
     * @return ResponseEntity com status 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionType type = ExceptionType.SYSTEM_ERROR;
        String detail = ERROR_MESSAGE;

        // Importante: printStackTrace ajuda no debug durante desenvolvimento
        // Em produção, substituir por logging adequado (ex: SLF4J, Logback)
        ex.printStackTrace();

        ExceptionsDTO body = createBuilder(status, type, detail).build();
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }
    

    // ==========================================
    // HANDLERS DE EXCEÇÕES DE DOMÍNIO
    // ==========================================

    /**
     * Trata exceções quando um recurso não é encontrado no banco de dados.
     * 
     * Exemplo de uso:
     * - Tentar buscar um restaurante com ID inexistente
     * - Tentar atualizar/deletar uma entidade que não existe
     * 
     * @param ex Exceção ResourceNotFoundException lançada pelo service
     * @param request Requisição HTTP
     * @return ResponseEntity com status 404 (Not Found)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionType type = ExceptionType.RESOURCE_NOT_FOUND;
        String detail = ex.getMessage();

        ExceptionsDTO body = createBuilder(status, type, detail).build();
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    /**
     * Trata exceções de regras de negócio violadas.
     * 
     * Exemplo de uso:
     * - Tentar criar um restaurante com cozinha inexistente
     * - Violar alguma regra específica do domínio
     * 
     * @param ex Exceção BusinessException lançada pelo service
     * @param request Requisição HTTP
     * @return ResponseEntity com status 400 (Bad Request)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionType type = ExceptionType.BUSINESS_BAD_REQUEST;
        String detail = ex.getMessage();

        ExceptionsDTO body = createBuilder(status, type, detail).build();
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    /**
     * Trata exceções quando uma entidade está em uso e não pode ser removida.
     * 
     * Exemplo de uso:
     * - Tentar deletar uma cozinha que possui restaurantes associados
     * - Tentar remover um recurso que tem dependências
     * 
     * @param ex Exceção EntityInUseException lançada pelo service
     * @param request Requisição HTTP
     * @return ResponseEntity com status 409 (Conflict)
     */
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

    // ==========================================
    // HANDLERS DE VALIDAÇÃO
    // ==========================================

    /**
     * Trata exceções de validação manual (usadas em PATCH, por exemplo).
     * 
     * Quando é usado:
     * - Validação programática com SmartValidator
     * - Validação após merge de campos em atualização parcial
     * 
     * @param ex ValidationException com BindingResult contendo os erros
     * @param request Requisição HTTP
     * @return ResponseEntity com status 400 (Bad Request) e lista de campos inválidos
     */
    @ExceptionHandler({ ValidationException.class })
    public ResponseEntity<Object> handleValidacaoException(ValidationException ex, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), new HttpHeaders(), 
                HttpStatus.BAD_REQUEST, request);
    }


    /**
     * Trata erros de validação automática do Spring (@Valid, @NotNull, @Size, etc).
     * 
     * Quando é usado:
     * - Campos anotados com @Valid no método do Controller
     * - Validações Bean Validation (JSR-303) falhando
     * 
     * Como funciona:
     * 1. Spring valida automaticamente o objeto quando tem @Valid
     * 2. Se houver erros, lança MethodArgumentNotValidException
     * 3. Este método captura e formata os erros em DTO padronizado
     * 
     * @param ex Exceção com BindingResult contendo todos os erros de validação
     * @param headers Cabeçalhos HTTP
     * @param status Status HTTP (geralmente 400)
     * @param request Requisição HTTP
     * @return ResponseEntity com status 400 e lista detalhada de campos inválidos
     */
    @Override 
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }

    /**
     * Método auxiliar compartilhado para processar erros de validação.
     * 
     * O que faz:
     * 1. Extrai todos os erros do BindingResult
     * 2. Busca mensagens internacionalizadas do messages.properties
     * 3. Formata cada erro em um objeto Field do DTO
     * 4. Retorna resposta padronizada com lista de campos inválidos
     * 
     * @param ex Exceção original
     * @param bindingResult Contém todos os erros de validação encontrados
     * @param headers Cabeçalhos HTTP
     * @param status Status HTTP
     * @param request Requisição HTTP
     * @return ResponseEntity com lista de campos inválidos e suas mensagens
     */
    public ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, 
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ExceptionType type = ExceptionType.INVALID_DATA;
        String detail = "Um ou mais campos estão inválidos. Preencha os dados corretamente e tente novamente.";

        // Processa cada erro de validação encontrado
        List<ExceptionsDTO.Field> fields = bindingResult
                .getAllErrors()
                .stream()
                .map(objectField -> {
                    // Busca mensagem internacionalizada do messages.properties
                    String message = messageSource.getMessage(objectField, LocaleContextHolder.getLocale());
                    
                    // Obtém nome do campo ou do objeto
                    String name = objectField.getObjectName();
                    
                    // Se for erro de campo específico, usa o nome do campo
                    if (objectField instanceof FieldError) {
                        name = ((FieldError) objectField).getField();
                    }

                    // Cria objeto Field com nome e mensagem do erro
                    return ExceptionsDTO.Field.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        ExceptionsDTO body = createBuilder((HttpStatus) status, type, detail)
                .userMessage(detail)
                .fields(fields)
                .build();

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    // ==========================================
    // HANDLERS DE HTTP HEADERS E TIPOS DE MÍDIA
    // ==========================================

    /**
     * Trata erros quando o cliente solicita um tipo de mídia que o servidor não pode fornecer.
     * 
     * Quando é usado:
     * - Cliente envia header Accept com tipo não suportado (ex: Accept: application/xml)
     * - Servidor só retorna JSON, mas cliente solicita outro formato
     * 
     * Como funciona:
     * - Retorna resposta vazia com status 406 (Not Acceptable) e headers apropriados
     * - Permite que o Spring gerencie a negociação de conteúdo automaticamente
     * 
     * @param ex Exceção indicando que o tipo de mídia solicitado não é aceito
     * @param headers Cabeçalhos HTTP
     * @param status Status HTTP (geralmente 406)
     * @param request Requisição HTTP
     * @return ResponseEntity vazia com status e headers apropriados
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).headers(headers).build();
    }

    // ==========================================
    // HANDLERS DE DESERIALIZAÇÃO JSON
    // ==========================================

    /**
     * Trata erros quando o corpo da requisição JSON não pode ser lido/convertido.
     * 
     * Quando é usado:
     * - JSON malformado (sintaxe inválida)
     * - Tipo de dado incompatível (ex: string onde espera número)
     * - Propriedade inexistente no objeto
     * 
     * Como funciona:
     * 1. Obtém a causa raiz da exceção
     * 2. Verifica o tipo específico de erro (InvalidFormat ou PropertyBinding)
     * 3. Delega para handler específico ou retorna erro genérico
     * 
     * @param ex Exceção de mensagem não legível
     * @param headers Cabeçalhos HTTP
     * @param status Status HTTP (geralmente 400)
     * @param request Requisição HTTP
     * @return ResponseEntity com detalhes do erro de deserialização
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, 
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // Obtém a causa raiz para identificar o tipo específico de erro
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        // Erro de formato inválido (ex: "abc" onde espera número)
        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
        }
        // Erro de propriedade inexistente (ex: campo "carro" que não existe)
        else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
        }

        // Erro genérico de JSON inválido
        ExceptionType type = ExceptionType.MESSAGE_NOT_READABLE;
        String detail = "O corpo da requisição não é válido. Corrija a sintaxe e tente novamente.";

        ExceptionsDTO body = createBuilder((HttpStatus) status, type, detail).build();
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Trata erros quando um valor no JSON não pode ser convertido para o tipo esperado.
     * 
     * Exemplo:
     * - Enviar "abc" para um campo do tipo Long
     * - Enviar "true" para um campo do tipo Integer
     * 
     * @param ex Exceção com detalhes do campo e valor inválido
     * @param headers Cabeçalhos HTTP
     * @param status Status HTTP
     * @param request Requisição HTTP
     * @return ResponseEntity informando campo, valor inválido e tipo esperado
     */
    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers, 
            HttpStatusCode status, WebRequest request) {
        // Constrói caminho do campo (ex: "restaurant.kitchen.id")
        String path = joinPath(ex.getPath());

        ExceptionType type = ExceptionType.MESSAGE_NOT_READABLE;
        String detail = String.format(
                "O campo '%s' possui um valor inválido '%s'. Tipo esperado: %s. Corrija e tente novamente.", 
                path,                    // Nome do campo
                ex.getValue(),           // Valor inválido recebido
                ex.getTargetType().getSimpleName()); // Tipo esperado

        ExceptionsDTO body = createBuilder((HttpStatus) status, type, detail)
                .userMessage(ERROR_MESSAGE)
                .build();

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Trata erros quando uma propriedade no JSON não existe na classe de destino.
     * 
     * Exemplo:
     * - Enviar {"carro": "123"} para um objeto Restaurant que não tem campo "carro"
     * - Enviar propriedade com nome incorreto (typo)
     * 
     * @param ex Exceção com detalhes da propriedade inexistente
     * @param headers Cabeçalhos HTTP
     * @param status Status HTTP
     * @param request Requisição HTTP
     * @return ResponseEntity informando qual propriedade não existe
     */
    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers, 
            HttpStatusCode status, WebRequest request) {
        // Constrói caminho da propriedade inexistente
        String path = joinPath(ex.getPath());

        ExceptionType type = ExceptionType.MESSAGE_NOT_READABLE;
        String detail = String.format(
                "O campo '%s' não existe. Corrija ou remova a propriedade e tente novamente.", path);
        
        ExceptionsDTO body = createBuilder((HttpStatus) status, type, detail)
                .userMessage(ERROR_MESSAGE)
                .build();

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    // ==========================================
    // HANDLERS DE TIPO/PARÂMETRO
    // ==========================================

    /**
     * Trata erros quando o tipo de um parâmetro não corresponde ao esperado.
     * 
     * Quando é usado:
     * - Path variable com tipo incorreto (ex: /restaurants/abc onde espera Long)
     * - Query parameter com tipo incorreto
     * 
     * @param ex Exceção genérica de tipo incompatível
     * @param headers Cabeçalhos HTTP
     * @param status Status HTTP
     * @param request Requisição HTTP
     * @return ResponseEntity com erro específico ou delega para handler padrão
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        // Se for erro de argumento de método (path variable), trata especificamente
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        // Caso contrário, usa handler padrão do Spring
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    /**
     * Trata erros quando um path variable ou query parameter tem tipo incorreto.
     * 
     * Exemplo:
     * - GET /restaurants/abc (onde "abc" não pode ser convertido para Long)
     * - GET /restaurants?id=xyz (onde "xyz" não pode ser convertido para Long)
     * 
     * @param ex Exceção com detalhes do parâmetro e tipo esperado
     * @param headers Cabeçalhos HTTP
     * @param status Status HTTP
     * @param request Requisição HTTP
     * @return ResponseEntity informando parâmetro, valor inválido e tipo esperado
     */
    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, 
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String path = ex.getName(); // Nome do parâmetro (ex: "id")
        ExceptionType type = ExceptionType.INVALID_PATH_VARIABLE;
        String detail = String.format(
                "O parâmetro da URL '%s' possui um valor inválido '%s'. Corrija e insira um valor válido do tipo %s.", 
                path,                           // Nome do parâmetro
                ex.getValue(),                  // Valor inválido recebido
                ex.getRequiredType().getSimpleName()); // Tipo esperado

        ExceptionsDTO body = createBuilder((HttpStatus) status, type, detail).build();
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    // ==========================================
    // HANDLER DE ENDPOINT NÃO ENCONTRADO
    // ==========================================

    /**
     * Trata erros quando nenhum endpoint corresponde à URL requisitada.
     * 
     * Quando é usado:
     * - URL completamente incorreta (ex: /carro121 ao invés de /restaurants)
     * - Método HTTP incorreto para a URL (ex: GET em endpoint que só aceita POST)
     * 
     * Exemplo:
     * - GET http://localhost:8080/carro121 → nenhum handler encontrado
     * 
     * @param ex Exceção com URL que não foi encontrada
     * @param headers Cabeçalhos HTTP
     * @param status Status HTTP (404)
     * @param request Requisição HTTP
     * @return ResponseEntity informando que o recurso/endpoint não existe
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        ExceptionType type = ExceptionType.RESOURCE_NOT_FOUND;
        String detail = String.format(
                "O recurso '%s' que foi tentado acessar não existe.", ex.getRequestURL());
        
        ExceptionsDTO body = createBuilder((HttpStatus) status, type, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

    /**
     * Sobrescreve método padrão do Spring para garantir formato consistente de resposta.
     * 
     * O que faz:
     * 1. Se body for null, cria DTO padrão com mensagem genérica
     * 2. Se body for String, converte para DTO padronizado
     * 3. Caso contrário, mantém o body como está
     * 
     * Por que é importante:
     * - Garante que todas as respostas de erro seguem o mesmo formato
     * - Evita respostas vazias ou em formato inconsistente
     * 
     * @param ex Exceção original
     * @param body Corpo da resposta (pode ser null, String ou ExceptionsDTO)
     * @param headers Cabeçalhos HTTP
     * @param statusCode Status HTTP
     * @param request Requisição HTTP
     * @return ResponseEntity padronizado
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, 
            HttpStatusCode statusCode, WebRequest request) {
        // Cria corpo padrão quando Spring não fornece nenhum body
        if (body == null) {
            body = ExceptionsDTO.builder()
                    .timestamp(OffsetDateTime.now())
                    .title(((HttpStatus) statusCode).getReasonPhrase()) // Mensagem padrão do Spring
                    .status(statusCode.value())
                    .userMessage(ERROR_MESSAGE)
                    .build();
        }
        // Converte mensagens de erro em texto puro (String) para o DTO padrão da API
        else if (body instanceof String) {
            body = ExceptionsDTO.builder()
                    .timestamp(OffsetDateTime.now())
                    .title((String) body)
                    .status(statusCode.value())
                    .userMessage(ERROR_MESSAGE)
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    /**
     * Método auxiliar para criar builder do DTO de exceção de forma padronizada.
     * 
     * O que faz:
     * - Cria um builder do ExceptionsDTO com campos comuns preenchidos
     * - Retorna builder para permitir adicionar campos específicos (fields, userMessage, etc)
     * 
     * Campos preenchidos:
     * - type: URI do tipo de erro (ex: "https://foodapi.com.br/entity-not-found")
     * - title: Título do tipo de erro (ex: "Entity not found")
     * - status: Código HTTP (ex: 404)
     * - detail: Detalhe específico do erro
     * - timestamp: Data/hora do erro
     * 
     * @param status Status HTTP da resposta
     * @param type Tipo da exceção (enum com URI e título)
     * @param detail Detalhe específico do erro
     * @return Builder do ExceptionsDTO para adicionar campos adicionais
     */
    public ExceptionsDTO.ExceptionsDTOBuilder createBuilder(HttpStatus status, ExceptionType type, String detail) {
        return ExceptionsDTO.builder()
                .type(type.getUri())              // URI do tipo de erro
                .title(type.getTitle())           // Título do tipo de erro
                .status(status.value())           // Código HTTP
                .detail(detail)                   // Detalhe específico
                .timestamp(OffsetDateTime.now());  // Timestamp do erro
    }

    /**
     * Constrói caminho completo de um campo a partir da lista de referências do Jackson.
     * 
     * O que faz:
     * - Converte lista de Reference em string com pontos (ex: "restaurant.kitchen.id")
     * - Útil para mostrar ao usuário exatamente qual campo causou o erro
     * 
     * Exemplo:
     * - Input: [Reference("restaurant"), Reference("kitchen"), Reference("id")]
     * - Output: "restaurant.kitchen.id"
     * 
     * @param path Lista de referências do caminho do campo no JSON
     * @return String com caminho completo separado por pontos
     */
    private String joinPath(List<Reference> path) {
        return path.stream()
                .map(Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

}
