import api from "./api";

export const login = async (email, password) => {
    const response = await api.post("/auth/login", {
        email,
        password
    });

    const token = response.data.token;

    localStorage.setItem("token", token);

    return response.data;
};

export const getCurrentUser = async () => {
    const response = await api.get("/auth/me");
    return response.data;
};

export const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("leaveweb_token");
    localStorage.removeItem("leaveweb_user");
};