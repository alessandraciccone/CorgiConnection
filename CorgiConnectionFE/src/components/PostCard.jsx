import React, { useState } from "react";

const PostCard = ({ post, onPostUpdated, onPostDeleted }) => {
  const [editContent, setEditContent] = useState(post.content);
  const [isEditing, setIsEditing] = useState(false);

  const token = localStorage.getItem("token");

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
        console.error("Errore aggiornamento post:", res.status);
      }
    } catch (err) {
      console.error(err);
    }
  };

  const handleDelete = async () => {
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
        console.error("Errore eliminazione post:", res.status);
      }
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className={`card post-card ${isEditing ? "editing" : ""}`}>
      <div className="card-body">
        <p>
          <strong>Autore:</strong> {post.author?.username || "Sconosciuto"}
        </p>
        <p>
          <strong>Data:</strong> {post.date}
        </p>

        {isEditing ? (
          <textarea
            value={editContent}
            onChange={(e) => setEditContent(e.target.value)}
            rows={4}
            className="edit-textarea"
          />
        ) : (
          <p className="card-text">{post.content}</p>
        )}

        <button
          onClick={() => setIsEditing(!isEditing)}
          className="btn btn-secondary me-2"
        >
          {isEditing ? "❌" : "✏️"}
        </button>
        {isEditing && (
          <button onClick={handleUpdate} className="btn btn-success me-2">
            ✔️
          </button>
        )}

        <button onClick={handleDelete} className="btn btn-danger">
          ✖️
        </button>
      </div>
    </div>
  );
};

export default PostCard;
