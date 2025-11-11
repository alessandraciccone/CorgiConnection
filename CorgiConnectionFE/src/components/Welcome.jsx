import cartooncorgi from "../assets/img/cartooncorgi.png";
import "../css/Welcome.css";

const Welcome = () => {
  return (
    <div className="container-fluid">
      {/* Immagine unica */}
      <img
        className="cartoon-corgi"
        src={cartooncorgi}
        alt="Cartoon girl with corgi"
      />

      {/* Fumetto Alessandra */}
      <div className="speech-bubble main">
        <span>
          {" "}
          Ciao! Benvenuti su Corgi Connection, il posto dove i Corgi si
          incontrano!
        </span>
      </div>

      {/* Fumetto Gaspare */}
      <div className="speech-bubble dog">
        <span>
          Io mi chiamo Gaspare, e lei è la mia umana Alessandra! Registrati e
          vieni a conoscere nuovi amici insieme a noi ❤️
        </span>
      </div>
    </div>
  );
};

export default Welcome;
