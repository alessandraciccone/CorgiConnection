import { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../css/Profilo.css";
import correrecorgi from "../assets/img/correrecorgi.png";

const Profilo = () => {
  const navigate = useNavigate();
  const [fotoProfilo, setFotoProfilo] = useState(null);
  const [utente, setUtente] = useState(null);
  const [modificaUtente, setModificaUtente] = useState(false);
  const [infoCane, setInfoCane] = useState("");

  const [profiloUtente, setProfiloUtente] = useState({
    username: "",
    email: "",
    firstName: "",
    lastName: "",
    city: "",
    province: "",
    registrationDate: "",
    profileImage: "",
  });

  const fileInputProfiloRef = useRef(null);
  const userId = "b3e0a997-5506-4e7e-8dd3-bd19d8989a66";

  useEffect(() => {
    const fetchUtente = async () => {
      try {
        const token = localStorage.getItem("token");
        if (!token) throw new Error("Token non trovato");

        const response = await fetch(`http://localhost:8888/users/${userId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) throw new Error("Errore nel recupero utente");
        const data = await response.json();
        setUtente(data);
        setProfiloUtente(data);
      } catch (error) {
        console.error("Errore:", error);
      }
    };

    fetchUtente();
  }, []);

  useEffect(() => {
    const storedFoto = localStorage.getItem("fotoProfilo");
    if (storedFoto) setFotoProfilo(storedFoto);
  }, []);

  useEffect(() => {
    const storedInfo = localStorage.getItem("infoCane");
    if (storedInfo) setInfoCane(storedInfo);
  }, []);

  const handleFotoProfilo = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onloadend = () => {
      const base64 = reader.result;
      setFotoProfilo(base64);
      localStorage.setItem("fotoProfilo", base64);
    };
    reader.readAsDataURL(file);
  };

  const handleChangeUtente = (e) => {
    const { name, value } = e.target;
    setProfiloUtente((prev) => ({ ...prev, [name]: value }));
  };

  const handleChangeInfoCane = (e) => {
    const value = e.target.value;
    setInfoCane(value);
    localStorage.setItem("infoCane", value);
  };

  const handleInviaUtente = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) throw new Error("Token non trovato");

      const response = await fetch(`http://localhost:8888/users/${userId}`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(profiloUtente),
      });

      if (!response.ok) throw new Error("Errore nell'aggiornamento profilo");
      const result = await response.json();
      console.log("Profilo utente aggiornato:", result);
    } catch (error) {
      console.error("Errore:", error);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
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
          Scrivi qualcosa sul tuo corgi: com’è, cosa ama fare… <br />e poi
          pubblica un post per conoscere nuovi amici a quattro zampe!
        </p>
      </div>

      <div className="row">
        <div className="col-12 col-lg-6 mb-4">
          <div className="card mt-2">
            <div className="card-body card-flex">
              <div className="dati-utente">
                {modificaUtente ? (
                  <>
                    <input
                      type="text"
                      name="username"
                      placeholder="Username"
                      value={profiloUtente.username}
                      onChange={handleChangeUtente}
                      className="input-block"
                    />
                    <input
                      type="email"
                      name="email"
                      placeholder="Email"
                      value={profiloUtente.email}
                      onChange={handleChangeUtente}
                      className="input-block"
                    />
                    <input
                      type="text"
                      name="firstName"
                      placeholder="Nome"
                      value={profiloUtente.firstName}
                      onChange={handleChangeUtente}
                      className="input-block"
                    />
                    <input
                      type="text"
                      name="lastName"
                      placeholder="Cognome"
                      value={profiloUtente.lastName}
                      onChange={handleChangeUtente}
                      className="input-block"
                    />
                    <input
                      type="text"
                      name="city"
                      placeholder="Città"
                      value={profiloUtente.city}
                      onChange={handleChangeUtente}
                      className="input-block"
                    />
                    <input
                      type="text"
                      name="province"
                      placeholder="Provincia"
                      value={profiloUtente.province}
                      onChange={handleChangeUtente}
                      className="input-block"
                    />
                    <div className="card mt-2">
                      <div className="card-body canecard">
                        <label htmlFor="infoCane">Info sul cane:</label>
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
                    >
                      ✔️
                    </span>
                  </>
                ) : utente ? (
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
                    <div className="bottoni-profilo">
                      <span
                        className="emoji-click"
                        onClick={() => setModificaUtente(true)}
                      >
                        🖊️
                      </span>
                    </div>
                  </>
                ) : (
                  <p>Caricamento dati utente...</p>
                )}
              </div>

              <div className="foto-col">
                {fotoProfilo && (
                  <img src={fotoProfilo} className="foto" alt="Foto profilo" />
                )}
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
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="xlogout">
        <p>
          Vuoi uscire dal tuo profilo?{" "}
          <span className="logout-link" onClick={handleLogout}>
            clicca qui
          </span>{" "}
          e torna presto a trovarci! 🐶
        </p>
      </div>
    </div>
  );
};

export default Profilo;
