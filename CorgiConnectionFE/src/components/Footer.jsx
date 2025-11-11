import React from "react";
import "../css/Footer.css";
import Logo from "../assets/img/logo.png";
import facebook from "../assets/img/facebook.png";
import istagram from "../assets/img/istagram.png";
import phone from "../assets/img/phone.png";
import chisiamo from "../assets/img/chisiamo.png";

const Footer = () => {
  const year = new Date().getFullYear();

  return (
    <footer className="corgi-footer">
      <div className="footer-content">
        <div className="footer-logo">
          <h3>Corgi Connection 🐾 Dove i Corgi si incontrano</h3>
        </div>

        <div className="footer-links">
          <div className="about">
            <img src={chisiamo} className="chisiamoicon" alt=" chisiamo icon" />

            <a href="/about">Chi siamo</a>
          </div>
          <a href="/adopt">Adotta un corgi</a>
          <a href="/gallery">Galleria</a>
          <div className="contattiLink">
            <img src={phone} className="phoneIcon" alt=" phone icon" />
            <a href="/contact">Contatti</a>
          </div>
        </div>

        <div className="footer-social">
          <div className="istagramWrapper">
            <img src={istagram} className="istagramIcon" alt="istagram icon" />
            <a
              href="https://istagram.com/corgiconnection"
              target="_blank"
              rel="noopener noreferrer"
            >
              Istagram
            </a>
          </div>
          <div className="facebookWrapper">
            <img src={facebook} className="facebookIcon" alt="facebook icon" />
            <a
              href="https://facebook.com/corgiconnection"
              target="_blank"
              rel="noopener noreferrer"
            >
              Facebook
            </a>
          </div>
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
