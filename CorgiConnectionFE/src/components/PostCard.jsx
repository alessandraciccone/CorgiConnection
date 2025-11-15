import React, { useState } from "react";
import MessageButton from "./MessageButton";
import CommentSection from "./CommentSection";

const PostCard = ({ post, onPostUpdated, onPostDeleted }) => {
  const [editContent, setEditContent] = useState(post.content);
  const [isEditing, setIsEditing] = useState(false);

  const token = localStorage.getItem("token");
  const isLoggedIn = !!token;

  let userIdFromToken = null;
  if (token) {
    try {
      const decodedToken = JSON.parse(atob(token.split(".")[1]));
      userIdFromToken =
        decodedToken?.sub ||
        decodedToken?.userId ||
        decodedToken?.id ||
        decodedToken?.user_id;
    } catch (e) {
      console.error("Errore nel parsing del token:", e);
    }
  }

  const isPostOwner =
    post.author?.id &&
    userIdFromToken &&
    String(post.author.id) === String(userIdFromToken);

  const handleUpdate = async () => {
    try {
      const res = await fetch(`http://localhost:8888/posts/${post.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ content: editContent }),
      });

      if (res.ok) {
        onPostUpdated(post.id, editContent);
        setIsEditing(false);
      } else {
        console.error("Errore nell'aggiornamento del post:", res.status);
      }
    } catch (err) {
      console.error("Errore nell'aggiornamento:", err);
    }
  };

  const handleDelete = async () => {
    if (!window.confirm("Sei sicuro di voler eliminare questo post?")) {
      return;
    }

    try {
      const res = await fetch(`http://localhost:8888/posts/${post.id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (res.ok) {
        onPostDeleted(post.id);
      } else {
        console.error("Errore nell'eliminazione del post:", res.status);
      }
    } catch (err) {
      console.error("Errore nell'eliminazione:", err);
    }
  };

  return (
    <div className={`card post-card ${isEditing ? "editing" : ""}`}>
      <div className="card-body">
        {/* INFO AUTORE */}
        <div
          style={{
            display: "flex",
            alignItems: "center",
            marginBottom: "15px",
          }}
        >
          <div>
            <strong>{post.author?.username || "Sconosciuto"}</strong>
            <br />
            <small style={{ color: "#666" }}>
              {post.datePost
                ? new Date(post.datePost).toLocaleDateString("it-IT")
                : post.date}
            </small>
          </div>
        </div>

        {/* CONTENUTO */}
        {isEditing ? (
          <textarea
            value={editContent}
            onChange={(e) => setEditContent(e.target.value)}
            rows={4}
            className="input-block"
            style={{ width: "100%", marginBottom: "10px" }}
          />
        ) : (
          <p className="card-text">{post.content}</p>
        )}

        {/* CORGI INFO */}
        {post.corgi && (
          <div className="alert alert-primary" style={{ marginTop: "10px" }}>
            <strong>🐕 {post.corgi.name}</strong> ({post.corgi.age} anni)
          </div>
        )}

        {/* BOTTONI */}
        <div
          style={{
            marginTop: "15px",
            display: "flex",
            gap: "10px",
            alignItems: "center",
          }}
        >
          {isLoggedIn && isPostOwner ? (
            <>
              {isEditing ? (
                <>
                  <button
                    onClick={handleUpdate}
                    style={{
                      marginTop: "10px",
                      color: "#0e0d0dff",
                      border: "none",
                      padding: "6px 12px",
                      borderRadius: "4px",
                      cursor: "pointer",
                      backgroundColor: "white", // ⬅️ CAMBIATO
                      fontSize: "14px",
                      marginRight: "8px",
                    }}
                  >
                    ✔️ Salva
                  </button>
                  <button
                    onClick={() => {
                      setIsEditing(false);
                      setEditContent(post.content);
                    }}
                    style={{
                      marginTop: "10px",
                      color: "#0e0d0dff",
                      border: "none",
                      padding: "6px 12px",
                      borderRadius: "4px",
                      cursor: "pointer",
                      backgroundColor: "white", // ⬅️ CAMBIATO
                      fontSize: "14px",
                      marginRight: "8px",
                    }}
                  >
                    ❌ Annulla
                  </button>
                </>
              ) : (
                <>
                  <button
                    onClick={() => setIsEditing(true)}
                    style={{
                      marginTop: "10px",
                      color: "#0e0d0dff",
                      border: "none",
                      padding: "6px 12px",
                      borderRadius: "4px",
                      cursor: "pointer",
                      backgroundColor: "white", // ⬅️ CAMBIATO
                      fontSize: "14px",
                      marginRight: "8px",
                    }}
                  >
                    ✏️ Modifica
                  </button>
                  <button
                    onClick={handleDelete}
                    style={{
                      marginTop: "10px",
                      color: "#0e0d0dff",
                      border: "none",
                      padding: "6px 12px",
                      borderRadius: "4px",
                      cursor: "pointer",
                      backgroundColor: "white", // ⬅️ CAMBIATO
                      fontSize: "14px",
                    }}
                  >
                    🗑️ Elimina
                  </button>
                </>
              )}
            </>
          ) : (
            isLoggedIn && <MessageButton recipientId={post.author?.id} />
          )}
        </div>

        <CommentSection postId={post.id} />
      </div>
    </div>
  );
};

export default PostCard;
