import { useState, useEffect } from "react";
import PostForm from "./PostForm";
import PostList from "./PostList";
import SearchBar from "./SearchBar";
import "../css/Home.css";
import corgipost from "../assets/img/corgipost.png";

const Home = () => {
  const [posts, setPosts] = useState([]);
  const [filters, setFilters] = useState({
    authorUsername: "",
    contentKeyword: "",
  });

  const fetchPosts = async () => {
    const token = localStorage.getItem("token");
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

    const res = await fetch(`http://localhost:8888/posts/search?${query}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!res.ok) {
      const errorText = await res.text();
      console.error("Errore nella ricerca dei post:", res.status, errorText);
      return;
    }

    let data = null;
    try {
      data = await res.json();
      setPosts(data.content || []);
    } catch (err) {
      console.error("Risposta non in formato JSON:", err);
    }
  };

  useEffect(() => {
    fetchPosts();
  }, [filters]);

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
        <PostForm onPostCreated={fetchPosts} />
        <SearchBar filters={filters} setFilters={setFilters} />
        <PostList posts={posts} onRefresh={fetchPosts} />
      </div>
    </>
  );
};

export default Home;
