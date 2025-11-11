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
        Ciao! Benvenuti su Corgi Connection, il posto dove i Corgi si
        incontrano!
      </div>

      {/* Fumetto Gaspare */}
      <div className="speech-bubble dog">
        Io mi chiamo Gaspare, e lei è la mia umana Alessandra! Registrati e
        vieni a conoscere nuovi amici insieme a noi ❤️
      </div>
    </div>
  );
};

export default Welcome;
