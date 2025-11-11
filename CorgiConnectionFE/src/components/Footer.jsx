import React from "react";
import "./Footer.css";

const Footer = () => {
  return (
    <footer className="corgi-footer">
      <div className="footer-content">
        <div className="footer-logo">
          <img src="/assets/corgi-logo.png" alt="Corgi Connection Logo" />
          <h3>Corgi Connection</h3>
        </div>

        <nav className="footer-links">
          <a href="/about">Chi siamo</a>
          <a href="/adopt">Adotta un corgi</a>
          <a href="/gallery">Galleria</a>
          <a href="/contact">Contatti</a>
        </nav>

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

        <p className="footer-copy">
          © 2025 Corgi Connection. Tutti i diritti riservati.
        </p>
      </div>
    </footer>
  );
};

export default Footer;
