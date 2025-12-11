package com.gtech.food_api.core.validation;

import java.math.BigDecimal;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


// MultipleValidator é uma classe que valida se o campo é multiplo de um valor
// ConstraintValidator é uma interface que valida uma annotation, passando o tipo da annotation e o tipo do campo a ser validado
public class MultipleValidator implements ConstraintValidator<Multiple, Number> {

    private int numberValue;

    @Override
    public void initialize(Multiple constraintAnnotation) {
        this.numberValue = constraintAnnotation.number();
    }
    
    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (value != null) {
            // converte o valor Number para BigDecimal
            var bigDecimalValue = BigDecimal.valueOf(value.doubleValue()); // numero que sera validado
            // numero que sera validado se é multiplo do valor passado na annotation
            var multiple = BigDecimal.valueOf(numberValue);
            // verifica o resto e verifica se é zero, se for zero, é multiplo
            var remainder = bigDecimalValue.remainder(multiple);

            isValid = BigDecimal.ZERO.compareTo(remainder) == 0; // true ou false?
        }
        
        return isValid;
    }
}
