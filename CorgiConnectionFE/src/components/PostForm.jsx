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

    // 1. Crea il post
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

    let post = null;
    try {
      post = await res.json();
    } catch (err) {
      console.error("Risposta non in formato JSON:", err);
    }

    // 2. Carica la foto se presente
    if (post && post.id && photo) {
      const formData = new FormData();
      formData.append("file", photo); // ✅ nome corretto per il backend

      const photoRes = await fetch(
        `http://localhost:8888/posts/${post.id}/photo`,
        {
          method: "POST", // ✅ deve essere POST, non PUT
          headers: {
            Authorization: `Bearer ${token}`,
          },
          body: formData,
        }
      );

      if (!photoRes.ok) {
        console.error(
          "Errore nel caricamento della foto:",
          await photoRes.text()
        );
      }
    }

    // 3. Reset del form
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
