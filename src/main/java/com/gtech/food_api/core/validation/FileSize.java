package com.gtech.food_api.core.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME) // rentention é o tempo de vida da annotation, RUNTIME é o tempo de vida da annotation em runtime
@Constraint(validatedBy = { FileSizeValidator.class }) // espefica a classe que implementa a logica
public @interface FileSize {
    
    // mensagem padrao da annotation
    String message() default "file size invalid";

    // grupos de validação
    Class<?>[] groups() default {};

    // payloads de validação
    Class<? extends Payload>[] payload() default {};

    String max();
}
