package com.example.dedadoeda;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"frontend-to-backend", "backend-to-frontend"})
public class DedadoedaApplicationTests {
    @Test
    void contextLoads() {
    }
}