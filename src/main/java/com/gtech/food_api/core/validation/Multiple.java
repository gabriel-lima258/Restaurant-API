package com.gtech.food_api.core.validation;

import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// multiple é uma annotation customizada para validar se o campo é multiplo de um valor

// onde podemos usar a annotation
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME) // rentention é o tempo de vida da annotation, RUNTIME é o tempo de vida da annotation em runtime
@Constraint(validatedBy = { MultipleValidator.class }) // espefica a classe que implementa a logica
public @interface Multiple {
    
    // mensagem padrao da annotation
    String message() default "multiple invalid";

    // grupos de validação
    Class<?>[] groups() default {};

    // payloads de validação
    Class<? extends Payload>[] payload() default {};

    int number();
}
