package com.gtech.food_api.api.controller;

import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.service.KitchenService;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.KitchenNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class KitchenControllerIT {

    @Autowired
    private KitchenService kitchenService;

    @Test
    public void shouldSaveKitchenWhenDataIsValid() {
        // given
        Kitchen newKitchen = new Kitchen();
        newKitchen.setName("Brasileira");
        // when
        newKitchen = kitchenService.save(newKitchen);
        // then
        assertThat(newKitchen).isNotNull();
        assertThat(newKitchen.getId()).isNotNull();
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenNameIsNull() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            kitchenService.save(new Kitchen());
        });
    }

    @Test
    public void shouldThrowEntityInUseExceptionWhenDeleteInUse() {
        Assertions.assertThrows(EntityInUseException.class, () -> {
            kitchenService.delete(1L);
        });
    }

    @Test
    public void shouldThrowKitchenNotFoundExceptionWhenDeleteNonExistent() {
        Assertions.assertThrows(KitchenNotFoundException.class, () -> {
            kitchenService.delete(100L);
        });
    }
}
