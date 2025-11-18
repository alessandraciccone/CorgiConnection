import { useState, useRef, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import "../css/Profilo.css";
import correrecorgi from "../assets/img/correrecorgi.png";

// --- FUNZIONI INDEXEDDB ---
const openDB = () => {
  return new Promise((resolve, reject) => {
    const request = indexedDB.open("UserDB", 1);
    request.onupgradeneeded = (event) => {
      const db = event.target.result;
      if (!db.objectStoreNames.contains("profileImages")) {
        db.createObjectStore("profileImages", { keyPath: "userId" });
      }
    };
    request.onsuccess = () => resolve(request.result);
    request.onerror = () => reject(request.error);
  });
};

const saveProfileImage = async (userId, blob) => {
  const db = await openDB();
  const tx = db.transaction("profileImages", "readwrite");
  const store = tx.objectStore("profileImages");
  store.put({ userId, image: blob });
  return tx.complete;
};

const getProfileImage = async (userId) => {
  const db = await openDB();
  return new Promise((resolve) => {
    const tx = db.transaction("profileImages", "readonly");
    const store = tx.objectStore("profileImages");
    const request = store.get(userId);
    request.onsuccess = () => resolve(request.result?.image || null);
  });
};

// --- COMPONENTE PROFILO ---
const Profilo = () => {
  const navigate = useNavigate();
  const { userId: profileUserId } = useParams(); // ✅ ID dal URL

  const [utente, setUtente] = useState(null);
  const [profiloUtente, setProfiloUtente] = useState({
    username: "",
    email: "",
    firstName: "",
    lastName: "",
    city: "",
    province: "",
  });
  const [fotoProfilo, setFotoProfilo] = useState(null);
  const [infoCane, setInfoCane] = useState("");
  const [modificaUtente, setModificaUtente] = useState(false);

  const [messaggi, setMessaggi] = useState([]);
  const [nuoviMessaggi, setNuoviMessaggi] = useState(0);
  const [risposta, setRisposta] = useState("");
  const [messaggioSelezionato, setMessaggioSelezionato] = useState(null);

  // --- STATI CHAT WEBSOCKET ---
  const [chatMessages, setChatMessages] = useState([]);
  const [chatInput, setChatInput] = useState("");
  const [connected, setConnected] = useState(false);
  const [showChat, setShowChat] = useState(false);
  const clientRef = useRef(null);
  const chatEndRef = useRef(null);

  const fileInputProfiloRef = useRef(null);

  // ✅ Determina se è il proprio profilo
  const myUserId = localStorage.getItem("userId");
  const isMyProfile = !profileUserId || profileUserId === myUserId;
  const targetUserId = profileUserId || myUserId;

  // --- FETCH DATI UTENTE E MESSAGGI ---
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token || !targetUserId) return;

    const fetchUtente = async () => {
      try {
        const response = await fetch(
          `http://localhost:8888/users/${targetUserId}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        if (!response.ok) throw new Error("Errore nel recupero utente");

        const data = await response.json();
        setUtente(data);
        setProfiloUtente(data);

        const blob = await getProfileImage(targetUserId);
        if (blob) setFotoProfilo(URL.createObjectURL(blob));

        const infoSalvata = localStorage.getItem(`infoCane-${targetUserId}`);
        setInfoCane(infoSalvata || "");
      } catch (error) {
        console.error(error);
      }
    };

    const fetchMessaggi = async () => {
      if (!isMyProfile) return; // ✅ Solo per il proprio profilo

      try {
        const response = await fetch(
          `http://localhost:8888/messages/${myUserId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        if (response.ok) {
          const data = await response.json();
          setMessaggi(data);
          setNuoviMessaggi(data.filter((msg) => !msg.read).length);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchUtente();
    fetchMessaggi();
  }, [targetUserId, isMyProfile, myUserId]);

  // --- WEBSOCKET CONNECTION (SOLO PER PROFILI ALTRUI) ---
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token || isMyProfile) return; // ✅ Non connettere se è il proprio profilo

    const socket = new SockJS("http://localhost:8888/ws");
    const stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      onConnect: () => {
        console.log("✅ Connesso a WebSocket");
        setConnected(true);

        stompClient.subscribe(
          `/user/${targetUserId}/queue/messages`,
          (message) => {
            const msg = JSON.parse(message.body);
            setChatMessages((prev) => [...prev, msg]);

            setTimeout(() => {
              chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
            }, 100);
          }
        );
      },
      onStompError: (error) => {
        console.error("❌ Errore STOMP:", error);
        setConnected(false);
      },
      onDisconnect: () => {
        console.log("🔴 Disconnesso da WebSocket");
        setConnected(false);
      },
    });

    stompClient.activate();
    clientRef.current = stompClient;

    return () => {
      stompClient.deactivate();
    };
  }, [isMyProfile, targetUserId]);

  // --- INVIO MESSAGGIO CHAT ---
  const sendChatMessage = () => {
    if (chatInput.trim() && connected && clientRef.current) {
      clientRef.current.publish({
        destination: "/app/chat.private",
        body: JSON.stringify({
          recipient: targetUserId,
          content: chatInput,
          type: "CHAT",
        }),
      });
      setChatInput("");
    }
  };

  // --- FUNZIONI FOTO PROFILO ---
  const handleFotoProfilo = (e) => {
    const file = e.target.files[0];
    const userId = localStorage.getItem("userId");
    if (!file || !userId) return;

    const reader = new FileReader();
    reader.onload = (event) => {
      const img = new Image();
      img.onload = () => {
        const canvas = document.createElement("canvas");
        const maxSize = 500;
        let { width, height } = img;

        if (width > height) {
          if (width > maxSize) {
            height *= maxSize / width;
            width = maxSize;
          }
        } else {
          if (height > maxSize) {
            width *= maxSize / height;
            height = maxSize;
          }
        }

        canvas.width = width;
        canvas.height = height;
        canvas.getContext("2d").drawImage(img, 0, 0, width, height);

        canvas.toBlob(async (blob) => {
          if (blob.size < 5 * 1024 * 1024) {
            await saveProfileImage(userId, blob);
            setFotoProfilo(URL.createObjectURL(blob));
          } else {
            alert("Immagine troppo grande, prova un file più leggero!");
          }
        }, "image/png");
      };
      img.src = event.target.result;
    };
    reader.readAsDataURL(file);
  };

  // --- CAMBIO DATI UTENTE ---
  const handleChangeUtente = (e) => {
    const { name, value } = e.target;
    setProfiloUtente((prev) => ({ ...prev, [name]: value }));
  };

  const handleChangeInfoCane = (e) => {
    const value = e.target.value;
    const userId = localStorage.getItem("userId");
    setInfoCane(value);
    localStorage.setItem(`infoCane-${userId}`, value);
  };

  const handleInviaUtente = async () => {
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");
    if (!token || !userId) return;

    try {
      const response = await fetch(`http://localhost:8888/users/${userId}`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(profiloUtente),
      });
      if (!response.ok) throw new Error("Errore nell'aggiornamento profilo");
    } catch (error) {
      console.error(error);
    }
  };

  // --- LOGOUT ---
  const handleLogout = () => {
    if (clientRef.current) {
      clientRef.current.deactivate();
    }
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    setUtente(null);
    setFotoProfilo(null);
    setInfoCane("");
    navigate("/login");
  };

  // --- SEGNA COME LETTO ---
  const segnaComeLetto = async (messageId) => {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
      const response = await fetch(
        `http://localhost:8888/messages/${messageId}/read`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.ok) {
        setMessaggi((prev) =>
          prev.map((msg) =>
            msg.id === messageId ? { ...msg, read: true } : msg
          )
        );
        setNuoviMessaggi(nuoviMessaggi - 1);
      }
    } catch (error) {
      console.error(error);
    }
  };

  // --- INVIO RISPOSTA ---
  const inviaRisposta = async (messageId, risposta) => {
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");
    if (!token || !userId || !risposta.trim()) return;

    try {
      const response = await fetch(
        `http://localhost:8888/messages/${messageId}/reply`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({ userId, content: risposta }),
        }
      );

      if (response.ok) {
        setMessaggi((prev) =>
          prev.map((msg) =>
            msg.id === messageId
              ? {
                  ...msg,
                  replies: [
                    ...msg.replies,
                    { content: risposta, sender: userId },
                  ],
                }
              : msg
          )
        );
        setRisposta("");
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="container">
      <div className="goCorgi">
        <img
          className="CCorgi"
          src={correrecorgi}
          alt="Corgi e ragazza che corrono"
        />
        <p className="textCorgi">
          Scrivi qualcosa su di te e sul tuo cagnolino… <br />e poi pubblica un
          post per conoscere nuovi amici a quattro zampe!
        </p>
      </div>

      {/* ✅ Mostra messaggi SOLO nel proprio profilo */}
      {isMyProfile && (
        <>
          {nuoviMessaggi > 0 && (
            <div className="notifica-messaggio">
              <p>Hai {nuoviMessaggi} nuovi messaggi! 📩</p>
            </div>
          )}

          <div className="messaggi">
            {messaggi.map((msg) => (
              <div key={msg.id} className="messaggio">
                <p>
                  <strong>{msg.senderName}</strong> ha inviato un messaggio:
                </p>
                <p>{msg.content}</p>
                {!msg.read && (
                  <button onClick={() => segnaComeLetto(msg.id)}>
                    Segna come letto
                  </button>
                )}
                <button onClick={() => setMessaggioSelezionato(msg)}>
                  Leggi / Rispondi
                </button>

                <div className="risposte">
                  {msg.replies?.map((reply, index) => (
                    <div key={index} className="risposta">
                      <p>
                        <strong>Risposta:</strong> {reply.content}
                      </p>
                    </div>
                  ))}

                  {messaggioSelezionato &&
                    messaggioSelezionato.id === msg.id && (
                      <div>
                        <textarea
                          value={risposta}
                          onChange={(e) => setRisposta(e.target.value)}
                          placeholder="Scrivi una risposta..."
                        />
                        <button onClick={() => inviaRisposta(msg.id, risposta)}>
                          Invia Risposta
                        </button>
                      </div>
                    )}
                </div>
              </div>
            ))}
          </div>
        </>
      )}

      <div className="row">
        <div className="col-12 col-lg-6 mb-4">
          <div className="card mt-2">
            <div className="card-body card-flex">
              <div className="dati-utente">
                {/* ✅ Modifica SOLO nel proprio profilo */}
                {isMyProfile && modificaUtente ? (
                  <>
                    {[
                      "username",
                      "email",
                      "firstName",
                      "lastName",
                      "city",
                      "province",
                    ].map((field) => (
                      <input
                        key={field}
                        type={field === "email" ? "email" : "text"}
                        name={field}
                        placeholder={
                          field.charAt(0).toUpperCase() + field.slice(1)
                        }
                        value={profiloUtente[field]}
                        onChange={handleChangeUtente}
                        className="input-block"
                      />
                    ))}
                    <div className="card mt-2">
                      <div className="card-body canecard">
                        <textarea
                          id="infoCane"
                          value={infoCane}
                          onChange={handleChangeInfoCane}
                          className="input-block cane-textarea"
                          rows={4}
                        />
                      </div>
                    </div>
                    <span
                      className="emoji-click"
                      onClick={() => {
                        handleInviaUtente();
                        setModificaUtente(false);
                      }}
                    >
                      ✔️
                    </span>
                  </>
                ) : utente ? (
                  <>
                    <h4 className="card-title">
                      {utente.firstName} {utente.lastName}
                    </h4>
                    <p className="card-text">Username: {utente.username}</p>
                    <p className="card-text">Email: {utente.email}</p>
                    <p className="card-text">Città: {utente.city}</p>
                    <p className="card-text">Provincia: {utente.province}</p>
                    {infoCane && (
                      <div className="card mt-2">
                        <div className="card-body canecard">
                          <p className="info-cane">{infoCane}</p>
                        </div>
                      </div>
                    )}
                    {/* ✅ Pulsante modifica SOLO nel proprio profilo */}
                    {isMyProfile && (
                      <div className="bottoni-profilo">
                        <span
                          className="emoji-click"
                          onClick={() => setModificaUtente(true)}
                        >
                          🖊️
                        </span>
                      </div>
                    )}
                  </>
                ) : (
                  <p>Caricamento dati utente...</p>
                )}
              </div>

              <div className="foto-col">
                {fotoProfilo && (
                  <img src={fotoProfilo} className="foto" alt="Foto profilo" />
                )}
                {/* ✅ Carica foto SOLO nel proprio profilo */}
                {isMyProfile && (
                  <>
                    <input
                      type="file"
                      accept="image/*"
                      ref={fileInputProfiloRef}
                      onChange={handleFotoProfilo}
                      style={{ display: "none" }}
                    />
                    <button
                      className="btn-small-profilo mt-2"
                      onClick={() => fileInputProfiloRef.current.click()}
                    >
                      <p className="prof">Carica foto profilo 📷</p>
                    </button>
                  </>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* ✅ CHAT - SOLO NEI PROFILI ALTRUI */}
      {!isMyProfile && (
        <>
          {!isMyProfile && (
            <>
              <button
                onClick={() => setShowChat(!showChat)}
                style={{
                  position: "fixed",
                  bottom: "20px",
                  right: "20px",
                  width: "60px",
                  height: "60px",
                  borderRadius: "50%",
                  background: "#d17b49",
                  color: "white",
                  border: "none",
                  fontSize: "24px",
                  cursor: "pointer",
                  boxShadow: "0 4px 8px rgba(0,0,0,0.2)",
                  zIndex: 1000,
                }}
              >
                💬
              </button>
            </>
          )}

          {showChat && (
            <div
              style={{
                position: "fixed",
                bottom: "90px",
                right: "20px",
                width: "350px",
                height: "500px",
                background: "white",
                borderRadius: "10px",
                boxShadow: "0 4px 16px rgba(0,0,0,0.2)",
                zIndex: 1000,
                display: "flex",
                flexDirection: "column",
              }}
            >
              <div
                style={{
                  background: "#d17b49",
                  color: "white",
                  padding: "15px",
                  borderRadius: "10px 10px 0 0",
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <div>
                  <strong>Chat con {utente?.username} 🐶</strong>
                  <div style={{ fontSize: "12px" }}>
                    {connected ? "🟢 Online" : "🔴 Offline"}
                  </div>
                </div>
                <button
                  onClick={() => setShowChat(false)}
                  style={{
                    background: "none",
                    border: "none",
                    color: "white",
                    fontSize: "20px",
                    cursor: "pointer",
                  }}
                >
                  ✕
                </button>
              </div>
              console.log(isMyProfile);
              <div
                style={{
                  flex: 1,
                  overflowY: "auto",
                  padding: "15px",
                  background: "#f9f9f9",
                }}
              >
                {chatMessages.length === 0 ? (
                  <p style={{ textAlign: "center", color: "#999" }}>
                    Inizia una conversazione...
                  </p>
                ) : (
                  chatMessages.map((msg, idx) => (
                    <div
                      key={idx}
                      style={{
                        marginBottom: "10px",
                        padding: "8px 12px",
                        background:
                          msg.sender === myUserId ? "#d17b49" : "white",
                        color: msg.sender === myUserId ? "white" : "black",
                        borderRadius: "8px",
                        boxShadow: "0 1px 3px rgba(0,0,0,0.1)",
                      }}
                    >
                      <p style={{ margin: "0", fontSize: "14px" }}>
                        {msg.content}
                      </p>
                    </div>
                  ))
                )}
                <div ref={chatEndRef} />
              </div>
              <div
                style={{
                  padding: "15px",
                  borderTop: "1px solid #ddd",
                  background: "white",
                  borderRadius: "0 0 10px 10px",
                }}
              >
                <div style={{ display: "flex", gap: "10px" }}>
                  <input
                    type="text"
                    value={chatInput}
                    onChange={(e) => setChatInput(e.target.value)}
                    onKeyPress={(e) => e.key === "Enter" && sendChatMessage()}
                    placeholder="Scrivi un messaggio..."
                    disabled={!connected}
                    style={{
                      flex: 1,
                      padding: "10px",
                      border: "1px solid #ddd",
                      borderRadius: "5px",
                      fontSize: "14px",
                    }}
                  />
                  <button
                    onClick={sendChatMessage}
                    disabled={!connected || !chatInput.trim()}
                    style={{
                      padding: "10px 15px",
                      background: connected ? "#d17b49" : "#ccc",
                      color: "white",
                      border: "none",
                      borderRadius: "5px",
                      cursor: connected ? "pointer" : "not-allowed",
                      fontSize: "18px",
                    }}
                  >
                    ➤
                  </button>
                </div>
              </div>
            </div>
          )}
        </>
      )}

      {/* ✅ Logout SOLO nel proprio profilo */}
      {isMyProfile && (
        <div className="xlogout">
          <p>
            Vuoi uscire dal tuo profilo?{" "}
            <span className="logout-link" onClick={handleLogout}>
              clicca qui
            </span>{" "}
            e torna presto a trovarci! 🐶
          </p>
        </div>
      )}
    </div>
  );
};

export default Profilo;
