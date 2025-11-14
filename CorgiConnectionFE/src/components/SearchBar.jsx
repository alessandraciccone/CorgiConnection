import { useState } from "react";
import "../css/Searchbar.css";

const SearchBar = ({ setFilters }) => {
  const [query, setQuery] = useState("");

  const handleSearch = () => {
    setFilters((f) => ({
      ...f,
      authorUsername: query,
      contentKeyword: query,
    }));
  };

  return (
    <div className="search-bar-container">
      <input
        type="text"
        className="search-input"
        placeholder="cerca qui il tuo nuovo amico a 4 zampe"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
      />
      <button className="search-button" onClick={handleSearch}>
        Vai!
      </button>
    </div>
  );
};

export default SearchBar;
