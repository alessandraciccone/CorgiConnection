import { useState, useEffect } from "react";
import PostList from "./PostList";
import SearchBar from "./SearchBar";
import "../css/Home.css";
import corgipost from "../assets/img/corgipost.png";

const Home = () => {
  const [posts, setPosts] = useState([]);
  const [filters] = useState({
    authorUsername: "",
    contentKeyword: "",
  });

  const token = localStorage.getItem("token");

  const fetchPosts = async () => {
    if (!token) {
      console.error("Token mancante: impossibile effettuare la richiesta.");
      return;
    }

    const query = new URLSearchParams({
      ...filters,
      page: 0,
      size: 10,
      sortBy: "datePost",
    }).toString();

    try {
      const res = await fetch(`http://localhost:8888/posts/search?${query}`, {
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
  }, [filters]);

  return (
    <>
      {/* Immagine decorativa */}
      <div className="corgiPost">
        <img className="corgiPostImg" src={corgipost} alt="immagine corgi" />
        <p className="corgiPostP">
          Ehi tu, umano curioso! Se hai un amico a quattro zampe, scrivi un post
          e presentacelo! Io adoro scoprire nuovi musi da annusare virtualmente!
        </p>
      </div>

      <div className="home-container">
        {/* Se vuoi creare nuovi post, possiamo gestirlo direttamente dentro PostList */}
        {/* <SearchBar filters={filters} setFilters={setFilters} /> */}
        <PostList posts={posts} />
      </div>
    </>
  );
};

export default Home;
