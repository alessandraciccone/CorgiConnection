import { useState } from "react";
import CommentSection from "./CommentSection";
import MessageButton from "./MessageButton";
import "../css/PostCard.css";

const PostCard = ({ post, onRefresh }) => {
  const [editing, setEditing] = useState(false);
  const [content, setContent] = useState(post.content);
  const [newPhoto, setNewPhoto] = useState(null);

  const currentUserId = localStorage.getItem("userId");
  const isAuthor = currentUserId && post.author.id === currentUserId;

  const token = localStorage.getItem("token");

  // Recupero la foto dal localStorage
  const storedPhoto = localStorage.getItem(`postPhoto-${post.id}`);
  const photoToShow = newPhoto ? URL.createObjectURL(newPhoto) : storedPhoto;

  const handleDelete = async () => {
    await fetch(`http://localhost:8888/posts/${post.id}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    });

    // Rimuovo la foto dal localStorage
    localStorage.removeItem(`postPhoto-${post.id}`);

    onRefresh();
  };

  const handleUpdate = async () => {
    await fetch(`http://localhost:8888/posts/${post.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ content }),
    });

    // Aggiorno la foto in localStorage se selezionata
    if (newPhoto) {
      const reader = new FileReader();
      reader.onloadend = () => {
        localStorage.setItem(`postPhoto-${post.id}`, reader.result);
      };
      reader.readAsDataURL(newPhoto);
    }

    setEditing(false);
    onRefresh();
  };

  return (
    <div className="cardPost">
      <h3 className="author-name">
        {post.author.firstName} {post.author.lastName} (@{post.author.username})
      </h3>

      {editing ? (
        <>
          <textarea
            className="edit-textarea"
            value={content}
            onChange={(e) => setContent(e.target.value)}
          />
          <input type="file" onChange={(e) => setNewPhoto(e.target.files[0])} />
          <button className="save-btn" onClick={handleUpdate}>
            Salva
          </button>
        </>
      ) : (
        <p className="post-text">{post.content}</p>
      )}

      {photoToShow && <img className="fotopost" src={photoToShow} alt="Post" />}

      <div className="actions">
        {isAuthor ? (
          <>
            <button className="edit-btn" onClick={() => setEditing(!editing)}>
              ✏️
            </button>
            <button className="delete-btn" onClick={handleDelete}>
              🗑️
            </button>
          </>
        ) : (
          <MessageButton recipientId={post.author.id} />
        )}
      </div>

      <hr className="divider" />

      <CommentSection postId={post.id} />
    </div>
  );
};

export default PostCard;
