import { useEffect, useState, useRef } from "react";
import SockJS from "sockjs-client";
import { over } from "@stomp/stompjs";

const Chat = () => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [connected, setConnected] = useState(false);
  const stompClientRef = useRef(null);
  const token = localStorage.getItem("token");

  useEffect(() => {
    const socket = new SockJS("http://localhost:8888/ws");
    const stompClient = over(socket);

    stompClient.connect(
      { Authorization: `Bearer ${token}` },
      () => {
        console.log("✅ Connesso a WebSocket");
        setConnected(true);

        // Chat pubblica
        stompClient.subscribe("/topic/public", (message) => {
          const msg = JSON.parse(message.body);
          setMessages((prev) => [...prev, msg]);
        });

        // Chat privata (ricezione)
        stompClient.subscribe("/user/queue/messages", (message) => {
          const msg = JSON.parse(message.body);
          setMessages((prev) => [...prev, msg]);
        });
      },
      (error) => {
        console.error("❌ Errore STOMP:", error);
      }
    );

    stompClientRef.current = stompClient;

    return () => {
      stompClient.disconnect(() => console.log("🔴 Disconnesso"));
    };
  }, [token]);

  const sendMessage = (recipient = null) => {
    if (input.trim() && connected) {
      const messagePayload = {
        content: input,
        type: "CHAT",
      };
      if (recipient) messagePayload.recipient = recipient;

      stompClientRef.current.send(
        recipient ? "/app/chat.private" : "/app/chat.send",
        {},
        JSON.stringify(messagePayload)
      );
      setInput("");
    }
  };

  return (
    <div style={{ padding: "20px", maxWidth: "600px", margin: "0 auto" }}>
      <h2>Chat in Tempo Reale 💬</h2>
      <div
        style={{
          border: "1px solid #ddd",
          borderRadius: "8px",
          padding: "10px",
          height: "400px",
          overflowY: "auto",
          marginBottom: "10px",
          background: "#f9f9f9",
        }}
      >
        {messages.map((msg, idx) => (
          <div key={idx} style={{ marginBottom: "8px" }}>
            <strong style={{ color: "#d17b49" }}>{msg.sender}:</strong>{" "}
            {msg.content}
          </div>
        ))}
      </div>

      <div style={{ display: "flex", gap: "10px" }}>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyPress={(e) => e.key === "Enter" && sendMessage()}
          placeholder="Scrivi un messaggio..."
          style={{
            flex: 1,
            padding: "10px",
            border: "1px solid #ddd",
            borderRadius: "5px",
          }}
        />
        <button
          onClick={() => sendMessage()}
          disabled={!connected}
          style={{
            padding: "10px 20px",
            background: connected ? "#d17b49" : "#ccc",
            color: "white",
            border: "none",
            borderRadius: "5px",
            cursor: connected ? "pointer" : "not-allowed",
          }}
        >
          Invia
        </button>
      </div>

      <div style={{ marginTop: "10px", fontSize: "12px", color: "#666" }}>
        Stato: {connected ? "🟢 Connesso" : "🔴 Disconnesso"}
      </div>
    </div>
  );
};

export default Chat;
