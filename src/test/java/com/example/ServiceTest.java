package com.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTest {


    @Mock
    private Socket aSocket;
    @InjectMocks
    private Service service;
    @BeforeAll
    public void setUp() throws Exception {
        H2Setup h2Setup = new H2Setup();
        h2Setup.initialise();
    }
    @Test
    public void testAttendRequest(){
        Boolean aBoolean = service.attendRequest();
        assertEquals(aBoolean, Boolean.TRUE);

    }
}
