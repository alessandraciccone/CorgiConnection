import { useEffect, useState } from "react";
import "../css/CosaFacciamo.css";
import turisti from "../assets/img/turisti.png";

const regioni = [
  { display: "🐻 Abruzzo", name: "Abruzzo" },
  { display: "Basilicata 🍞", name: "Basilicata" },
  { display: "🌶️ Calabria", name: "Calabria" },
  { display: "Campania 🌋", name: "Campania" },
  { display: "🥟 Emilia-Romagna", name: "Emilia-Romagna" },
  { display: "Friuli Venezia Giulia 🌼", name: "Friuli Venezia Giulia" },
  { display: "🏛️ Lazio", name: "Lazio" },
  { display: "Liguria 🌿", name: "Liguria" },
  { display: "🛕 Lombardia", name: "Lombardia" },
  { display: "Marche 🎭", name: "Marche" },
  { display: "🌾 Molise", name: "Molise" },
  { display: "Piemonte 🍫", name: "Piemonte" },
  { display: "🌳 Puglia", name: "Puglia" },
  { display: "Sardegna 🏝️", name: "Sardegna" },
  { display: "🍊 Sicilia", name: "Sicilia" },
  { display: "Toscana 🌻", name: "Toscana" },
  { display: "🍎 Trentino-Alto Adige", name: "Trentino-Alto Adige" },
  { display: "Umbria🫒", name: "Umbria" },
  { display: "🏔️ Valle d'Aosta", name: "Valle d'Aosta" },
  { display: "Veneto🍷", name: "Veneto" },
];

const emojiPerTipo = (tipo) => {
  switch (tipo) {
    case "RESTAURANT":
      return "🍽️";
    case "VET":
      return "🐾";
    case "HOTEL":
      return "🏨";
    case "PARK":
      return "🌳";
    case "SHOP":
      return "🛍️";
    case "EVENT":
      return "🎉";
    case "BEACH":
      return "🏖️";
    default:
      return "❓";
  }
};

const CosaFacciamo = () => {
  const [datiPerRegione, setDatiPerRegione] = useState({});
  const [loading, setLoading] = useState(true);
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchDatiPerRegione = async (regione) => {
      let page = 0;
      const size = 100;
      let tuttiIDati = [];
      let hasMore = true;

      while (hasMore) {
        try {
          const res = await fetch(
            `http://localhost:8888/pet-friendly-things/search?region=${encodeURIComponent(
              regione.name
            )}&page=${page}&size=${size}`,
            { headers: { Authorization: `Bearer ${token}` } }
          );

          if (res.ok) {
            const data = await res.json();
            const nuovi = data.content || data;
            tuttiIDati = [...tuttiIDati, ...nuovi];
            hasMore = data.content ? !data.last : false;
            page++;
          } else {
            console.error(`Errore per ${regione.name}:`, res.status);
            hasMore = false;
          }
        } catch (err) {
          console.error(`Errore fetch ${regione.name}:`, err);
          hasMore = false;
        }
      }

      setDatiPerRegione((prev) => ({
        ...prev,
        [regione.display]: tuttiIDati,
      }));
    };

    const fetchAll = async () => {
      setLoading(true);
      await Promise.all(regioni.map((reg) => fetchDatiPerRegione(reg)));
      setLoading(false);
    };

    fetchAll();
  }, [token]);

  if (loading) return <p>Caricamento dati per tutte le regioni...</p>;

  return (
    <div className="container t">
      <div className="turisti">
        <img
          className="turistiImg"
          src={turisti}
          alt="ragazza e corgi turisti"
        />
        <p className="turistitx">
          🧡 "Zaino in spalla, zampette pronte:
          <br />
          l’Italia ci aspetta con mille avventure e coccole pet-friendly!"
        </p>
      </div>

      <div className="collapsiblescs-wrapper">
        <div className="row">
          {regioni.map((regione, index) => (
            <div className="collapsiblecs" key={regione.display}>
              <input type="checkbox" id={`toggle-${index}`} />
              <label
                htmlFor={`toggle-${index}`}
                className="collapsiblecs-label"
              >
                {regione.display}
              </label>

              <div className="collapsiblecs-body">
                {datiPerRegione[regione.display]?.length > 0 ? (
                  <ul>
                    {datiPerRegione[regione.display].map((item, i) => (
                      <li key={i}>
                        <span>
                          {emojiPerTipo(item.type)}{" "}
                          <strong>{item.petFriendlyName}</strong>
                        </span>
                        <ul>
                          {item.cityThing && (
                            <li>
                              <strong>Città:</strong> {item.cityThing.trim()}
                            </li>
                          )}
                          {item.districtThing && (
                            <li>
                              <strong>Provincia:</strong> {item.districtThing}
                            </li>
                          )}
                          {item.descriptionThing && (
                            <li>
                              <strong>Descrizione:</strong>{" "}
                              {item.descriptionThing}
                            </li>
                          )}
                          {item.address && (
                            <li>
                              <strong>Indirizzo:</strong> {item.address}
                            </li>
                          )}
                        </ul>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <span>Nessun risultato per questa regione.</span>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default CosaFacciamo;
