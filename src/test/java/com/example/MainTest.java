package com.example;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest{

    @Test
    void testFirstMethod(){
        Main mainInst = new Main();
        String result = mainInst.firstMethod();
        assertEquals("Checking if i can write a Junit test", result, "Incorrect");
    }
}