import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const inventoryService = {
    getDashboardStats: async () => {
        try {
            const response = await axios.get(`${API_URL}/inventory/dashboard`);
            return response.data;
        } catch (error) {
            console.error('Error fetching dashboard stats:', error);
            throw error;
        }
    },
    
    getAllItems: async () => {
        try {
            const response = await axios.get(`${API_URL}/inventory`);
            return response.data;
        } catch (error) {
            console.error('Error fetching inventory items:', error);
            throw error;
        }
    },
    
    getItemById: async (id) => {
        try {
            const response = await axios.get(`${API_URL}/inventory/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching item with id ${id}:`, error);
            throw error;
        }
    },
    
    searchItems: async (query) => {
        try {
            const response = await axios.get(`${API_URL}/inventory/search?query=${query}`);
            return response.data;
        } catch (error) {
            console.error('Error searching inventory items:', error);
            throw error;
        }
    },
    
    createItem: async (itemData) => {
        try {
            const response = await axios.post(`${API_URL}/inventory`, itemData);
            return response.data;
        } catch (error) {
            console.error('Error creating inventory item:', error);
            throw error;
        }
    },
    
    updateItem: async (id, itemData) => {
        try {
            const response = await axios.put(`${API_URL}/inventory/${id}`, itemData);
            return response.data;
        } catch (error) {
            console.error(`Error updating item with id ${id}:`, error);
            throw error;
        }
    },
    
    deleteItem: async (id) => {
        try {
            await axios.delete(`${API_URL}/inventory/${id}`);
            return true;
        } catch (error) {
            console.error(`Error deleting item with id ${id}:`, error);
            throw error;
        }
    }
};
