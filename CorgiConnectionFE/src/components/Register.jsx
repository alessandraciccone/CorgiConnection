import React, { useState } from "react";
import { Link } from "react-router-dom";

import "../css/Register.css";
import corgiregister from "../assets/img/corgiregister.png";

const Register = () => {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    firstName: "",
    lastName: "",
    city: "",
    province: "",
    profileImage: "",
  });

  const [success, setSuccess] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess(false);

    try {
      const response = await fetch("http://localhost:8888/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Errore nella registrazione");
      }

      setSuccess(true);
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
        <div className="form-group">
          <label htmlFor="username">Username 🧸 </label>
          <input
            type="text"
            id="username"
            name="username"
            value={formData.username}
            onChange={handleChange}
            placeholder="Scrivi il tuo Username 🐾"
            required
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
