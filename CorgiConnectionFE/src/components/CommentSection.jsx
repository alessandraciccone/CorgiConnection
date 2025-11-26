import { useState, useEffect } from "react";
import "../css/CommentSection.css";

const CommentSection = ({ postId, userId, token }) => {
  const [comments, setComments] = useState([]);
  const [text, setText] = useState("");
<<<<<<< Updated upstream
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

=======
  const [username, setUsername] = useState(""); // ⬅️ State separato per username

>>>>>>> Stashed changes
  // Carica commenti salvati
  useEffect(() => {
    const saved = localStorage.getItem(`comments-${postId}`);
    setComments(saved ? JSON.parse(saved) : []);
  }, [postId]);

<<<<<<< Updated upstream
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
=======
  // Recupera utente e username
  useEffect(() => {
    const fetchUser = async () => {
      try {
        const res = await fetch(`http://localhost:8888/users/${userId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (res.ok) {
          const data = await res.json();
          console.log("👤 USER DATA:", data);
          console.log("📝 USERNAME:", data.username);
          setUsername(data.username || "Anonimo"); // ⬅️ Salva username nello state
        }
      } catch (err) {
        console.error("Errore nel recupero utente:", err);
        setUsername("Anonimo"); // ⬅️ Fallback
      }
    };

    if (userId && token) {
      fetchUser();
    }
  }, [userId, token]);
>>>>>>> Stashed changes

  // Salva commenti
  const saveComments = (newComments) => {
    setComments(newComments);
    localStorage.setItem(`comments-${postId}`, JSON.stringify(newComments));
  };

  // Aggiungi commento
  const handleSubmit = () => {
    console.log("🔴 SUBMIT - Username:", username);
    console.log("🔴 SUBMIT - Text:", text);

    if (!text.trim()) return;

<<<<<<< Updated upstream
    const newComment = {
      id: Date.now(),
      text,
      date: new Date().toISOString(),
      author: username,
    };

    const newComments = [...comments, newComment];
=======
    const newComments = [
      ...comments,
      {
        id: Date.now(),
        text,
        date: new Date().toISOString(),
        author: username || "Anonimo", // ⬅️ Usa lo state username
      },
    ];

    console.log("✅ Nuovo commento:", newComments[newComments.length - 1]);
>>>>>>> Stashed changes
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
        style={{
          width: "100%",
          padding: "8px",
          border: "1px solid #ddd",
          borderRadius: "5px",
          fontSize: "14px",
          marginBottom: "8px",
          resize: "vertical",
        }}
      />

      <button className="small-button" onClick={handleSubmit}>
        Commenta
      </button>
    </div>
  );
};

export default CommentSection;
