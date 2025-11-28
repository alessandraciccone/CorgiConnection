import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import correrecorgi from "../assets/img/correrecorgi.png";
import "../css/Profilo.css";
import ChatPopup from "./ChatPopup";

const ProfilePage = () => {
  const { userId } = useParams();
  const [user, setUser] = useState(null);
  const [fotoProfilo, setFotoProfilo] = useState(null);
  const [infoCane, setInfoCane] = useState("");
  const token = localStorage.getItem("token");

  // 🔥 FIX: convertiamo entrambi a stringa
  const loggedUserId = String(localStorage.getItem("userId"));
  const visitingId = String(userId);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const baseUrl = import.meta.env.VITE_API_URL;
        const res = await fetch(`${baseUrl}/users/${userId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (res.ok) {
          const data = await res.json();
          setUser(data);

          const fotoSalvata = localStorage.getItem(`fotoProfilo-${userId}`);
          setFotoProfilo(fotoSalvata || data.profileImage || null);

          const infoSalvata = localStorage.getItem(`infoCane-${userId}`);
          setInfoCane(infoSalvata || "");
        } else {
          console.error("Errore nel recupero del profilo:", res.status);
        }
      } catch (err) {
        console.error("Errore nella fetch:", err);
      }
    };

    fetchUser();
  }, [userId]);

  if (!user) return <p>Caricamento profilo...</p>;

  return (
    <div className="container">
      <div className="row">
        {loggedUserId !== visitingId && <ChatPopup recipient={userId} />}

        <div className="col-12 col-lg-6 mb-4">
          <div className="card mt-2" style={{ border: "1px solid #ff7f50" }}>
            <div className="card-body card-flex">
              <div className="dati-utente">
                <h4 className="card-title">
                  {user.firstName} {user.lastName}
                </h4>
                <p className="card-text">Username: {user.username}</p>
                <p className="card-text">Email: {user.email}</p>
                <p className="card-text">Città: {user.city}</p>
                <p className="card-text">Provincia: {user.province}</p>

                {infoCane && (
                  <div className="card mt-2">
                    <div className="card-body canecard">
                      <p className="info-cane">{infoCane}</p>
                    </div>
                  </div>
                )}
              </div>

              <div className="foto-col">
                {fotoProfilo ? (
                  <img
                    src={fotoProfilo}
                    className="foto"
                    alt="Foto profilo"
                    style={{ border: "1px solid #ff7f50" }}
                  />
                ) : (
                  <img
                    src={correrecorgi}
                    className="foto"
                    alt="Corgi di default"
                    style={{ border: "1px solid #ff7f50" }}
                  />
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
