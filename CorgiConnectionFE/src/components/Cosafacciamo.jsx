import { useEffect, useState } from "react";
import "../css/CosaFacciamo.css";
import turisti from "../assets/img/turisti.png";

// Regione
const regioniPure = [
  "Abruzzo",
  "Basilicata",
  "Calabria",
  "Campania",
  "Emilia-Romagna",
  "Friuli Venezia Giulia",
  "Lazio",
  "Liguria",
  "Lombardia",
  "Marche",
  "Molise",
  "Piemonte",
  "Puglia",
  "Sardegna",
  "Sicilia",
  "Toscana",
  "Trentino-Alto Adige",
  "Umbria",
  "Valle d'Aosta",
  "Veneto",
];

// Regione con emoji (per il label)
const regioniConEmoji = [
  "🐺 Abruzzo",
  "Basilicata 🗿",
  "🌶️ Calabria",
  "Campania 🍕",
  "🍝 Emilia-Romagna",
  "Friuli Venezia Giulia 🍇",
  "🏛️ Lazio",
  "Liguria 🌊",
  "🏙️ Lombardia",
  "Marche 🌀",
  "🌾 Molise",
  "Piemonte 🍫",
  "🌵 Puglia",
  "Sardegna 🐑",
  "🌋 Sicilia",
  "Toscana 🍷",
  "🏔️ Trentino-Alto Adige",
  "Umbria 🍃",
  "⛷️ Valle d'Aosta",
  "Veneto 🛶",
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
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchDatiPerRegione = async (regione) => {
      let page = 0;
      const size = 100;
      let tuttiIDati = [];
      let hasMore = true;

      while (hasMore) {
        try {
          const baseUrl = import.meta.env.VITE_API_URL;
          const res = await fetch(
            `${baseUrl}/pet-friendly-things/search?region=${regione}&page=${page}&size=${size}`,
            { headers: { Authorization: `Bearer ${token}` } }
          );

          if (res.ok) {
            const data = await res.json();
            tuttiIDati = [...tuttiIDati, ...(data.content || [])];
            hasMore = !data.last;
            page++;
          } else {
            console.error(`Errore per ${regione}:`, res.status);
            hasMore = false;
          }
        } catch (err) {
          console.error(`Errore fetch ${regione}:`, err);
          hasMore = false;
        }
      }

      setDatiPerRegione((prev) => ({
        ...prev,
        [regione]: tuttiIDati,
      }));
    };

    // Usa i nomi puliti per le fetch
    regioniPure.forEach((regione) => fetchDatiPerRegione(regione));
  }, [token]);

  return (
    <div className="container t">
      {/* Sezione introduttiva */}
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

      {/* Wrapper con sfondo giallo */}
      <div className="collapsiblescs-wrapper">
        <div className="row">
          {regioniConEmoji.map((regioneEmoji, index) => (
            <div className="collapsiblecs" key={regioneEmoji}>
              <input type="checkbox" id={`toggle-${index}`} />
              <label
                htmlFor={`toggle-${index}`}
                className="collapsiblecs-label"
              >
                {regioneEmoji}
              </label>

              <div className="collapsiblecs-body">
                {datiPerRegione[regioniPure[index]]?.length > 0 ? (
                  <ul>
                    {datiPerRegione[regioniPure[index]].map((item, i) => (
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
