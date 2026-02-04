import React, { useState, useEffect } from 'react';
import { Button, Form, Table, Alert, Modal, Row, Col, Badge } from 'react-bootstrap';
import { publicationService, authorService } from '../services/apiService';

const PublicationsComponent = () => {
  const [publications, setPublications] = useState([]);
  const [authors, setAuthors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [showStatusModal, setShowStatusModal] = useState(false);
  const [selectedPublication, setSelectedPublication] = useState(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [filterStatus, setFilterStatus] = useState(null);

  const [formData, setFormData] = useState({
    title: '',
    description: '',
    content: '',
    authorId: '',
    keywords: '',
    category: '',
    language: 'ES'
  });

  const [statusData, setStatusData] = useState({
    status: 'IN_REVIEW',
    reviewerNotes: ''
  });

  useEffect(() => {
    loadPublications();
    loadAuthors();
  }, [page, filterStatus]);

  const loadPublications = async () => {
    setLoading(true);
    setError(null);
    try {
      let response;
      if (filterStatus) {
        response = await publicationService.getPublicationsByStatus(filterStatus, page, 10);
      } else {
        response = await publicationService.getAllPublications(page, 10);
      }
      setPublications(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (err) {
      setError(err.response?.data?.message || 'Error al cargar publicaciones');
    } finally {
      setLoading(false);
    }
  };

  const loadAuthors = async () => {
    try {
      const response = await authorService.getAllAuthors(0, 100);
      setAuthors(response.data.content);
    } catch (err) {
      console.error('Error cargando autores:', err);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleStatusChange = (e) => {
    const { name, value } = e.target;
    setStatusData(prev => ({
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
      await publicationService.createPublication(formData);
      setSuccess('Publicación creada correctamente');
      resetForm();
      loadPublications();
    } catch (err) {
      setError(err.response?.data?.message || 'Error al guardar publicación');
    } finally {
      setLoading(false);
    }
  };

  const handleStatusSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(null);

    try {
      await publicationService.updatePublicationStatus(
        selectedPublication.id,
        statusData.status,
        statusData.reviewerNotes
      );
      setSuccess('Estado de publicación actualizado correctamente');
      resetStatusForm();
      loadPublications();
    } catch (err) {
      setError(err.response?.data?.message || 'Error al actualizar estado');
    } finally {
      setLoading(false);
    }
  };

  const handleEditStatus = (pub) => {
    setSelectedPublication(pub);
    setStatusData({
      status: pub.status,
      reviewerNotes: pub.reviewerNotes || ''
    });
    setShowStatusModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Estás seguro de que deseas eliminar esta publicación?')) {
      try {
        await publicationService.deletePublication(id);
        setSuccess('Publicación eliminada correctamente');
        loadPublications();
      } catch (err) {
        setError(err.response?.data?.message || 'Error al eliminar publicación');
      }
    }
  };

  const getStatusBadge = (status) => {
    const statusMap = {
      'DRAFT': 'secondary',
      'IN_REVIEW': 'warning',
      'APPROVED': 'info',
      'PUBLISHED': 'success',
      'REJECTED': 'danger',
      'ARCHIVED': 'dark'
    };
    return <Badge bg={statusMap[status] || 'secondary'}>{status}</Badge>;
  };

  const resetForm = () => {
    setFormData({
      title: '',
      description: '',
      content: '',
      authorId: '',
      keywords: '',
      category: '',
      language: 'ES'
    });
    setShowModal(false);
  };

  const resetStatusForm = () => {
    setStatusData({
      status: 'IN_REVIEW',
      reviewerNotes: ''
    });
    setShowStatusModal(false);
    setSelectedPublication(null);
  };

  if (loading && publications.length === 0) {
    return <div className="text-center py-5">Cargando publicaciones...</div>;
  }

  return (
    <div>
      {error && <Alert variant="danger" className="error-alert">{error}</Alert>}
      {success && <Alert variant="success" className="success-alert">{success}</Alert>}

      <Row className="mb-4">
        <Col md={6}>
          <Button 
            variant="primary" 
            onClick={() => { resetForm(); setShowModal(true); }}
          >
            ➕ Nueva Publicación
          </Button>
        </Col>
        <Col md={6}>
          <Form.Select 
            value={filterStatus || ''} 
            onChange={(e) => { setFilterStatus(e.target.value || null); setPage(0); }}
          >
            <option value="">Todos los estados</option>
            <option value="DRAFT">Borrador</option>
            <option value="IN_REVIEW">En Revisión</option>
            <option value="APPROVED">Aprobada</option>
            <option value="PUBLISHED">Publicada</option>
            <option value="REJECTED">Rechazada</option>
            <option value="ARCHIVED">Archivada</option>
          </Form.Select>
        </Col>
      </Row>

      {/* Modal para crear publicación */}
      <Modal show={showModal} onHide={resetForm} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Crear Nueva Publicación</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Título *</Form.Label>
              <Form.Control
                type="text"
                name="title"
                value={formData.title}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Autor *</Form.Label>
              <Form.Select
                name="authorId"
                value={formData.authorId}
                onChange={handleInputChange}
                required
              >
                <option value="">Seleccionar autor...</option>
                {authors.map(author => (
                  <option key={author.id} value={author.id}>
                    {author.name}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Contenido *</Form.Label>
              <Form.Control
                as="textarea"
                rows={5}
                name="content"
                value={formData.content}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Descripción</Form.Label>
              <Form.Control
                as="textarea"
                rows={2}
                name="description"
                value={formData.description}
                onChange={handleInputChange}
              />
            </Form.Group>

            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Categoría</Form.Label>
                  <Form.Control
                    type="text"
                    name="category"
                    value={formData.category}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Idioma</Form.Label>
                  <Form.Select
                    name="language"
                    value={formData.language}
                    onChange={handleInputChange}
                  >
                    <option value="ES">Español</option>
                    <option value="EN">English</option>
                    <option value="FR">Français</option>
                  </Form.Select>
                </Form.Group>
              </Col>
            </Row>

            <Form.Group className="mb-3">
              <Form.Label>Palabras clave</Form.Label>
              <Form.Control
                type="text"
                name="keywords"
                value={formData.keywords}
                onChange={handleInputChange}
                placeholder="Separadas por comas"
              />
            </Form.Group>

            <div className="d-flex justify-content-between">
              <Button variant="secondary" onClick={resetForm}>
                Cancelar
              </Button>
              <Button variant="primary" type="submit" disabled={loading}>
                {loading ? 'Creando...' : 'Crear'}
              </Button>
            </div>
          </Form>
        </Modal.Body>
      </Modal>

      {/* Modal para cambiar estado */}
      <Modal show={showStatusModal} onHide={resetStatusForm}>
        <Modal.Header closeButton>
          <Modal.Title>Cambiar Estado de Publicación</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleStatusSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Nuevo Estado *</Form.Label>
              <Form.Select
                name="status"
                value={statusData.status}
                onChange={handleStatusChange}
                required
              >
                <option value="DRAFT">Borrador</option>
                <option value="IN_REVIEW">En Revisión</option>
                <option value="APPROVED">Aprobada</option>
                <option value="PUBLISHED">Publicada</option>
                <option value="REJECTED">Rechazada</option>
                <option value="ARCHIVED">Archivada</option>
              </Form.Select>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Notas del Revisor</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="reviewerNotes"
                value={statusData.reviewerNotes}
                onChange={handleStatusChange}
              />
            </Form.Group>

            <div className="d-flex justify-content-between">
              <Button variant="secondary" onClick={resetStatusForm}>
                Cancelar
              </Button>
              <Button variant="primary" type="submit" disabled={loading}>
                {loading ? 'Actualizando...' : 'Actualizar'}
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
              <th>Título</th>
              <th>Autor</th>
              <th>Estado</th>
              <th>Categoría</th>
              <th>Vistas</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {publications.map(pub => (
              <tr key={pub.id}>
                <td><Badge bg="info">{pub.id}</Badge></td>
                <td>{pub.title}</td>
                <td>
                  {pub.author ? (
                    <small>{pub.author.name}</small>
                  ) : (
                    <small className="text-muted">ID: {pub.authorId}</small>
                  )}
                </td>
                <td>{getStatusBadge(pub.status)}</td>
                <td>{pub.category || '-'}</td>
                <td className="text-center">
                  <Badge bg="info">{pub.viewsCount || 0}</Badge>
                </td>
                <td>
                  <Button 
                    variant="warning" 
                    size="sm" 
                    className="me-2"
                    onClick={() => handleEditStatus(pub)}
                  >
                    📋 Estado
                  </Button>
                  <Button 
                    variant="danger" 
                    size="sm"
                    onClick={() => handleDelete(pub.id)}
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
        <small className="text-muted">{publications.length} publicaciones mostradas</small>
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

export default PublicationsComponent;
