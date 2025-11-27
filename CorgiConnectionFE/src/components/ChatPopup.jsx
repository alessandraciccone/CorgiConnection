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
          border: "solid 1px #ff9900",
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
            border: "1px solid #ff9900",

            bottom: "100px",
            right: "25px",
            width: "350px",
            height: "500px",
            background: "white",
            borderRadius: "10px",
            zIndex: 9998,
            overflow: "hidden",
            display: "flex",
            flexDirection: "column",
          }}
        >
          {/* CONTENUTO CHAT */}
          <div style={{ flex: 1, overflow: "auto" }}>
            <Chat recipient={recipient} />
          </div>

          {/* TASTO CHIUSURA */}
          <div
            className="chat-popup-content"
            style={{ padding: "5px", textAlign: "center" }}
          >
            <button
              onClick={() => setOpen(false)}
              className="btn btn-sm"
              style={{ border: "1px solid #ff9900" }}
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
