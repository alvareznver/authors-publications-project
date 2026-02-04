import axios from 'axios';

const AUTHORS_API = process.env.REACT_APP_AUTHORS_API || 'http://localhost:8001/api/v1/authors';
const PUBLICATIONS_API = process.env.REACT_APP_PUBLICATIONS_API || 'http://localhost:8002/api/v1/publications';

const apiClient = axios.create({
    headers: {
        'Content-Type': 'application/json',
    }
});

// Authors API
export const authorService = {
    getAllAuthors: (page = 0, size = 10) => 
        apiClient.get(`${AUTHORS_API}?page=${page}&size=${size}`),
    
    getAuthorById: (id) => 
        apiClient.get(`${AUTHORS_API}/${id}`),
    
    createAuthor: (data) => 
        apiClient.post(AUTHORS_API, data),
    
    updateAuthor: (id, data) => 
        apiClient.put(`${AUTHORS_API}/${id}`, data),
    
    deleteAuthor: (id) => 
        apiClient.delete(`${AUTHORS_API}/${id}`),
    
    searchAuthors: (keyword, page = 0, size = 10) => 
        apiClient.get(`${AUTHORS_API}/search?keyword=${keyword}&page=${page}&size=${size}`),
    
    getTotalAuthors: () => 
        apiClient.get(`${AUTHORS_API}/stats/total`),
};

// Publications API
export const publicationService = {
    getAllPublications: (page = 0, size = 10) => 
        apiClient.get(`${PUBLICATIONS_API}?page=${page}&size=${size}`),
    
    getPublicationById: (id) => 
        apiClient.get(`${PUBLICATIONS_API}/${id}`),
    
    createPublication: (data) => 
        apiClient.post(PUBLICATIONS_API, data),
    
    updatePublicationStatus: (id, status, notes = '') => 
        apiClient.patch(`${PUBLICATIONS_API}/${id}/status`, {
            status: status,
            reviewerNotes: notes
        }),
    
    deletePublication: (id) => 
        apiClient.delete(`${PUBLICATIONS_API}/${id}`),
    
    searchPublications: (keyword, page = 0, size = 10) => 
        apiClient.get(`${PUBLICATIONS_API}/search?keyword=${keyword}&page=${page}&size=${size}`),
    
    getPublicationsByAuthor: (authorId, page = 0, size = 10) => 
        apiClient.get(`${PUBLICATIONS_API}/author/${authorId}?page=${page}&size=${size}`),
    
    getPublicationsByStatus: (status, page = 0, size = 10) => 
        apiClient.get(`${PUBLICATIONS_API}/status/${status}?page=${page}&size=${size}`),
    
    getTotalPublications: () => 
        apiClient.get(`${PUBLICATIONS_API}/stats/total`),
    
    getTotalPublicationsByStatus: (status) => 
        apiClient.get(`${PUBLICATIONS_API}/stats/by-status/${status}`),
};

export default apiClient;
