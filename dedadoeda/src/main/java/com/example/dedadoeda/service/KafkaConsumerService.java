package com.example.dedadoeda.service;

import com.example.dedadoeda.model.ProcessedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ProcessedMessageService processedMessageService;

    private static int messageId = 0;

    @KafkaListener(topics = "frontend-to-backend", groupId = "your-group-id")
    public void consume(String message) {
        logger.info("Получено сообщение: {}", message);

        // Обработка сообщения
        String processedMessage = message.toUpperCase();
        logger.info("Обработанное сообщение: {}", processedMessage);

        // Отправка обработанного сообщения в другой топик
        kafkaTemplate.send("backend-to-frontend", processedMessage);
        logger.info("Отправлено сообщение в 'backend-to-frontend': {}", processedMessage);

        // Публикация обработанного сообщения для SSE
        ProcessedMessage msg = new ProcessedMessage(++messageId, message, processedMessage);
        processedMessageService.publish(msg);
    }
}