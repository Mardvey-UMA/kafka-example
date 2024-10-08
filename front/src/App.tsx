import React, { useState, useEffect } from "react";
import "./App.css";

interface ProcessedMessage {
  id: number;
  original: string;
  processed: string;
}

function App() {
  const [message, setMessage] = useState<string>("");
  const [processedMessages, setProcessedMessages] = useState<
    ProcessedMessage[]
  >([]);

  // Жёстко закодированный URL бэкенда
  const BACKEND_URL = "http://localhost:8080";

  useEffect(() => {
    const eventSource = new EventSource(`${BACKEND_URL}/api/stream`);

    eventSource.onmessage = (event) => {
      try {
        const data: ProcessedMessage = JSON.parse(event.data);
        setProcessedMessages((prev) => [...prev, data]);
      } catch (error) {
        console.error("Ошибка при разборе сообщения SSE:", error);
      }
    };

    eventSource.onerror = (err) => {
      console.error("EventSource failed:", err);
      eventSource.close();
    };

    return () => {
      eventSource.close();
    };
  }, [BACKEND_URL]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (message.trim() === "") return;

    try {
      const response = await fetch(`${BACKEND_URL}/api/send`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(message),
      });

      if (response.ok) {
        setMessage("");
        console.log("Message sent successfully");
      } else {
        console.error("Failed to send message");
      }
    } catch (error) {
      console.error("Error sending message:", error);
    }
  };

  return (
    <div className="App">
      <h1>Kafka Messaging App</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Введите сообщение"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />
        <button type="submit">Отправить</button>
      </form>

      <h2>Обработанные Сообщения:</h2>
      <ul>
        {processedMessages.map((msg) => (
          <li key={msg.id}>
            <strong>Оригинал:</strong> {msg.original} <br />
            <strong>Обработано:</strong> {msg.processed}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
