import { useState, useRef, useEffect } from "react";

const Profilo = () => {
  const [fotoProfilo, setFotoProfilo] = useState(null);
  const [fotoCane, setFotoCane] = useState(null);
  const [utente, setUtente] = useState(null);
  const [modificaUtente, setModificaUtente] = useState(false);
  const [modificaCane, setModificaCane] = useState(false);

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

  const [cane, setCane] = useState({
    name: "",
    age: "",
    gender: "",
    type: "",
    color: "",
    personality: "",
  });

  const fileInputProfiloRef = useRef(null);
  const fileInputCaneRef = useRef(null);

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

  const handleFotoProfilo = (e) => {
    const file = e.target.files[0];
    if (file) setFotoProfilo(URL.createObjectURL(file));
  };

  const handleFotoCane = (e) => {
    const file = e.target.files[0];
    if (file) setFotoCane(URL.createObjectURL(file));
  };

  const handleChangeUtente = (e) => {
    const { name, value } = e.target;
    setProfiloUtente((prev) => ({ ...prev, [name]: value }));
  };

  const handleChangeCane = (e) => {
    const { name, value } = e.target;
    setCane((prev) => ({ ...prev, [name]: value }));
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

  const handleInviaCane = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) throw new Error("Token non trovato");

      const response = await fetch("http://localhost:8888/corgis", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          ...cane,
          photo: "",
          owner: { id: userId },
        }),
      });

      if (!response.ok) throw new Error("Errore nella creazione del cane");
      const result = await response.json();
      console.log("Cane creato:", result);
    } catch (error) {
      console.error("Errore nella creazione del cane:", error);
    }
  };

  return (
    <div className="container">
      <h3>Profilo</h3>
      <div className="row">
        {/* Colonna Profilo Utente */}
        <div className="col-12 col-lg-6 mb-4">
          <div className="card">
            {fotoProfilo && (
              <img
                src={fotoProfilo}
                alt="Foto profilo"
                style={{ width: "100%" }}
              />
            )}
            <div className="card-body">
              <input
                type="file"
                accept="image/*"
                ref={fileInputProfiloRef}
                onChange={handleFotoProfilo}
                style={{ display: "none" }}
              />
              <button
                className="btn-small"
                onClick={() => fileInputProfiloRef.current.click()}
              >
                Carica foto profilo
              </button>
            </div>
          </div>

          <div className="card mt-2">
            <div className="card-body">
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
                  <button
                    className="btn-small mt-2"
                    onClick={() => {
                      handleInviaUtente();
                      setModificaUtente(false);
                    }}
                  >
                    💾 Salva profilo utente
                  </button>
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
                  <button
                    className="btn-small mt-2"
                    onClick={() => setModificaUtente(true)}
                  >
                    ✏️ Modifica profilo
                  </button>
                </>
              ) : (
                <p>Caricamento dati utente...</p>
              )}
            </div>
          </div>
        </div>

        {/* Colonna Profilo Cane */}
        <div className="col-12 col-lg-6 mb-4">
          <h3>Profilo cane</h3>
          <div className="card">
            {fotoCane && (
              <img src={fotoCane} alt="Foto cane" style={{ width: "100%" }} />
            )}
            <div className="card-body">
              <input
                type="file"
                accept="image/*"
                ref={fileInputCaneRef}
                onChange={handleFotoCane}
                style={{ display: "none" }}
              />
              <button
                className="btn-small"
                onClick={() => fileInputCaneRef.current.click()}
              >
                Carica foto cane
              </button>

              {modificaCane ? (
                <>
                  <input
                    type="text"
                    name="name"
                    placeholder="Nome"
                    value={cane.name}
                    onChange={handleChangeCane}
                    className="input-block"
                  />
                  <input
                    type="number"
                    name="age"
                    placeholder="Età"
                    value={cane.age}
                    onChange={handleChangeCane}
                    className="input-block"
                  />
                  <input
                    type="text"
                    name="gender"
                    placeholder="Genere"
                    value={cane.gender}
                    onChange={handleChangeCane}
                    className="input-block"
                  />
                  <input
                    type="text"
                    name="type"
                    placeholder="Razza"
                    value={cane.type}
                    onChange={handleChangeCane}
                    className="input-block"
                  />
                  <input
                    type="text"
                    name="color"
                    placeholder="Colore"
                    value={cane.color}
                    onChange={handleChangeCane}
                    className="input-block"
                  />
                  <input
                    type="text"
                    name="personality"
                    placeholder="Personalità"
                    value={cane.personality}
                    onChange={handleChangeCane}
                    className="input-block"
                  />
                  <button
                    className="btn-small mt-2"
                    onClick={() => {
                      handleInviaCane();
                      setModificaCane(false);
                    }}
                  >
                    💾 Salva profilo cane
                  </button>
                </>
              ) : (
                <>
                  <h4 className="card-title">
                    {cane.name || "Nome non inserito"}
                  </h4>
                  <p className="card-text">Età: {cane.age}</p>
                  <p className="card-text">Genere: {cane.gender}</p>
                  <p className="card-text">Razza: {cane.type}</p>
                  <p className="card-text">Colore: {cane.color}</p>
                  <p className="card-text">Personalità: {cane.personality}</p>
                  <button
                    className="btn-small mt-2"
                    onClick={() => setModificaCane(true)}
                  >
                    ✏️ Modifica profilo cane
                  </button>
                </>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profilo;
