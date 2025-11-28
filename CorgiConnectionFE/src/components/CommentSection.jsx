import { useState, useEffect } from "react";
import "../css/CommentSection.css";

const CommentSection = ({ postId }) => {
  const [comments, setComments] = useState([]);
  const [text, setText] = useState("");
  const [username, setUsername] = useState("");

  // Recupera token
  const getToken = () => localStorage.getItem("token");

  // Estrae userId dal token JWT
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

  // Carica commenti da localStorage
  useEffect(() => {
    const saved = localStorage.getItem(`comments-${postId}`);
    const parsed = saved ? JSON.parse(saved) : [];

    const fixed = parsed.map((c) => ({
      ...c,
      likes: Number.isFinite(c.likes) ? c.likes : 0,
    }));

    setComments(fixed);
    localStorage.setItem(`comments-${postId}`, JSON.stringify(fixed));
  }, [postId]);

  //  Recupera username dal backend usando il token
  useEffect(() => {
    const fetchUsername = async () => {
      const userId = getUserIdFromToken();
      if (!userId) {
        setUsername("Anonimo");
        return;
      }

      try {
        const token = getToken();
        const baseUrl = import.meta.env.VITE_API_URL;
        const res = await fetch(`${baseUrl}/users/${userId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (res.ok) {
          const data = await res.json();
          setUsername(data.username || "Anonimo");
        } else {
          setUsername("Anonimo");
        }
      } catch (err) {
        console.error("Errore fetch username:", err);
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
      id: crypto.randomUUID(),
      text,
      date: new Date().toISOString(),
      author: username, // ✔ usa lo username autenticato
      likes: 0,
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

  // Like +1
  const handleLike = (id) => {
    const newComments = comments.map((c) =>
      c.id === id ? { ...c, likes: (c.likes || 0) + 1 } : c
    );

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

              <div className="like-row" style={{ marginTop: "6px" }}>
                <span
                  className="like-button"
                  onClick={() => handleLike(c.id)}
                  style={{
                    cursor: "pointer",
                    color: "red",
                    fontSize: "18px",
                    marginRight: "5px",
                  }}
                >
                  ❤️
                </span>
                <span
                  className="like-count"
                  style={{ fontSize: "12px", color: "#444" }}
                >
                  {c.likes}
                </span>
              </div>
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
