// ProcessedMessage.java

package com.example.dedadoeda.model;

public class ProcessedMessage {
    private int id;
    private String original;
    private String processed;

    public ProcessedMessage(int id, String original, String processed) {
        this.id = id;
        this.original = original;
        this.processed = processed;
    }

    // Геттеры и сеттеры

    public int getId() {
        return id;
    }

    public String getOriginal() {
        return original;
    }

    public String getProcessed() {
        return processed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }
}
