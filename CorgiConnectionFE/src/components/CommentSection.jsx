import { useState, useEffect } from "react";
import "../css/CommentSection.css";

const CommentSection = ({ postId }) => {
  //postId passato come prop quindi ogni post ha la sua sezione commenti
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
      const decoded = JSON.parse(atob(token.split(".")[1])); //atob decodifica base64
      return decoded.sub; //assumo che l'ID utente sia nel campo 'sub'
    } catch (error) {
      console.error("Errore decodifica token:", error);
      return null;
    }
  };

  // Carica commenti da localStorage
  useEffect(() => {
    const saved = localStorage.getItem(`comments-${postId}`);
    const parsed = saved ? JSON.parse(saved) : []; //se non ci sono commenti uso array vuoto

    const fixed = parsed.map((c) => ({
      //assicuro che ogni commento abbia i campi necessari
      ...c, //spread operator per copiare tutte le proprietà esistenti
      likes: Number.isFinite(c.likes) ? c.likes : 0, //se likes non è un numero valido lo imposto a 0
    }));

    setComments(fixed); // aggiorno lo stato dei commenti
    localStorage.setItem(`comments-${postId}`, JSON.stringify(fixed));
  }, [postId]); // useEffect per caricare i commenti al montaggio o cambio postId

  //  Recupera username dal backend usando il token
  useEffect(() => {
    //useeffect per caricare lo username al montaggio
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
          //fetch per ottenere i dati utente
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
    if (!text.trim()) return; //trim per rimuovere spazi vuoti

    const newComment = {
      id: crypto.randomUUID(), // genera un ID unico
      text,
      date: new Date().toISOString(), // data e ora corrente in formato ISO
      author: username,
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
    //id è l'id del commento da likare
    const newComments = comments.map(
      (
        c //.map per creare un nuovo array con i commenti aggiornati
      ) => (c.id === id ? { ...c, likes: (c.likes || 0) + 1 } : c) //se l'id corrisponde incremento i like altrimenti lascio invariato
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
                  onClick={() => handleLike(c.id)} //handlelike con id del commento
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
