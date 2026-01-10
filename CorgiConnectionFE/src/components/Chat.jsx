import { useEffect, useState, useRef } from "react";
import SockJS from "sockjs-client";
import * as StompJs from "@stomp/stompjs";

const Chat = ({ recipient }) => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [connected, setConnected] = useState(false);
  const stompClientRef = useRef(null);
  const token = localStorage.getItem("token");

  useEffect(() => {
    const socket = new SockJS(import.meta.env.VITE_WS_URL);

    const stompClient = new StompJs.Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: (str) => console.log(str),
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });

    stompClient.onConnect = () => {
      console.log("🟢 Connesso a WebSocket");
      setConnected(true);

      stompClient.subscribe("/topic/public", (message) => {
        const msg = JSON.parse(message.body);
        setMessages((prev) => [...prev, msg]);
      });

      // Chat privata
      stompClient.subscribe("/user/queue/messages", (message) => {
        const msg = JSON.parse(message.body);
        setMessages((prev) => [...prev, msg]);
      });
    };

    stompClient.onStompError = (err) => {
      console.error("Errore STOMP:", err);
    };

    stompClient.activate();
    stompClientRef.current = stompClient;

    return () => {
      stompClient.deactivate();
    };
  }, [token]);

  const sendMessage = () => {
    if (input.trim() && connected) {
      const messagePayload = {
        content: input,
        type: "CHAT",
        recipient: recipient || null,
      };

      stompClientRef.current.publish({
        destination: recipient ? "/app/chat.private" : "/app/chat.send",
        body: JSON.stringify(messagePayload),
      });

      setInput("");
    }
  };

  return (
    <div style={{ padding: "20px", maxWidth: "600px", margin: "0 auto" }}>
      <div
        style={{
          border: "1px solid #ff9900",
          borderRadius: "8px",
          padding: "10px",
          height: "400px",
          overflowY: "auto",
          marginBottom: "10px",
          background: "#fff8e6",
        }}
      >
        {messages.map((msg, idx) => (
          <div key={idx} style={{ marginBottom: "8px" }}>
            <strong style={{ color: "#f5c1a3ff" }}>{msg.sender}:</strong>{" "}
            {msg.content}
          </div>
        ))}
      </div>

      <div style={{ display: "flex", gap: "10px" }}>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
          placeholder="Scrivi un messaggio..."
          style={{
            flex: 1,
            padding: "10px",
            border: "1px solid #ff9900",
            borderRadius: "5px",
            background: "#fff8e6",
          }}
        />
        <button
          onClick={sendMessage}
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
