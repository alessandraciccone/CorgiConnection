import { useState, useRef, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import "../css/Profilo.css";
import correrecorgi from "../assets/img/correrecorgi.png";

// --- FUNZIONI INDEXEDDB ---
const openDB = () => {
  return new Promise((resolve, reject) => {
    // Apri (o crea) il database indexedDB che funziona in modo asincrono. indebex è più ampio del localstorage perché può salvare file binari come le immagini.
    const request = indexedDB.open("UserDB", 1);
    request.onupgradeneeded = (event) => {
      // Crea l'object store se non esiste
      const db = event.target.result; // Ottieni il database
      if (!db.objectStoreNames.contains("profileImages")) {
        // Crea un object store per le immagini del profilo
        db.createObjectStore("profileImages", { keyPath: "userId" }); // Usa userId come chiave primaria
      }
    };
    request.onsuccess = () => resolve(request.result); // Risolvi la promessa con il database aperto
    request.onerror = () => reject(request.error);
  });
};

const saveProfileImage = async (userId, blob) => {
  const db = await openDB();
  const tx = db.transaction("profileImages", "readwrite");
  const store = tx.objectStore("profileImages");
  store.put({ userId, image: blob });
  return tx.complete; // Completa la transazione
};

const getProfileImage = async (userId) => {
  const db = await openDB(); // Apri il database
  return new Promise((resolve) => {
    const tx = db.transaction("profileImages", "readonly");
    const store = tx.objectStore("profileImages");
    const request = store.get(userId);
    request.onsuccess = () => resolve(request.result?.image || null);
  });
};

const Profilo = () => {
  const navigate = useNavigate();
  const { userId: profileUserId } = useParams(); // Ottieni l'userId dai parametri URL (se presente)

  const [utente, setUtente] = useState(null); //nullo perché inizialmente non ci sono dati
  const [profiloUtente, setProfiloUtente] = useState({
    //memorizzo i dati del profilo che possono essere modificati
    username: "",
    email: "",
    firstName: "",
    lastName: "",
    city: "",
    province: "",
  });
  const [fotoProfilo, setFotoProfilo] = useState(null); //stato per l'immagine del profilo
  const [infoCane, setInfoCane] = useState("");
  const [modificaUtente, setModificaUtente] = useState(false); //false perché inizialmente non si sta modificando

  const fileInputProfiloRef = useRef(null); //useRef per il caricamento della foto del profilo

  //  Determina se è il proprio profilo
  const myUserId = localStorage.getItem("userId");
  const isMyProfile = !profileUserId || profileUserId === myUserId; // Se non c'è userId nei parametri o corrisponde a quello salvato, è il proprio profilo
  const targetUserId = profileUserId || myUserId; // Usa l'userId dai parametri o quello salvato

  // --- FETCH DATI UTENTE ---
  useEffect(() => {
    // esegue il codice al caricamento del componente o al cambiamento di targetUserId
    const token = localStorage.getItem("token");
    if (!token || !targetUserId) return;

    const fetchUtente = async () => {
      try {
        const baseUrl = import.meta.env.VITE_API_URL;
        const response = await fetch(`${baseUrl}/users/${targetUserId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (!response.ok) throw new Error("Errore nel recupero utente");

        const data = await response.json();
        setUtente(data);
        setProfiloUtente(data);

        const blob = await getProfileImage(targetUserId); // Recupera l'immagine del profilo da indexedDB
        if (blob) setFotoProfilo(URL.createObjectURL(blob)); // Crea un URL per l'immagine e aggiornala nello stato

        const infoSalvata = localStorage.getItem(`infoCane-${targetUserId}`); // Recupera le info del cane dal localStorage
        setInfoCane(infoSalvata || "");
      } catch (error) {
        console.error(error);
      }
    };

    fetchUtente(); // Chiamata alla funzione di fetch
  }, [targetUserId]); // Esegui quando targetUserId cambia

  // --- FUNZIONI FOTO PROFILO ---
  const handleFotoProfilo = (e) => {
    const file = e.target.files[0];
    const userId = localStorage.getItem("userId");
    if (!file || !userId) return; //se manca il file o l'userId esci

    const reader = new FileReader(); // legge il mfile come reader o base64
    reader.onload = (event) => {
      // quando il file è caricato lo processa in IMG
      const img = new Image();
      img.onload = () => {
        // quando l'immagine è caricata la ridimensiona se necessario
        const canvas = document.createElement("canvas"); // crea un canvas per il ridimensionamento
        const maxSize = 500;
        let { width, height } = img; // ottieni larghezza e altezza originali

        if (width > height) {
          // controlla se la larghezza è maggiore dell'altezza
          if (width > maxSize) {
            height *= maxSize / width;
            width = maxSize;
          }
        } else {
          if (height > maxSize) {
            width *= maxSize / height;
            height = maxSize;
          }
        }

        canvas.width = width; // imposta le nuove dimensioni
        canvas.height = height;
        canvas.getContext("2d").drawImage(img, 0, 0, width, height); // disegna l'immagine ridimensionata nel canvas

        canvas.toBlob(async (blob) => {
          // converte il canvas in un blob
          // blob è l'immagine ridimensionata
          if (blob.size < 5 * 1024 * 1024) {
            await saveProfileImage(userId, blob); // salva l'immagine ridimensionata in indexedDB
            setFotoProfilo(URL.createObjectURL(blob)); // aggiorna lo stato con la nuova immagine
          } else {
            alert("Immagine troppo grande, prova un file più leggero!");
          }
        }, "image/png"); // formato dell'immagine
      };
      img.src = event.target.result;
    };
    reader.readAsDataURL(file);
  };

  // --- CAMBIO DATI UTENTE --
  const handleChangeUtente = (e) => {
    const { name, value } = e.target;
    setProfiloUtente((prev) => ({ ...prev, [name]: value }));
  };

  const handleChangeInfoCane = (e) => {
    const value = e.target.value;
    const userId = localStorage.getItem("userId");
    setInfoCane(value);
    localStorage.setItem(`infoCane-${userId}`, value);
  };

  const handleInviaUtente = async () => {
    // Invia i dati modificati al server
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");
    if (!token || !userId) return; //se manca il token o l'userId esci

    try {
      const baseUrl = import.meta.env.VITE_API_URL;
      const response = await fetch(`${baseUrl}/users/${userId}`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(profiloUtente),
      });
      if (!response.ok) throw new Error("Errore nell'aggiornamento profilo");
    } catch (error) {
      console.error(error);
    }
  };

  // --- LOGOUT ---
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    setUtente(null);
    setFotoProfilo(null);
    setInfoCane("");
    navigate("/login");
  };

  return (
    <div className="container">
      <div className="goCorgi">
        <img
          className="CCorgi"
          src={correrecorgi}
          alt="Corgi e ragazza che corrono"
        />
        <p className="textCorgi">
          Scrivi qualcosa su di te e sul tuo cagnolino… <br />e poi pubblica un
          post per conoscere nuovi amici a quattro zampe!
        </p>
      </div>

      <div className="row">
        <div className="col-12 col-lg-6 mb-4">
          <div className="card mt-2 " style={{ border: "1px solid #ff7f50" }}>
            <div className="card-body card-flex">
              <div className="dati-utente">
                {/*  Modifica SOLO nel proprio profilo */}
                {isMyProfile && modificaUtente ? (
                  <>
                    {[
                      "username",
                      "email",
                      "firstName",
                      "lastName",
                      "city",
                      "province",
                    ].map(
                      (
                        field //.map serve per creare un array di input in base ai campi
                      ) => (
                        <input
                          key={field}
                          type={field === "email" ? "email" : "text"} //tipo email per il campo email il ? serve per fare una condizione inline. se input è email allora type email altrimenti text
                          name={field}
                          placeholder={
                            field.charAt(0).toUpperCase() + field.slice(1) //prima lettera maiuscola
                          }
                          value={profiloUtente[field]}
                          onChange={handleChangeUtente}
                          className="input-block"
                        />
                      )
                    )}
                    <div className="card mt-2">
                      <div className="card-body canecard">
                        <textarea
                          id="infoCane"
                          value={infoCane}
                          onChange={handleChangeInfoCane}
                          className="input-block cane-textarea"
                          rows={4}
                        />
                      </div>
                    </div>
                    <span
                      className="emoji-click"
                      onClick={() => {
                        handleInviaUtente();
                        setModificaUtente(false);
                      }}
                      style={{
                        border: "1px solid #ff7f50",
                        marginTop: "3px",
                        display: "inline-block",
                      }}
                    >
                      ✔️
                    </span>
                  </>
                ) : utente ? ( // mostra i dati utente se non si sta modificando
                  <>
                    <h4 className="card-title">
                      {utente.firstName} {utente.lastName}
                    </h4>
                    <p className="card-text">Username: {utente.username}</p>
                    <p className="card-text">Email: {utente.email}</p>
                    <p className="card-text">Città: {utente.city}</p>
                    <p className="card-text">Provincia: {utente.province}</p>
                    {infoCane && (
                      <div className="card mt-2">
                        <div className="card-body canecard">
                          <p className="info-cane">{infoCane}</p>
                        </div>
                      </div>
                    )}
                    {/*  Pulsante modifica SOLO nel proprio profilo */}
                    {isMyProfile && (
                      <div className="bottoni-profilo">
                        <span
                          className="emoji-click"
                          onClick={() => setModificaUtente(true)}
                          style={{ border: "1px solid #ff7f50" }}
                        >
                          🖊️
                        </span>
                      </div>
                    )}
                  </>
                ) : (
                  <p>Caricamento dati utente...</p>
                )}
              </div>

              <div className="foto-col">
                {fotoProfilo && (
                  <img
                    src={fotoProfilo}
                    className="foto"
                    alt="Foto profilo"
                    style={{ border: "1px solid #ff7f50" }}
                  />
                )}
                {/*  Carica foto SOLO nel proprio profilo */}
                {isMyProfile && (
                  <>
                    <input
                      type="file"
                      accept="image/*"
                      ref={fileInputProfiloRef}
                      onChange={handleFotoProfilo}
                      style={{ display: "none" }}
                    />
                    <button
                      className="btn-small-profilo mt-2"
                      onClick={() => fileInputProfiloRef.current.click()}
                    >
                      <p className="prof">Carica foto profilo 📷</p>
                    </button>
                  </>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/*  Logout SOLO nel proprio profilo */}
      {isMyProfile && (
        <div className="xlogout">
          <p>
            Vuoi uscire dal tuo profilo?{" "}
            <span className="logout-link" onClick={handleLogout}>
              clicca qui
            </span>{" "}
            e torna presto a trovarci! 🐶
          </p>
        </div>
      )}
    </div>
  );
};

export default Profilo;
