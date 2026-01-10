import React, { useState } from "react";
import "../css/PaperNav.css";
import queen from "../assets/img/queen.png";
import { Link } from "react-router-dom";

const PaperNav = () => {
  const [menuOpen, setMenuOpen] = useState(false);

  // Close menu on navigation (for mobile)
  const handleNavClick = () => setMenuOpen(false);

  return (
    <nav
      className="border fixed split-nav"
      role="navigation"
      aria-label="Main Navigation"
    >
      <div className="nav-brand">
        <Link to="/" className="welcome-link" onClick={handleNavClick}>
          <h3>Corgi Connection 🐾</h3>
        </Link>
      </div>
      <div className="collapsible">
        <button
          className="hamburger"
          aria-label={menuOpen ? "Chiudi menu" : "Apri menu"}
          aria-expanded={menuOpen}
          aria-controls="main-menu"
          onClick={() => setMenuOpen((open) => !open)}
        >
          <div className="bar1"></div>
          <div className="bar2"></div>
          <div className="bar3"></div>
        </button>
        <div
          className={`collapsible-body${menuOpen ? " open" : ""}`}
          id="main-menu"
        >
          <ul className="inline">
            <li>
              <Link
                to="/home"
                className="login-link-nav"
                onClick={handleNavClick}
              >
                Home
              </Link>
            </li>
            <li>
              <Link
                to="/curiosita"
                className="curiosita-link-nav"
                onClick={handleNavClick}
              >
                Lo sapevi?
              </Link>
            </li>
            <li>
              <Link
                to="/cosafacciamo"
                className="cosfacciamo-link-nav"
                onClick={handleNavClick}
              >
                Cosa facciamo?
              </Link>
            </li>
            <li>
              <Link
                to="/login"
                className="login-link-nav"
                onClick={handleNavClick}
              >
                Log in
              </Link>
            </li>
            <li>
              <Link
                to="/profilo"
                className="profile-link-nav"
                onClick={handleNavClick}
              >
                <img className="profilo" src={queen} alt="queen" />
              </Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default PaperNav;
