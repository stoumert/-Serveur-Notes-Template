package fr.univ_amu.fr.formation.m1info.gla.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegerFactoryTest {

    @Test
    void next() {
        IntegerFactory factory = new IntegerFactory();
        assertEquals(1, factory.next());
        assertEquals(2, factory.next());
        assertEquals(3, factory.next());
        IntegerFactory factory2 = new IntegerFactory(-1);
        assertEquals(0, factory2.next());
    }
}