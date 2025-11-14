import { useState, useEffect } from "react";

const CommentSection = ({ postId }) => {
  const [comments, setComments] = useState([]);
  const [text, setText] = useState("");

  useEffect(() => {
    const saved = localStorage.getItem(`comments-${postId}`);
    setComments(saved ? JSON.parse(saved) : []);
  }, [postId]);

  const saveComments = (newComments) => {
    setComments(newComments);
    localStorage.setItem(`comments-${postId}`, JSON.stringify(newComments));
  };

  const handleSubmit = () => {
    if (!text.trim()) return;

    const newComments = [
      ...comments,
      {
        id: Date.now(),
        text,
        date: new Date().toISOString(),
      },
    ];

    saveComments(newComments);
    setText("");
  };

  const handleDelete = (id) => {
    const newComments = comments.filter((c) => c.id !== id);
    saveComments(newComments);
  };

  return (
    <div className="comments">
      <ul className="comment-list">
        {comments.map((c) => (
          <li key={c.id} className="comment-item">
            {c.text}
            <button
              className="comment-delete"
              onClick={() => handleDelete(c.id)}
            >
              ❌
            </button>
          </li>
        ))}
      </ul>

      <textarea
        className="comment-textarea"
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
