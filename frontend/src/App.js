import React, { useState } from 'react';
import { Container, Nav, Navbar, Tab, Tabs } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import AuthorsComponent from './components/AuthorsComponent';
import PublicationsComponent from './components/PublicationsComponent';
import './App.css';

function App() {
  const [key, setKey] = useState('authors');

  return (
    <div className="App">
      <Navbar bg="dark" variant="dark" expand="lg" className="mb-4">
        <Container>
          <Navbar.Brand href="#home" className="fw-bold">
            📚 Editorial Platform
          </Navbar.Brand>
          <Navbar.Text className="text-white">
            Gestión de Autores y Publicaciones
          </Navbar.Text>
        </Container>
      </Navbar>

      <Container className="py-4">
        <Tabs
          id="main-tabs"
          activeKey={key}
          onSelect={(k) => setKey(k)}
          className="mb-4"
        >
          <Tab eventKey="authors" title="👨‍✍️ Autores">
            <AuthorsComponent />
          </Tab>
          <Tab eventKey="publications" title="📖 Publicaciones">
            <PublicationsComponent />
          </Tab>
          <Tab eventKey="info" title="ℹ️ Información">
            <InfoComponent />
          </Tab>
        </Tabs>
      </Container>

      <footer className="bg-dark text-white text-center py-4 mt-5">
        <p>© 2024 Editorial Microservices Platform - All Rights Reserved</p>
        <p className="small">Arquitectura de Microservicios con Spring Boot y PostgreSQL</p>
      </footer>
    </div>
  );
}

function InfoComponent() {
  return (
    <div className="card mt-4">
      <div className="card-body">
        <h5 className="card-title">Información del Sistema</h5>
        <div className="row">
          <div className="col-md-6">
            <h6>🔌 Servicios</h6>
            <ul>
              <li><strong>Authors Service:</strong> http://localhost:8001</li>
              <li><strong>Publications Service:</strong> http://localhost:8002</li>
              <li><strong>Frontend:</strong> http://localhost:3000</li>
            </ul>
          </div>
          <div className="col-md-6">
            <h6>📊 Bases de Datos</h6>
            <ul>
              <li><strong>Authors DB:</strong> postgresql://localhost:5432/authors_db</li>
              <li><strong>Publications DB:</strong> postgresql://localhost:5433/publications_db</li>
            </ul>
          </div>
        </div>
        <div className="mt-3">
          <h6>🏗️ Arquitectura</h6>
          <p>
            Sistema basado en microservicios con dos servicios independientes:
            <strong> Authors Service</strong> y <strong>Publications Service</strong>.
            Cada servicio tiene su propia base de datos PostgreSQL.
          </p>
          <p>
            El servicio de Publicaciones se comunica con el servicio de Autores
            para validar que el autor exista antes de crear una publicación.
          </p>
        </div>
      </div>
    </div>
  );
}

export default App;
