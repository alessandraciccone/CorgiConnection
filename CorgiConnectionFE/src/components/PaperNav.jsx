import "../css/PaperNav.css";
import CorgiIcon from "../assets/img/corgi-icon.png";
import { Link } from "react-router-dom";

const PaperNav = () => {
  return (
    <nav className="border fixed split-nav">
      <div className="nav-brand">
        <Link to="/" className="welcome-link">
          <h3>Corgi Connection 🐾</h3>
        </Link>
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
              <Link to="/login" className="login-link-nav">
                Log in
              </Link>
            </li>
            <li>
              <Link to="/profilo" className="profile-link-nac">
                <img className="profilo" src={CorgiIcon} alt="Corgi Icon" />
              </Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default PaperNav;
