import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import ragazza from "../assets/img/ragazza.png";
import "../css/Login.css";

const Login = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    username: "",
    password: "",
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
      const response = await fetch("http://localhost:8888/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Errore nel login");
      }
      const data = await response.json();
      localStorage.setItem("token", data.token);
      localStorage.setItem("userId", data.userId); // ✅ fondamentale

      setSuccess(true);
      setFormData({ username: "", password: "" });
      console.log("Risposta login:", data);

      // ✅ Vai al profilo
      navigate("/profilo");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <>
      <div className="girlDiv">
        <img src={ragazza} className="girl-image" alt="ragazza con corgi" />
        <p className="girl-intro">
          Hai già fatto amicizia con i nostri corgi? Allora non perdere tempo…
          fai il login e torna a scodinzolare con noi! 💛
        </p>
      </div>

      <form onSubmit={handleSubmit} className="login-form">
        <div className="form-group">
          <label htmlFor="username">Username 🧸</label>
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
          <label htmlFor="password">Password 🔑</label>
          <input
            type="password"
            id="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            placeholder="Scrivi la tua Password 🐾"
            required
          />
        </div>

        <button type="submit">Login</button>

        {success && (
          <p className="success-message">Login effettuato con successo!</p>
        )}
        {error && <p className="error-message">Errore: {error}</p>}
      </form>

      <p className="accountRegister">
        Non hai ancora un account?{" "}
        <Link to="/register" className="register-link">
          Registrati ora!
        </Link>{" "}
        🐾
      </p>
    </>
  );
};

export default Login;
