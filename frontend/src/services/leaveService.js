import api from "./api";

export const getMyLeaves = async () => {
    const response = await api.get("/leaves");
    return response.data;
};

export const getLeaveById = async (id) => {
    const response = await api.get(`/leaves/${id}`);
    return response.data;
};

export const applyLeave = async (leaveData) => {
    const response = await api.post("/leaves", leaveData);
    return response.data;
};

export const updateLeave = async (id, leaveData) => {
    const response = await api.put(`/leaves/${id}`, leaveData);
    return response.data;
};

export const cancelLeave = async (id) => {
    const response = await api.post(`/leaves/${id}/cancel`);
    return response.data;
};

export const deleteLeave = async (id) => {
    const response = await api.delete(`/leaves/${id}`);
    return response.data;
};