package dev.dluks.minervamoney.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void shouldCreateBaseCategoryWithNameAndDescription() {
        Category category = new Category("Food", "Food expenses");

        assertThat(category.getName()).isEqualTo("Food");
        assertThat(category.getDescription()).isEqualTo("Food expenses");
        assertThat(category.isBaseCategory()).isTrue();
    }

}