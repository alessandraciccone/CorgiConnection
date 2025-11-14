import { useState } from "react";

const PostForm = ({ onPostCreated }) => {
  const [content, setContent] = useState("");
  const [photo, setPhoto] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const token = localStorage.getItem("token");
    if (!token) {
      alert("Devi essere autenticata per pubblicare un post.");
      return;
    }

    // 1. Creo il post sul backend (solo content)
    const res = await fetch("http://localhost:8888/posts", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ content }),
    });

    if (!res.ok) {
      console.error("Errore nella creazione del post:", await res.text());
      return;
    }

    const post = await res.json();

    // 2. Salvo la foto in localStorage come base64 (se presente)
    if (photo) {
      const reader = new FileReader();
      reader.onloadend = () => {
        localStorage.setItem(`postPhoto-${post.id}`, reader.result);
      };
      reader.readAsDataURL(photo);
    }

    // 3. Reset form
    setContent("");
    setPhoto(null);
    onPostCreated();
  };

  return (
    <form onSubmit={handleSubmit}>
      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
        placeholder="Scrivi il tuo post..."
      />
      <input type="file" onChange={(e) => setPhoto(e.target.files[0])} />
      <button type="submit">Pubblica</button>
    </form>
  );
};

export default PostForm;
