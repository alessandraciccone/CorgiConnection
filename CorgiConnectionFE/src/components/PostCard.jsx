import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import CommentSection from "./CommentSection";

const PostCard = ({ post, onPostUpdated, onPostDeleted }) => {
  const [editContent, setEditContent] = useState(post.content); // content  è il testo del post
  const [isEditing, setIsEditing] = useState(false); // editing è lo stato di modifica
  const [showConfirm, setShowConfirm] = useState(false); //showConfirm è lo stato di conferma eliminazione

  const [reactions, setReactions] = useState({
    "❤️": [],
    "😂": [],
    "😮": [],
    "😢": [],
  });

  const token = localStorage.getItem("token");
  const isLoggedIn = !!token; // Verifica se l'utente è loggato

  // Decodifica il token per ottenere l'ID utente

  let userId = null;
  if (token) {
    try {
      const decodedToken = JSON.parse(atob(token.split(".")[1]));
      userId =
        decodedToken?.sub ||
        decodedToken?.userId ||
        decodedToken?.id ||
        decodedToken?.user_id;
    } catch (e) {
      console.error("Errore parsing token:", e);
    }
  }

  const isPostOwner = //se l'utente loggato è il proprietario del post
    post.author?.id && userId && String(post.author.id) === String(userId); //confronto gli id come stringhe

  // Carica reactions
  useEffect(() => {
    const saved = localStorage.getItem(`post-${post.id}-reactions`);
    if (saved) setReactions(JSON.parse(saved));
  }, [post.id]); // useEffect per caricare le reactions al montaggio o cambio post.id

  const saveReactions = (newReactions) => {
    setReactions(newReactions);
    localStorage.setItem(
      `post-${post.id}-reactions`,
      JSON.stringify(newReactions)
    );
  };

  const handleReaction = (emoji) => {
    //gestisce le reazioni. rimuove la reazione se già presente, altrimenti la aggiunge
    if (!userId) return;

    const newReactions = { ...reactions };

    for (const key of Object.keys(newReactions)) {
      newReactions[key] = newReactions[key].filter((id) => id !== userId);
    } // rimuovo l'utente da tutte le reazioni

    if (!reactions[emoji].includes(userId)) {
      newReactions[emoji].push(userId);
    } // aggiungo l'utente alla reazione selezionata

    saveReactions(newReactions);
  };

  const handleUpdate = async () => {
    try {
      const baseUrl = import.meta.env.VITE_API_URL;
      const res = await fetch(`${baseUrl}/posts/${post.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ content: editContent }),
      });
      if (!res.ok) {
        console.error("Errore update:", res.status);
        return;
      }

      onPostUpdated(post.id, editContent);
      setIsEditing(false);
    } catch (err) {
      console.error("Errore update:", err);
    }
  };

  const handleDelete = async () => {
    try {
      const baseUrl = import.meta.env.VITE_API_URL;
      const res = await fetch(`${baseUrl}/posts/${post.id}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!res.ok) {
        console.error("Errore delete:", res.status);
        return;
      }

      onPostDeleted(post.id);
    } catch (err) {
      console.error("Errore delete:", err);
    }
  };

  return (
    <div
      className={`card post-card ${isEditing ? "editing" : ""}`}
      style={{
        border: "1px solid #ff7f50",
        borderRadius: "5px",
        backgroundColor: "#fff8e6",
      }}
    >
      <div className="card-body">
        {/* AUTORE */}
        <div style={{ marginBottom: "15px" }}>
          <Link to={`/profilo/${post.author?.id}`} className="username-link">
            <strong>@{post.author?.username || "Utente"}</strong>
          </Link>
          <br />
          <small style={{ color: "#666" }}>
            {post.datePost
              ? new Date(post.datePost).toLocaleDateString("it-IT")
              : ""}
          </small>
        </div>

        {/* TESTO POST */}
        {isEditing ? (
          <textarea
            value={editContent}
            onChange={(e) => setEditContent(e.target.value)}
            rows={4}
            className="form-control"
            style={{ marginBottom: "10px" }}
          />
        ) : (
          <p>{post.content}</p>
        )}

        {/* INFO CORGI */}
        {/* {post.corgi && (
          <div className="alert alert-primary mt-2">
            <strong>🐕 {post.corgi.name}</strong> — {post.corgi.age} anni
          </div>
        )} */}

        {/* REACTIONS */}
        <div
          style={{
            display: "flex",
            gap: "10px",
            marginTop: "10px",
            fontSize: "20px",
            cursor: "pointer",
          }}
        >
          {Object.keys(reactions).map(
            (
              emoji //.map serve per creare un elemento per ogni reazione
            ) => (
              <span
                key={emoji}
                onClick={() => handleReaction(emoji)}
                style={{
                  color: reactions[emoji].includes(userId) ? "red" : "#555",
                  userSelect: "none", // Impedisce la selezione del testo durante il click
                }}
                title={`${reactions[emoji].length} reazioni`}
              >
                {emoji}{" "}
                {reactions[emoji].length > 0 ? reactions[emoji].length : ""}
              </span>
            )
          )}
        </div>

        {/* BOTTONI */}
        {isLoggedIn & isPostOwner ? (
          isEditing ? (
            <>
              <button onClick={handleUpdate} className="btn btn btn-sm">
                ✔️ Salva
              </button>
              <button
                onClick={() => {
                  setIsEditing(false);
                  setEditContent(post.content);
                }}
                className="btn btn btn-sm"
              >
                Annulla
              </button>
            </>
          ) : (
            <>
              <button
                onClick={() => setIsEditing(true)}
                className="btn btn btn-sm"
              >
                ✏️ Modifica
              </button>

              <button
                onClick={() => setShowConfirm(true)}
                className="btn btn btn-sm"
                style={{ border: "none", backgroundColor: "transparent" }}
              >
                🗑️ Elimina
              </button>

              {showConfirm && (
                <div className="custom-alert">
                  <p>Vuoi davvero eliminare questo post?</p>
                  <button
                    onClick={() => {
                      handleDelete();
                      setShowConfirm(false);
                    }}
                    className="alert-btn"
                    style={{ border: "none", backgroundColor: "transparent" }}
                  >
                    Sì
                  </button>
                  <button
                    onClick={() => setShowConfirm(false)}
                    className="alert-btn"
                    style={{
                      border: "none",
                      backgroundColor: "transparent",
                    }}
                  >
                    No
                  </button>
                </div>
              )}
            </>
          )
        ) : null}

        <CommentSection postId={post.id} />
      </div>
    </div>
  );
};

export default PostCard;
