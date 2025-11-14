import { useState } from "react";

const MessageButton = ({ recipientId }) => {
  const [showForm, setShowForm] = useState(false);
  const [message, setMessage] = useState("");

  const sendMessage = async () => {
    await fetch("http://localhost:8888/messages", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ recipientId, content: message }),
    });
    setMessage("");
    setShowForm(false);
  };

  return (
    <div className="message-button">
      <button onClick={() => setShowForm(!showForm)}>💬</button>
      {showForm && (
        <div className="message-form">
          <textarea
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder="Scrivi un messaggio..."
          />
          <button onClick={sendMessage}>Invia</button>
        </div>
      )}
    </div>
  );
};

export default MessageButton;
