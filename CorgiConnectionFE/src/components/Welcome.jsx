import React, { useEffect, useState } from "react";
import cartooncorgi from "../assets/img/cartooncorgi.png";
import "../css/Welcome.css";

const Welcome = () => {
  const [step, setStep] = useState(1);

  useEffect(() => {
    const timer = setTimeout(() => {
      setStep(2);
    }, 5000); // dopo 5 secondi

    return () => clearTimeout(timer);
  }, []);

  return (
    <div className="container-fluid welcome-wrapper">
      <div className="row justify-content-center">
        <div className="col-12 col-md-8 text-center position-relative comic-container">
          <img
            className="cartoon-corgi"
            src={cartooncorgi}
            alt="Cartoon girl with corgi"
          />

          {step === 1 && (
            <>
              <div className="speech-bubble fade-in">
                Ciao! Benvenuti su Corgi Connection!
              </div>
              <div className="speech-bubble dog fade-in">
                Io mi chiamo Gaspare, e lei è la mia umana Alessandra!
              </div>
            </>
          )}

          {step === 2 && (
            <>
              <div className="speech-bubble fade-in">
                Qui potrai conoscere nuovi amici e scoprire curiosità sui corgi!
              </div>
              <div className="speech-bubble dog fade-in">
                Registrati e preparati a scodinzolare di gioia!
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default Welcome;
