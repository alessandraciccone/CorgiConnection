import { useState, useEffect } from "react";
import PostList from "./PostList";
import "../css/Home.css";
import corgipost from "../assets/img/corgipost.png";

const Home = () => {
  const [posts, setPosts] = useState([]);
  const token = localStorage.getItem("token");

  const fetchPosts = async () => {
    if (!token) {
      console.error("Token mancante: impossibile effettuare la richiesta.");
      return;
    }

    try {
      const baseUrl = import.meta.env.VITE_API_URL;
      const res = await fetch(`${baseUrl}/posts`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (!res.ok) {
        const errorText = await res.text();
        console.error("Errore nella ricerca dei post:", res.status, errorText);
        return;
      }

      const data = await res.json();
      const postsWithUser = (data.content || []).map((post) => ({
        ...post,
        authorUsername: post.author?.username || "Anonimo",
      }));

      setPosts(postsWithUser);
    } catch (err) {
      console.error("Errore durante fetchPosts:", err);
    }
  };

  useEffect(() => {
    fetchPosts();
  }, []);

  return (
    <>
      <div className="corgiPost">
        <img className="corgiPostImg" src={corgipost} alt="immagine corgi" />
        <p className="corgiPostP">
          Ehi tu, umano curioso! Se hai un amico a quattro zampe, scrivi un post
          e presentacelo! Io adoro scoprire nuovi musi da annusare virtualmente!
        </p>
      </div>

      <div className="home-container">
        <PostList posts={posts} />
      </div>
    </>
  );
};

export default Home;
