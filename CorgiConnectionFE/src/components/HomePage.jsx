import React, { useEffect, useState, useRef } from "react";
import ResultSection from "./ResultSection";
import scoperta from "../assets/img/scoperta.png";
import "../css/HomePage.css";

const Homepage = ({ filters }) => {
  const [results, setResults] = useState([]);
  const [page, setPage] = useState(0); // pagina corrente
  const [hasMore, setHasMore] = useState(true); // se ci sono altri risultati
  const loader = useRef(null);

  useEffect(() => {
    if (!filters.query) return;
    setResults([]); // reset risultati alla nuova query
    setPage(0);
    setHasMore(true);
  }, [filters]);

  useEffect(() => {
    if (!filters.query || !hasMore) return;

    const fetchResults = async () => {
      const token = localStorage.getItem("token");
      if (!token) return console.error("Token mancante!");

      let url = "";
      const size = 20; // fetch 20 alla volta
      if (filters.searchType === "users")
        url = `http://localhost:8888/users/search?username=${filters.query}&page=${page}&size=${size}`;
      if (filters.searchType === "posts")
        url = `http://localhost:8888/posts/search?contentKeyword=${filters.query}&page=${page}&size=${size}&sortBy=datePost`;
      if (filters.searchType === "pet-friendly-things")
        url = `http://localhost:8888/pet-friendly-things/search?descriptionKeyword=${filters.query}&page=${page}&size=${size}`;

      try {
        const res = await fetch(url, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const data = await res.json();

        const newResults = data.content || data;
        setResults((prev) => {
          const combined = [...prev, ...newResults];
          // crea una mappa per rimuovere duplicati basati su id
          const uniqueResults = Array.from(
            new Map(combined.map((item) => [item.id, item])).values()
          );
          return uniqueResults;
        });

        if (!data.content || data.content.length < size) setHasMore(false); // fine dei risultati
      } catch (err) {
        console.error("Errore fetch:", err);
      }
    };

    fetchResults();
  }, [filters, page]);

  // observer per lo scroll
  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasMore) {
          setPage((prev) => prev + 1);
        }
      },
      { threshold: 1 }
    );

    if (loader.current) observer.observe(loader.current);

    return () => {
      if (loader.current) observer.unobserve(loader.current);
    };
  }, [hasMore]);

  return (
    <div className="container">
      {/* Sezione introduttiva */}
      <div className="scoperta">
        <img
          className="scoprirecose"
          src={scoperta}
          alt="ragazza e corgi che scoprono"
        />
        <p className="textscoperta">
          "Non sappiamo cosa sia, ma se ci fa sorridere… <br />
          vale la pena cercarlo!"
        </p>
      </div>

      <ResultSection results={results} searchType={filters.searchType} />
      {hasMore && (
        <div ref={loader} style={{ height: "50px", textAlign: "center" }}>
          Caricamento...
        </div>
      )}
    </div>
  );
};

export default Homepage;
