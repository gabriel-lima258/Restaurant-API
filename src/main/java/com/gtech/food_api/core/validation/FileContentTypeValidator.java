package com.gtech.food_api.core.validation;

import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {
    
    // realiza a conversão do tamanho passado na annotation para DataSize automaticamente
    private String[] allowedContentTypes;

    @Override
    public void initialize(FileContentType constraintAnnotation) {
        this.allowedContentTypes = constraintAnnotation.allowed();
    }
    
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        // verifica se o conteudo do arquivo é valido comparando com o array de content types permitidos
        return value == null || Arrays.asList(this.allowedContentTypes).contains(value.getContentType());
    }
}
