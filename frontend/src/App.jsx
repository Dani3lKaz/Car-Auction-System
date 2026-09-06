import * as React from 'react';
import Bar from './components/Bar.jsx';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/Home.jsx';
import Login from './pages/Login.jsx';
import Register from './pages/Register.jsx';

function App() {
  return (
      <>
          <BrowserRouter>
              <Bar/>
              <Routes>
                  <Route path="/" element={<Home/>}/>
                  <Route path="/login" element={<Login/>}/>
                  <Route path="/register" element={<Register/>}/>
              </Routes>
          </BrowserRouter>
      </>
  );
}

export default App
