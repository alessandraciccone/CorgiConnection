import React, { useState } from "react";
import { Routes, Route } from "react-router-dom";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import PaperNav from "./components/PaperNav";
import Welcome from "./components/Welcome";
import Footer from "./components/Footer";
import Register from "./components/Register";
import Login from "./components/Login";
import Profilo from "./components/Profilo";
import Home from "./components/Home";
import SearchBar from "./components/SearchBar";
import ProfilePage from "./components/ProfilePage";

const Admin = () => <h2>Admin Panel</h2>;

const App = () => {
  const [filters, setFilters] = useState({});

  return (
    <>
      <PaperNav />
      <SearchBar setFilters={setFilters} />
      <div className="container py-4">
        <Routes>
          <Route path="/" element={<Welcome />} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          <Route path="/home" element={<Home filters={filters} />} />{" "}
          <Route path="/profile/:userId" element={<ProfilePage />} />
          <Route path="/profilo" element={<Profilo />} />
          {/* <Route path="/admin" element={<Admin />} /> */}
        </Routes>
        <Footer />
      </div>
    </>
  );
};

export default App;
