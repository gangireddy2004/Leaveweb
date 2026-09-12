export const mockUser = {
  id: 'EMP-1042', name: 'Maya Anderson', email: 'maya.anderson@leaveweb.com', phone: '+1 (415) 555-0142',
  department: 'Product Design', role: 'Employee', avatar: 'MA', isAdmin: true,
}

export const leaveTypes = [
  { id: 'casual', name: 'Casual leave', balance: 8, total: 12, color: 'teal', description: 'For personal errands and short breaks.' },
  { id: 'sick', name: 'Sick leave', balance: 11, total: 14, color: 'coral', description: 'Health and recovery related absence.' },
  { id: 'earned', name: 'Earned leave', balance: 16, total: 24, color: 'amber', description: 'Planned time away from work.' },
  { id: 'optional', name: 'Optional holiday', balance: 2, total: 3, color: 'blue', description: 'A personal choice of company holiday.' },
]

export const mockLeaves = [
  { id: 'LV-2084', type: 'Earned leave', typeId: 'earned', start: '2026-09-18', end: '2026-09-19', days: 2, reason: 'Family weekend out of town', status: 'Pending', applied: '2026-09-10', employee: 'Maya Anderson', email: 'maya.anderson@leaveweb.com' },
  { id: 'LV-2079', type: 'Casual leave', typeId: 'casual', start: '2026-09-04', end: '2026-09-04', days: 1, reason: 'Personal appointment', status: 'Approved', applied: '2026-08-28', employee: 'Maya Anderson', email: 'maya.anderson@leaveweb.com' },
  { id: 'LV-2061', type: 'Sick leave', typeId: 'sick', start: '2026-08-17', end: '2026-08-18', days: 2, reason: 'Recovery from seasonal flu', status: 'Approved', applied: '2026-08-16', employee: 'Maya Anderson', email: 'maya.anderson@leaveweb.com' },
  { id: 'LV-2047', type: 'Casual leave', typeId: 'casual', start: '2026-07-22', end: '2026-07-22', days: 1, reason: 'Home maintenance', status: 'Rejected', applied: '2026-07-15', employee: 'Maya Anderson', email: 'maya.anderson@leaveweb.com' },
]

export const mockUsers = [
  { id: 'EMP-1042', name: 'Maya Anderson', email: 'maya.anderson@leaveweb.com', department: 'Product Design', role: 'Employee', status: 'Active', joined: '2024-03-12' },
  { id: 'EMP-1038', name: 'Jon Bell', email: 'jon.bell@leaveweb.com', department: 'Engineering', role: 'Manager', status: 'Active', joined: '2023-11-08' },
  { id: 'EMP-1027', name: 'Priya Nair', email: 'priya.nair@leaveweb.com', department: 'Marketing', role: 'Employee', status: 'Active', joined: '2024-01-21' },
  { id: 'EMP-1019', name: 'Owen Wright', email: 'owen.wright@leaveweb.com', department: 'Finance', role: 'Employee', status: 'Inactive', joined: '2022-09-02' },
]

export const mockNotifications = [
  { id: 'N-1', title: 'Leave request approved', body: 'Your casual leave for September 4 was approved.', time: '12 minutes ago', read: false },
  { id: 'N-2', title: 'Upcoming holiday', body: 'Founders Day is coming up on October 14.', time: 'Yesterday', read: false },
  { id: 'N-3', title: 'Policy update', body: 'The optional holiday policy was updated by HR.', time: '3 days ago', read: true },
]

export const holidays = [
  { date: '14', month: 'OCT', name: 'Founders Day', day: 'Wednesday' },
  { date: '02', month: 'NOV', name: 'Community Day', day: 'Monday' },
  { date: '26', month: 'NOV', name: 'Thanksgiving', day: 'Thursday' },
]