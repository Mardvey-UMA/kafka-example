//package com.example.dedadoeda.controller;
//
//import com.example.dedadoeda.service.KafkaProducerService;
//import org.springframework.web.bind.annotation.RestController;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("/api")
//public class DataController {
//
//    @Autowired
//    private KafkaProducerService kafkaProducerService;
//
//    @PostMapping("/send")
//    public ResponseEntity<String> sendMessage(@RequestBody String message) {
//        // Отправляем сообщение в Kafka
//        kafkaProducerService.sendMessage("frontend-to-backend", message);
//        return ResponseEntity.ok("Сообщение отправлено в Kafka");
//    }
//
//    // Если получение сообщений больше не требуется в контроллере, можно удалить этот метод
//    // или изменить его для другой логики
//}
// DataController.java

package com.example.dedadoeda.controller;

import com.example.dedadoeda.service.KafkaProducerService;
import com.example.dedadoeda.service.ProcessedMessageService;
import com.example.dedadoeda.model.ProcessedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ProcessedMessageService processedMessageService;

    private final Sinks.Many<ProcessedMessage> sink;

    public DataController() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        kafkaProducerService.sendMessage("frontend-to-backend", message);
        return ResponseEntity.ok("Сообщение отправлено в Kafka");
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProcessedMessage> streamProcessedMessages() {
        return processedMessageService.getProcessedMessages().doOnNext(sink::tryEmitNext);
    }
}
