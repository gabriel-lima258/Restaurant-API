package com.gtech.food_api.core.validation;

import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;



/**
 * Validação customizada: quando o valor (valueField) for zero,
 * a descrição (descriptionField) DEVE conter o texto obrigatório (requiredDescription).
 * 
 * Exemplo: Se shippingFee = 0, então name deve conter "Free shipping"
 */
@Target({ ElementType.TYPE }) // permite aplicar somente em classes, interface, enum, etc
@Retention(RUNTIME)
@Constraint(validatedBy = { ValueZeroIncludedDescriptionValidator.class })
public @interface ValueZeroIncludedDescription {

    // mensagem padrao da annotation
    String message() default "This field must be zero and include free shipping description";

    // grupos de validação
    Class<?>[] groups() default {};

    // payloads de validação
    Class<? extends Payload>[] payload() default {};

    // valores da annotation
    String valueField();
    String descriptionField();
    String requiredDescription();

}
