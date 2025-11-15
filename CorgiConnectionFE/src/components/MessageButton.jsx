import { useState, useEffect } from "react"; // Importa anche useEffect

const MessageButton = ({ recipientId }) => {
  const [showForm, setShowForm] = useState(false);
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [messages, setMessages] = useState([]);
  const [unreadMessagesCount, setUnreadMessagesCount] = useState(0);
  const userId = localStorage.getItem("userId"); // Assicurati che l'utente sia loggato

  // Funzione per inviare un messaggio
  const sendMessage = async () => {
    if (!message.trim()) {
      return; // Non inviare messaggi vuoti
    }

    setIsLoading(true);
    setError(null);

    try {
      const response = await fetch("http://localhost:8888/messages", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify({
          contentMessage: message,
          senderId: userId,
          receiverId: recipientId,
          relatedPostId: null, // Se vuoi associare il messaggio a un post specifico, aggiungi l'id qui
        }),
      });

      if (!response.ok) {
        throw new Error("Errore nell'invio del messaggio");
      }

      setMessage(""); // Pulisci il campo messaggio
      setShowForm(false); // Chiudi il form
      fetchUnreadMessages(); // Ricarica i messaggi non letti
    } catch (err) {
      setError(err.message); // Mostra l'errore
    } finally {
      setIsLoading(false);
    }
  };

  // Funzione per ottenere i messaggi non letti
  const fetchUnreadMessages = async () => {
    try {
      const response = await fetch(
        `http://localhost:8888/messages/unread/${userId}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (response.ok) {
        const data = await response.json();
        setMessages(data);
        setUnreadMessagesCount(data.length);
      } else {
        throw new Error("Errore nel recupero dei messaggi");
      }
    } catch (err) {
      console.error("Errore nel recupero dei messaggi:", err);
    }
  };

  // Funzione per segnare il messaggio come letto
  const markAsRead = async (messageId) => {
    try {
      const response = await fetch(
        `http://localhost:8888/messages/${messageId}/read`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (response.ok) {
        setMessages(messages.filter((message) => message.id !== messageId)); // Rimuovi il messaggio dalla lista dei non letti
        setUnreadMessagesCount(unreadMessagesCount - 1);
      } else {
        throw new Error("Errore nell'aggiornamento del messaggio come letto");
      }
    } catch (err) {
      console.error("Errore nel segnare il messaggio come letto:", err);
    }
  };

  // Recupero i messaggi non letti all'inizializzazione
  useEffect(() => {
    fetchUnreadMessages();
  }, []); // Viene chiamata solo una volta, quando il componente viene montato

  return (
    <div>
      {/* Bottone per aprire il form di invio messaggio */}
      <div className="card" style={{ marginBottom: "0", paddingBottom: "0" }}>
        <button
          onClick={() => setShowForm((prev) => !prev)}
          style={{
            marginTop: "0",
            marginBottom: "0",
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

      {/* Form di invio messaggio */}
      {showForm && (
        <div
          className="message-form"
          style={{ marginTop: "0", paddingTop: "0" }}
        >
          <textarea
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder="Scrivi un messaggio 🐶"
            style={{
              width: "100%",
              padding: "10px",
              marginBottom: "0",
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
            disabled={isLoading}
            style={{
              color: "#0e0d0dff",
              border: "none",
              padding: "8px 16px",
              borderRadius: "4px",
              cursor: isLoading ? "not-allowed" : "pointer",
              backgroundColor: "white",
              fontSize: "14px",
            }}
          >
            {isLoading ? "Invio..." : "Invia"}
          </button>
        </div>
      )}

      {/* Notifica dei messaggi non letti */}
      {unreadMessagesCount > 0 && (
        <div>
          <div className="notification-badge">
            {unreadMessagesCount} nuovi messaggi
          </div>
          <ul>
            {messages.map((msg) => (
              <li key={msg.id}>
                <p>{msg.contentMessage}</p>
                <button onClick={() => markAsRead(msg.id)}>
                  Segna come letto
                </button>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default MessageButton;
