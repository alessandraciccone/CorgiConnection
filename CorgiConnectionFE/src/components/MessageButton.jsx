import { useState } from "react";

const MessageButton = ({ recipientId }) => {
  const [showForm, setShowForm] = useState(false);
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const sendMessage = async () => {
    if (!message.trim()) {
      return; // Don't send empty messages
    }

    setIsLoading(true);
    setError(null); // Reset any previous errors

    try {
      const response = await fetch("http://localhost:8888/messages", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          recipientId,
          content: message,
        }),
      });

      if (!response.ok) {
        throw new Error("Failed to send message");
      }

      setMessage(""); // Clear the message input
      setShowForm(false); // Close the message form
    } catch (err) {
      setError(err.message); // Set the error state if something goes wrong
    } finally {
      setIsLoading(false); // Stop the loading state
    }
  };

  return (
    <>
      <div className="card" style={{ marginBottom: "0", paddingBottom: "0" }}>
        {" "}
        {/* Rimuovi lo spazio sotto la card */}
        {/* Botton per inviare messaggi */}
        <button
          onClick={() => setShowForm((prev) => !prev)}
          style={{
            marginTop: "0", // Rimuovi lo spazio sopra il bottone
            marginBottom: "0", // Rimuovi lo spazio sotto il bottone
            color: "#0e0d0dff",
            border: "none",
            padding: "6px 12px",
            borderRadius: "4px",
            cursor: "pointer",
            backgroundColor: "white",
            fontSize: "16px",
          }}
        >
          Invia un messaggio privato! 💬
        </button>
      </div>

      {showForm && (
        <div
          className="message-form"
          style={{ marginTop: "0", paddingTop: "0" }}
        >
          {" "}
          {/* Rimuovi lo spazio sopra il form */}
          <textarea
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder="Scrivi un messaggio 🐶"
            style={{
              width: "100%",
              padding: "10px",
              marginBottom: "0", // Rimuovi lo spazio sotto la textarea
              border: "1px solid #ddd",
              borderRadius: "4px",
              fontSize: "14px",
              minHeight: "80px",
              resize: "vertical",
            }}
          />
          {error && (
            <div style={{ color: "red", marginBottom: "10px" }}>{error} 😞</div>
          )}
          <button
            onClick={sendMessage}
            disabled={isLoading} // Disable the button when loading
            style={{
              color: "#0e0d0dff",
              border: "none",
              padding: "8px 16px",
              borderRadius: "4px",
              cursor: isLoading ? "not-allowed" : "pointer", // Change cursor when loading
              backgroundColor: "white",
              fontSize: "14px",
            }}
          >
            {isLoading ? "Invio..." : "Invia"}
          </button>
        </div>
      )}
    </>
  );
};

export default MessageButton;
