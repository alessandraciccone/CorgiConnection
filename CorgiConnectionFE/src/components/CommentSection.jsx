import { useState, useEffect } from "react";

const CommentSection = ({ postId }) => {
  const [comments, setComments] = useState([]);
  const [text, setText] = useState("");

  // Carica i commenti del post dal localStorage
  useEffect(() => {
    const saved = localStorage.getItem(`comments-${postId}`);
    setComments(saved ? JSON.parse(saved) : []);
  }, [postId]);

  // Salva nel localStorage
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
      <ul>
        {comments.map((c) => (
          <li key={c.id}>
            {c.text}
            <button onClick={() => handleDelete(c.id)}>❌</button>
          </li>
        ))}
      </ul>

      <textarea
        value={text}
        onChange={(e) => setText(e.target.value)}
        placeholder="Scrivi un commento..."
      />
      <button onClick={handleSubmit}>Commenta</button>
    </div>
  );
};

export default CommentSection;
