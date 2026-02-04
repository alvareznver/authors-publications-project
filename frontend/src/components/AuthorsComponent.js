import React, { useState, useEffect } from 'react';
import { Button, Form, Table, Alert, Modal, Row, Col, Badge } from 'react-bootstrap';
import { authorService } from '../services/apiService';

const AuthorsComponent = () => {
  const [authors, setAuthors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [selectedAuthor, setSelectedAuthor] = useState(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [formData, setFormData] = useState({
    name: '',
    email: '',
    bio: '',
    authorType: 'INDIVIDUAL',
    country: '',
    phone: ''
  });

  useEffect(() => {
    loadAuthors();
  }, [page]);

  const loadAuthors = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await authorService.getAllAuthors(page, 10);
      setAuthors(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (err) {
      setError(err.response?.data?.message || 'Error al cargar autores');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(null);

    try {
      if (selectedAuthor) {
        await authorService.updateAuthor(selectedAuthor.id, formData);
        setSuccess('Autor actualizado correctamente');
      } else {
        await authorService.createAuthor(formData);
        setSuccess('Autor creado correctamente');
      }
      resetForm();
      loadAuthors();
    } catch (err) {
      setError(err.response?.data?.message || 'Error al guardar autor');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (author) => {
    setSelectedAuthor(author);
    setFormData({
      name: author.name,
      email: author.email,
      bio: author.bio || '',
      authorType: author.authorType,
      country: author.country || '',
      phone: author.phone || ''
    });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Estás seguro de que deseas eliminar este autor?')) {
      try {
        await authorService.deleteAuthor(id);
        setSuccess('Autor eliminado correctamente');
        loadAuthors();
      } catch (err) {
        setError(err.response?.data?.message || 'Error al eliminar autor');
      }
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      email: '',
      bio: '',
      authorType: 'INDIVIDUAL',
      country: '',
      phone: ''
    });
    setSelectedAuthor(null);
    setShowModal(false);
  };

  if (loading && authors.length === 0) {
    return <div className="text-center py-5">Cargando autores...</div>;
  }

  return (
    <div>
      {error && <Alert variant="danger" className="error-alert">{error}</Alert>}
      {success && <Alert variant="success" className="success-alert">{success}</Alert>}

      <Button 
        variant="primary" 
        className="mb-4" 
        onClick={() => { resetForm(); setShowModal(true); }}
      >
        ➕ Nuevo Autor
      </Button>

      <Modal show={showModal} onHide={resetForm} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>{selectedAuthor ? 'Editar Autor' : 'Crear Nuevo Autor'}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Nombre *</Form.Label>
              <Form.Control
                type="text"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Email *</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Tipo de Autor *</Form.Label>
                  <Form.Select
                    name="authorType"
                    value={formData.authorType}
                    onChange={handleInputChange}
                  >
                    <option value="INDIVIDUAL">Individual</option>
                    <option value="ORGANIZATION">Organización</option>
                    <option value="ACADEMIC">Académico</option>
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>País</Form.Label>
                  <Form.Control
                    type="text"
                    name="country"
                    value={formData.country}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </Col>
            </Row>

            <Form.Group className="mb-3">
              <Form.Label>Teléfono</Form.Label>
              <Form.Control
                type="tel"
                name="phone"
                value={formData.phone}
                onChange={handleInputChange}
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Biografía</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="bio"
                value={formData.bio}
                onChange={handleInputChange}
              />
            </Form.Group>

            <div className="d-flex justify-content-between">
              <Button variant="secondary" onClick={resetForm}>
                Cancelar
              </Button>
              <Button variant="primary" type="submit" disabled={loading}>
                {loading ? 'Guardando...' : selectedAuthor ? 'Actualizar' : 'Crear'}
              </Button>
            </div>
          </Form>
        </Modal.Body>
      </Modal>

      <div className="table-responsive">
        <Table striped bordered hover>
          <thead className="table-dark">
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Email</th>
              <th>Tipo</th>
              <th>País</th>
              <th>Publicaciones</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {authors.map(author => (
              <tr key={author.id}>
                <td><Badge bg="info">{author.id}</Badge></td>
                <td>{author.name}</td>
                <td>{author.email}</td>
                <td><Badge bg="secondary">{author.authorType}</Badge></td>
                <td>{author.country || '-'}</td>
                <td className="text-center">
                  <Badge bg="success">{author.publicationsCount || 0}</Badge>
                </td>
                <td>
                  <Button 
                    variant="info" 
                    size="sm" 
                    className="me-2"
                    onClick={() => handleEdit(author)}
                  >
                    ✏️ Editar
                  </Button>
                  <Button 
                    variant="danger" 
                    size="sm"
                    onClick={() => handleDelete(author.id)}
                  >
                    🗑️ Eliminar
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>

      <div className="d-flex justify-content-between align-items-center mt-3">
        <small className="text-muted">{authors.length} autores mostrados</small>
        <div>
          <Button 
            variant="outline-secondary" 
            size="sm" 
            disabled={page === 0}
            onClick={() => setPage(page - 1)}
            className="me-2"
          >
            Anterior
          </Button>
          <Button 
            variant="outline-secondary" 
            size="sm" 
            disabled={page >= totalPages - 1}
            onClick={() => setPage(page + 1)}
          >
            Siguiente
          </Button>
        </div>
      </div>
    </div>
  );
};

export default AuthorsComponent;
