import React, { useState } from "react";
import { Link } from "react-router-dom";

import "../css/Register.css";
import corgiregister from "../assets/img/corgiregister.png";

const Register = () => {
  const [formData, setFormData] = useState({
    // stato per i dati del form. il setform serve per aggiornare i dati

    username: "",
    email: "",
    password: "",
    firstName: "",
    lastName: "",
    city: "",
    province: "",
    profileImage: "",
  });

  const [success, setSuccess] = useState(false); // stato per il successo della registrazione
  const [error, setError] = useState(""); // stato per gli errori

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };
  //..prev è lo spread operator che copia i valori precedenti di formData
  //con hanflechange aggiorno i valori di formData in base a quello che scrivo nei campi del form
  const handleSubmit = async (e) => {
    //invio del form
    e.preventDefault(); //previene il comportamento predefinito del form che è il refresh della pagina
    setError(""); //azzero gli errori
    setSuccess(false); //azzero il successo(come ad esempio se avevo già fatto una registrazione con successo e poi rifaccio il form)

    try {
      //blocco try catch per gestire gli errori
      const baseUrl = import.meta.env.VITE_API_URL; //prendo l'url dell'api dalle variabili d'ambiente
      const response = await fetch(`${baseUrl}/auth/register`, {
        //await per aspettare la risposta del server
        method: "POST",
        headers: {
          //intestazioni della richiesta
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData), //corpo della richiesta, trasformo i dati del form in json
      });

      if (!response.ok) {
        //se la risposta non è ok, lancio un errore
        const errorData = await response.json();
        throw new Error(errorData.message || "Errore nella registrazione");
      }

      setSuccess(true); //se la registrazione va a buon fine, setto il successo a true
      setFormData({
        username: "",
        email: "",
        password: "",
        firstName: "",
        lastName: "",
        city: "",
        province: "",
        profileImage: "",
      });
    } catch (err) {
      //gestione degli errori. se non va a un buon fine itnercetto l'errore e mando il messaggio
      setError(err.message);
    }
  };

  return (
    <>
      <div className="corgi-intro">
        <img src={corgiregister} alt="Corgi giocoso" className="corgi-image" />
        <p className="corgi-message">
          Ehi tu! Non vorrai mica perderti il mondo dei Corgi? Registrati e
          vieni a giocare con noi! 🐾
        </p>
      </div>
      <form onSubmit={handleSubmit}>
        {" "}
        {/* gestione dell'invio del form */}
        <div className="form-group">
          <label htmlFor="username">Username 🧸 </label>
          <input
            type="text"
            id="username"
            name="username"
            value={formData.username}
            onChange={handleChange}
            placeholder="Scrivi il tuo Username 🐾"
            required //campo obbligatorio
          />
        </div>
        <div className="form-group">
          <label htmlFor="firstName">Nome 🧝 </label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            value={formData.firstName}
            onChange={handleChange}
            placeholder="Scrivi il tuo nome 🐾"
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="lastName">Cognome 🧬 </label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            value={formData.lastName}
            onChange={handleChange}
            placeholder="Scrivi il tuo cognome 🐾"
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="city">Città 🏡</label>
          <input
            type="text"
            id="city"
            name="city"
            value={formData.city}
            onChange={handleChange}
            placeholder="Scrivi la tua città 🐾"
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="province">Provincia 🗺️</label>
          <input
            type="text"
            id="province"
            name="province"
            value={formData.province}
            onChange={handleChange}
            placeholder="Sigla provincia  🐾"
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="email">Email 📨</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            placeholder="Scrivi la tua email 🐾"
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">Password 🔑</label>
          <input
            type="password"
            id="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            placeholder="Scrivi la tua password 🐾"
            required
          />
        </div>
        {/* <div className="form-group">
        <label htmlFor="profileImage">Immagine profilo 🖼️</label>
        <input
          type="url"
          id="profileImage"
          name="profileImage"
          value={formData.profileImage}
          onChange={handleChange}
          placeholder="URL immagine profilo 🐾"
        />
      </div> */}
        <button type="submit">Invia</button>
        {success && (
          <p className="success-message">
            Registrazione completata con successo!
          </p>
        )}
        {error && <p className="error-message">Errore: {error}</p>}
      </form>
      <p className="accountLogin">
        {" "}
        Hai già un account? Presto, fai il{" "}
        <Link to="/login" className="login-link">
          Login!
        </Link>{" "}
        ❤️{" "}
      </p>
    </>
  );
};

export default Register;
