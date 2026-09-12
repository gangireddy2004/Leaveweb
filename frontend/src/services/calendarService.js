import api from './api'
export const getCalendar = (year) => api.get('/calendar', { params: { year } }).then((response) => response.data)