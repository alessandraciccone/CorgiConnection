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
              <Link to="/home" className="login-link-nav">
                Home
              </Link>
            </li>
            <li>
              <Link to="/curiosita" className="curiosita-link-nav">
                Lo sapevi?
              </Link>
            </li>
            <li>
              <Link to="/cosafacciamo" className="cosfacciamo-link-nav">
                Cosa facciamo?
              </Link>
            </li>
            <li>
              <Link to="/login" className="login-link-nav">
                Log in
              </Link>
            </li>
            <li>
              <Link to="/profilo" className="profile-link-nav">
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
