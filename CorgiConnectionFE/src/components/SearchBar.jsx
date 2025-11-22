import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../css/SearchBar.css";

const SearchBar = ({ setFilters }) => {
  const [query, setQuery] = useState("");
  const [type, setType] = useState("posts"); // default search type
  const navigate = useNavigate();

  const handleSearch = () => {
    if (query.trim() === "") return;

    setFilters({ query, searchType: type }); // invia query + tipo
    navigate("/home");
  };

  return (
    <div className="search-bar">
      <input
        type="text"
        placeholder="Cerca qui! 🐶"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
      />
      <select value={type} onChange={(e) => setType(e.target.value)}>
        <option value="posts">Post</option>
        <option value="users">Utenti</option>
        <option value="pet-friendly-things">Dove andare</option>
      </select>
      <button onClick={handleSearch}>Cerca</button>
    </div>
  );
};

export default SearchBar;
