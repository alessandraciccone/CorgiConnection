import { useState } from "react";
import Chat from "./Chat";

const ChatPopup = ({ recipient }) => {
  const [open, setOpen] = useState(false);

  return (
    <>
      {/* PULSANTE FISSO IN BASSO A DESTRA */}
      <button
        onClick={() => setOpen(!open)}
        style={{
          position: "fixed",
          bottom: "25px",
          right: "25px",
          width: "60px",
          height: "60px",
          borderRadius: "50%",
          background: "#d17b49",
          color: "white",
          border: "none",
          boxShadow: "0px 4px 10px rgba(0,0,0,0.2)",
          fontSize: "26px",
          cursor: "pointer",
          zIndex: 9999,
        }}
      >
        💬
      </button>

      {/* CHAT POPUP */}
      {open && (
        <div
          style={{
            position: "fixed",
            bottom: "100px",
            right: "25px",
            width: "350px",
            height: "500px",
            background: "white",
            borderRadius: "10px",
            boxShadow: "0 8px 25px rgba(0,0,0,0.25)",
            zIndex: 9998,
            overflow: "hidden",
            display: "flex",
            flexDirection: "column",
          }}
        >
          {/* HEADER */}
          <div
            style={{
              background: "#d17b49",
              color: "white",
              padding: "10px",
              fontWeight: "bold",
              textAlign: "center",
            }}
          >
            Chat
          </div>

          {/* CONTENUTO CHAT */}
          <div style={{ flex: 1, overflow: "auto" }}>
            <Chat recipient={recipient} />
          </div>

          {/* TASTO CHIUSURA */}
          <div style={{ padding: "10px", textAlign: "center" }}>
            <button
              onClick={() => setOpen(false)}
              className="btn btn-sm btn-secondary"
            >
              Chiudi
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default ChatPopup;
