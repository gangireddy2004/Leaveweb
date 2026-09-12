import api from './api'
export const getLeaveTypes = () => api.get('/admin/leave-types')
export const createLeaveType = (payload) => api.post('/admin/leave-types', payload)
export const updateLeaveType = (id, payload) => api.put(`/admin/leave-types/${id}`, payload)
export const deleteLeaveType = (id) => api.delete(`/admin/leave-types/${id}`)
export const getReports = (params) => api.get('/admin/reports', { params })
export const getPendingUsers = () => api.get('/admin/users/pending').then((response) => response.data)
export const approveUser = (id) => api.post(`/admin/users/${id}/approve`).then((response) => response.data)
export const rejectUser = (id, reason) => api.post(`/admin/users/${id}/reject`, { reason }).then((response) => response.data)