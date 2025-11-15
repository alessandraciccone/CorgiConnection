import { useState } from "react";
import "../css/Profilo.css";
import "../css/SearchBar.css";

const SearchBar = ({ setFilters }) => {
  const [query, setQuery] = useState(""); // Per il valore di ricerca
  const [searchType] = useState("users"); // Tipo di ricerca

  const handleSearch = async () => {
    if (!query.trim()) return;

    setFilters((f) => ({
      ...f,
      query,
      searchType,
    }));

    try {
      let url = "";
      switch (searchType) {
        case "users":
          url = `http://localhost:8888/users/search?username=${query}`;
          break;
        case "posts":
          url = `http://localhost:8888/posts/search?contentKeyword=${query}`;
          break;
        case "pet-friendly-things":
          url = `http://localhost:8888/pet-friendly-things/search?descriptionKeyword=${query}`;
          break;
        case "quizzes":
          url = `http://localhost:3001/quizzes/search?keyword=${query}`;
          break;
        default:
          return;
      }

      const token = localStorage.getItem("token");
      const response = await fetch(url, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`HTTP ${response.status}: ${errorText}`);
      }

      const data = await response.json();
      console.log(data);
    } catch (error) {
      console.error("Errore durante la ricerca:", error);
    }
  };

  return (
    <div className="search-bar-container">
      <input
        className="search-input"
        type="text"
        id="paperInputs3"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder="Cerca..."
      />
      <button className="search-button" onClick={handleSearch}>
        Vai!
      </button>
    </div>
  );
};

export default SearchBar;
