import React from "react";
import { Routes, Route } from "react-router-dom";
import "./App.css";
import PaperNav from "./components/PaperNav";
import Welcome from "./components/Welcome";
import Footer from "./components/Footer";
const Home = () => <h2>Home Page</h2>;
const Profile = () => <h2>Profilo Utente</h2>;
const Admin = () => <h2>Admin Panel</h2>;

const App = () => {
  return (
    <>
      <PaperNav />
      <Footer />
      <div className="container py-4">
        <Routes>
          <Route path="/" element={<Welcome />} />{" "}
          <Route path="/home" element={<Home />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/admin" element={<Admin />} />
        </Routes>
      </div>
    </>
  );
};

export default App;
