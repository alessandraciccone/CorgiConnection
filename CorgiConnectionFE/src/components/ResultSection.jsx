import React from "react";
import { useNavigate } from "react-router-dom";
import "../css/ResultSection.css";

const ResultSection = ({ results, searchType }) => {
  const navigate = useNavigate();

  const handleClick = (item) => {
    if (searchType === "users") {
      // porta al profilo corretto di quell'utente
      navigate(`/profilo/${item.id}`);
    } else if (searchType === "posts") {
      navigate(`/post/${item.id}`);
    } else if (searchType === "pet-friendly-things") {
      navigate(`/pet/${item.id}`);
    }
  };

  if (!results || results.length === 0) return <p>Nessun risultato trovato.</p>;

  return (
    <div className="results-container">
      <div className="cards-grid">
        {results.map((item, index) => (
          <div
            key={`${item.id}-${index}`}
            className={`result-card ${searchType}`}
            onClick={() => handleClick(item)}
          >
            {searchType === "users" && (
              <>
                👤 <h4>@{item.username}</h4>
              </>
            )}
            {searchType === "posts" && (
              <>
                📝 <p>{item.content}</p>
              </>
            )}
            {searchType === "pet-friendly-things" && (
              <>
                🐾 <h4>{item.petFriendlyName}</h4>
                <p>{item.address}</p>
                <p>{item.cityThing}</p>
                <p>{item.districtThing}</p>
                <p>{item.region}</p>
              </>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default ResultSection;
