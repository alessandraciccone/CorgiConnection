import React, { useState } from "react";
import "../css/PaperNav.css";
import queen from "../assets/img/queen.png";
import { Link } from "react-router-dom";

const PaperNav = () => {
  const [menuOpen, setMenuOpen] = useState(false); //stato per menu hamburger serve per mobile

  // usestate in react serve per gestire la memoria interna di un componente funzionale
  const handleNavClick = () => setMenuOpen(false); //chiude il menu quando si clicca su un link

  return (
    <nav className="navbar navbar-expand-lg custom-nav sticky-top">
      <div className="container-fluid">
        {/* Brand */}
        <Link
          to="/"
          className="navbar-brand welcome-link"
          onClick={handleNavClick}
        >
          <h3 className="brand-title">Corgi Connection 🐾</h3>
        </Link>

        {/* Hamburger Button */}
        <button
          className="navbar-toggler" //classe per bottone hamburger
          type="button" //tipo bottone
          onClick={() => setMenuOpen(!menuOpen)} //toggle menu aperto/chiuso alla pressione
          aria-controls="navbarNav" //
          aria-expanded={menuOpen} //aggiorna attributo aria-expanded serve per accessibilità
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        {/* Menu Items */}
        <div
          className={`collapse navbar-collapse ${menuOpen ? "show" : ""}`} // sse menuOpen è true aggiunge classe show per mostrare il menu
          id="navbarNav"
        >
          <ul className="navbar-nav ms-auto ">
            <li className="nav-item">
              <Link to="/home" className="nav-link" onClick={handleNavClick}>
                Home
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/curiosita"
                className="nav-link"
                onClick={handleNavClick} //chiude menu al click quando seleziono una voce
              >
                Lo sapevi?
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/cosafacciamo"
                className="nav-link"
                onClick={handleNavClick}
              >
                Cosa facciamo?
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/login" className="nav-link" onClick={handleNavClick}>
                Log in
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/profilo"
                className="nav-link profile-link"
                onClick={handleNavClick}
              >
                <img className="profilo-img" src={queen} alt="Profilo" />
              </Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default PaperNav;
