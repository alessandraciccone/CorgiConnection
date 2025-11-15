import React, { useEffect, useState } from "react";
import PostCard from "./PostCard";
const PostList = () => {
  const [posts, setPosts] = useState([]);
  const [newPostContent, setNewPostContent] = useState("");

  const token = localStorage.getItem("token");

  // Fetch dei post
  const fetchPosts = async () => {
    try {
      const res = await fetch("http://localhost:8888/posts", {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (res.ok) {
        const data = await res.json();
        setPosts(
          Array.isArray(data.content)
            ? data.content
            : Array.isArray(data)
            ? data
            : []
        );
      } else {
        console.error("Errore fetch posts:", res.status);
      }
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchPosts();
  }, []);

  // Aggiorna post
  const handlePostUpdated = (postId, newContent) => {
    setPosts((prev) =>
      prev.map((p) => (p.id === postId ? { ...p, content: newContent } : p))
    );
  };

  // Elimina post
  const handlePostDeleted = (postId) => {
    setPosts((prev) => prev.filter((p) => p.id !== postId));
  };

  // Crea nuovo post
  const handleNewPostSubmit = async (e) => {
    e.preventDefault();
    if (!newPostContent.trim()) return;

    try {
      const today = new Date().toISOString();
      const res = await fetch("http://localhost:8888/posts", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ content: newPostContent, datePost: today }),
      });

      if (res.ok) {
        const createdPost = await res.json();
        setPosts([createdPost, ...posts]);
        setNewPostContent("");
      } else {
        console.error("Errore nella creazione del post:", res.status);
      }
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <>
      {/* Form per nuovo post */}
      <div className="row post-list">
        <div className="col-12 col-lg-6 mb-4">
          <form
            onSubmit={handleNewPostSubmit}
            className="post-card"
            style={{
              backgroundColor: "#fff8e6",
              padding: "10px",
              borderRadius: "6px",
            }}
          >
            <div className="form-group">
              <label htmlFor="no-resize" className="lbl"></label>
              <textarea
                className="no-resize"
                id="no-resize"
                placeholder="Scrivi qui il tuo post 🐶"
                value={newPostContent}
                onChange={(e) => setNewPostContent(e.target.value)}
                rows="4"
                style={{
                  backgroundColor: "#fff8e6",
                  width: "100%",
                  padding: "10px",
                  border: "none",
                  borderRadius: "4px",
                }}
              />
            </div>
            <button
              type="submit"
              className="paper-btn btn-sc"
              style={{ marginTop: "10px" }}
            >
              Pubblica
            </button>
          </form>
        </div>
      </div>

      {/* Lista dei post */}
      <div className="row post-list">
        {posts.length === 0 && <p>Nessun post disponibile</p>}
        {posts.map((post) => (
          <div key={post.id} className="col-12 col-lg-6 mb-4">
            <div className="post-card">
              <PostCard
                post={post}
                onPostUpdated={handlePostUpdated}
                onPostDeleted={handlePostDeleted}
              />
            </div>
          </div>
        ))}
      </div>
    </>
  );
};

export default PostList;
