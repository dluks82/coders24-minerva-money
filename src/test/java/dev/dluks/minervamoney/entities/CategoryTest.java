package dev.dluks.minervamoney.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void shouldCreateBaseCategoryWithNameAndDescription() {
        Category category = new Category("Food", "Food expenses");

        assertEquals("Food", category.getName());
        assertEquals("Food expenses", category.getDescription());
        assertTrue(category.isBaseCategory());

    }

}