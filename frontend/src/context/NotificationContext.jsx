import { createContext, useContext, useState } from 'react'
import { mockNotifications } from '../data/mockData'
const NotificationContext = createContext(null)
export function NotificationProvider({ children }) {
  const [notifications, setNotifications] = useState(mockNotifications)
  const unreadCount = notifications.filter((item) => !item.read).length
  const markRead = (id) => setNotifications((items) => items.map((item) => item.id === id ? { ...item, read: true } : item))
  const remove = (id) => setNotifications((items) => items.filter((item) => item.id !== id))
  return <NotificationContext.Provider value={{ notifications, unreadCount, markRead, remove }}>{children}</NotificationContext.Provider>
}
export const useNotifications = () => useContext(NotificationContext)