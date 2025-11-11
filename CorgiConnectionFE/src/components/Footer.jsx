import React from "react";
import "../css/Footer.css";
import Logo from "../assets/img/logo.png";

const Footer = () => {
  const year = new Date().getFullYear();

  return (
    <footer className="corgi-footer">
      <div className="footer-content">
        <div className="footer-logo">
          <h3>Corgi Connection 🐾 Dove i Corgi si incontrano</h3>
        </div>

        <div className="footer-links">
          <a href="/about">Chi siamo</a>
          <a href="/adopt">Adotta un corgi</a>
          <a href="/gallery">Galleria</a>
          <a href="/contact">Contatti</a>
        </div>

        <div className="footer-social">
          <a
            href="https://instagram.com/corgiconnection"
            target="_blank"
            rel="noopener noreferrer"
          >
            Instagram
          </a>
          <a
            href="https://facebook.com/corgiconnection"
            target="_blank"
            rel="noopener noreferrer"
          >
            Facebook
          </a>
        </div>
      </div>
      <div className="footer-logo">
        <img src={Logo} alt="Logo Corgi Connection" />
      </div>
      <p className="footer-copy">
        © {year} Corgi Connection. Tutti i diritti riservati.
      </p>
    </footer>
  );
};

export default Footer;
