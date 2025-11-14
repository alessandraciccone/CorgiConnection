import { useState } from "react";
import CommentSection from "./CommentSection";
import MessageButton from "./MessageButton";
import "../css/PostCard.css";

const PostCard = ({ post, onRefresh }) => {
  const [editing, setEditing] = useState(false);
  const [content, setContent] = useState(post.content);

  const handleDelete = async () => {
    await fetch(`http://localhost:8888/posts/${post.id}`, { method: "DELETE" });
    onRefresh();
  };

  const handleUpdate = async () => {
    await fetch(`http://localhost:8888/posts/${post.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ content }),
    });
    setEditing(false);
    onRefresh();
  };

  return (
    <div className="card">
      {post.photos && post.photos.length > 0 && (
        <img
          className="fotopost"
          src={post.photos[0].url}
          alt="Foto del post"
          style={{ width: "100%", borderRadius: "6px" }}
        />
      )}

      {editing ? (
        <>
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
          />
          <button onClick={handleUpdate}>Salva</button>
        </>
      ) : (
        <p>{post.content}</p>
      )}

      <div className="actions">
        <button onClick={() => setEditing(!editing)}>✏️</button>
        <button onClick={handleDelete}>🗑️</button>
        <MessageButton recipientId={post.author.id} />
      </div>

      <CommentSection postId={post.id} />
    </div>
  );
};

export default PostCard;
