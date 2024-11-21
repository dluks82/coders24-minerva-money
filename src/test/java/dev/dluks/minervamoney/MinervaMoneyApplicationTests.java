package dev.dluks.minervamoney;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class MinervaMoneyApplicationTests {

    @Test
    void shouldRemoveTestWhenDevelopingRealTests() {
        assertTrue(true, "Delete this test when implementing real unit tests");
    }
}