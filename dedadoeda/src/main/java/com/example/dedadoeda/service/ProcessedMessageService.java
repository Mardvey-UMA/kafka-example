
package com.example.dedadoeda.service;

import com.example.dedadoeda.model.ProcessedMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class ProcessedMessageService {

    private final Sinks.Many<ProcessedMessage> sink;
    private final Flux<ProcessedMessage> flux;

    public ProcessedMessageService() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        this.flux = sink.asFlux();
    }

    public void publish(ProcessedMessage message) {
        sink.tryEmitNext(message);
    }

    public Flux<ProcessedMessage> getProcessedMessages() {
        return flux;
    }
}