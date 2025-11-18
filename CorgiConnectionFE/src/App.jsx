import React, { useState } from "react";
import { Routes, Route, useLocation } from "react-router-dom";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import PaperNav from "./components/PaperNav";
import SearchBar from "./components/SearchBar";
import Footer from "./components/Footer";
import Welcome from "./components/Welcome";
import Register from "./components/Register";
import Login from "./components/Login";
import Profilo from "./components/Profilo";
import Home from "./components/Home";
import Homepage from "./components/HomePage";
import ProfilePage from "./components/ProfilePage";
import Curiosita from "./components/Curiosita";
import Cosafacciamo from "./components/Cosafacciamo";

const App = () => {
  const [filters, setFilters] = useState(null);
  const location = useLocation(); // prende il percorso corrente

  const showSearchBar = location.pathname !== "/"; // false se siamo sulla welcome

  return (
    <>
      <PaperNav />
      {showSearchBar && <SearchBar setFilters={setFilters} />}

      <div className="container py-4">
        <Routes>
          <Route path="/" element={<Welcome />} />
          <Route path="/home" element={<Home />} />
          <Route path="/homepage" element={<Homepage filters={filters} />} />
          <Route path="/curiosita" element={<Curiosita />} />
          <Route path="/cosafacciamo" element={<Cosafacciamo />} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          {/* PROFILO ROUTES */}
          <Route path="/profilo" element={<Profilo />} />
          <Route path="/profilo/:userId" element={<Profilo />} />
          <Route path="/profile/:userId" element={<ProfilePage />} />
        </Routes>

        <Footer />
      </div>
    </>
  );
};

export default App;
