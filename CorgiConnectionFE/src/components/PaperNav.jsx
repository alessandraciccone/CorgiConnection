import React from "react";
import "../css/PaperNav.css";
import CorgiIcon from "../assets/img/corgi-icon.png";
const PaperNav = () => {
  return (
    <nav className="border fixed split-nav">
      <div className="nav-brand">
        <h3>Corgi Connection 🐾</h3>
      </div>
      <div className="collapsible">
        <input id="collapsible1" type="checkbox" name="collapsible1" />
        <label htmlFor="collapsible1">
          <div className="bar1"></div>
          <div className="bar2"></div>
          <div className="bar3"></div>
        </label>
        <div className="collapsible-body">
          <ul className="inline">
            <li>
              <a href="#">Home</a>
            </li>
            <li>
              <a href="#">Lo sapevi?</a>
            </li>
            <li>
              <a href="#">Cosa facciamo oggi?</a>
            </li>

            <li>
              <a href="#">Log in</a>
            </li>

            <li>
              <a href="#">FAQ </a>
            </li>

            <li>
              <a href="#">
                <img src={CorgiIcon} alt="Corgi Icon" width="35" height="24" />
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default PaperNav;
