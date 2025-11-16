import { useState, useEffect } from "react";
import "../css/CommentSection.css";

const CommentSection = ({ postId }) => {
  const [comments, setComments] = useState([]);
  const [text, setText] = useState("");
  const [username, setUsername] = useState("Anonimo");

  // Recupera token dal localStorage
  const getToken = () => localStorage.getItem("token");

  // Decodifica token e ritorna sub
  const getUserIdFromToken = () => {
    const token = getToken();
    if (!token) return null;
    try {
      const decoded = JSON.parse(atob(token.split(".")[1]));
      return decoded.sub;
    } catch (error) {
      console.error("Errore decodifica token:", error);
      return null;
    }
  };

  // Carica commenti salvati
  useEffect(() => {
    const saved = localStorage.getItem(`comments-${postId}`);
    setComments(saved ? JSON.parse(saved) : []);
  }, [postId]);

  // Recupera username dal backend
  useEffect(() => {
    const fetchUsername = async () => {
      const userId = getUserIdFromToken();
      if (!userId) return;

      try {
        const token = getToken();
        const res = await fetch(`http://localhost:8888/users/${userId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (res.ok) {
          const data = await res.json();
          setUsername(data.username || "Anonimo");
        } else {
          console.warn("Utente non trovato, fallback a Anonimo");
          setUsername("Anonimo");
        }
      } catch (err) {
        console.error("Errore nel recupero utente:", err);
        setUsername("Anonimo");
      }
    };

    fetchUsername();
  }, []);

  // Salva commenti
  const saveComments = (newComments) => {
    setComments(newComments);
    localStorage.setItem(`comments-${postId}`, JSON.stringify(newComments));
  };

  // Aggiungi commento
  const handleSubmit = () => {
    if (!text.trim()) return;

    const newComment = {
      id: Date.now(),
      text,
      date: new Date().toISOString(),
      author: username,
    };

    const newComments = [...comments, newComment];
    saveComments(newComments);
    setText("");
  };

  // Cancella commento
  const handleDelete = (id) => {
    const newComments = comments.filter((c) => c.id !== id);
    saveComments(newComments);
  };

  return (
    <div className="comments">
      <hr className="comment-divider" />

      <ul className="comment-list">
        {comments.map((c) => (
          <li key={c.id} className="comment-item">
            <div style={{ flex: 1 }}>
              <strong
                style={{
                  fontSize: "12px",
                  color: "#d17b49",
                  display: "block",
                  marginBottom: "4px",
                }}
              >
                {c.author || "Anonimo"}
              </strong>
              <span className="comment-text">{c.text}</span>
            </div>
            <span className="comment-delete" onClick={() => handleDelete(c.id)}>
              ❌
            </span>
          </li>
        ))}
      </ul>

      <textarea
        className="comment-textarea paper-field"
        value={text}
        onChange={(e) => setText(e.target.value)}
        placeholder="Scrivi un commento..."
      />

      <button className="small-button" onClick={handleSubmit}>
        Commenta
      </button>
    </div>
  );
};

export default CommentSection;
