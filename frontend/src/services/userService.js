import api from './api'
export const getUsers = (params) => api.get('/users', { params })
export const createUser = (payload) => api.post('/users', payload)
export const updateUser = (id, payload) => api.put(`/users/${id}`, payload)
export const deleteUser = (id) => api.delete(`/users/${id}`)