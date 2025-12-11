package com.gtech.food_api.core.validation;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;


/**
 * Validador: Se o valor (ex: shippingFee) for ZERO, então a descrição (ex: name)
 * DEVE conter o texto obrigatório (ex: "Free shipping").
 * Caso contrário, a validação falha.
 */
public class ValueZeroIncludedDescriptionValidator implements ConstraintValidator<ValueZeroIncludedDescription, Object> {

    private String valueField;
    private String descriptionField;
    private String requiredDescription;

    // inicialize recebe os valores da annotation
    @Override
    public void initialize(ValueZeroIncludedDescription constraintAnnotation) {
        this.valueField = constraintAnnotation.valueField();
        this.descriptionField = constraintAnnotation.descriptionField();
        this.requiredDescription = constraintAnnotation.requiredDescription();
    }

    @Override
    public boolean isValid(Object objectClass, ConstraintValidatorContext context) {
        boolean isValid = true;

        try {
            // BeansUtils ajuda a pegar o valor de um atributo de um objeto dinamicamente
            // ex: Restaurant -> shippingFee 
            BigDecimal value = (BigDecimal) BeanUtils.getPropertyDescriptor(objectClass.getClass(), valueField)
            .getReadMethod() // pega o get do valueField
            .invoke(objectClass);

            String description = (String) BeanUtils.getPropertyDescriptor(objectClass.getClass(), descriptionField)
            .getReadMethod()
            .invoke(objectClass);

            // REGRA: Se shippingFee = 0, então name DEVE conter "Free shipping"
            // Se o valor for zero, valida se a descrição contém o texto obrigatório
            if (value != null && BigDecimal.ZERO.compareTo(value) == 0 && description != null) {
                isValid = description.toLowerCase().contains(this.requiredDescription.toLowerCase());
            }

            return isValid;
        } catch (Exception e) {
            throw new ValidationException(e);
        }
    }
}
