import axios from 'axios';
import { inventoryService } from '../services/inventoryService';

// Mock axios
jest.mock('axios');

describe('Inventory Service', () => {
  beforeEach(() => {
    // Clear all mocks before each test
    jest.clearAllMocks();
  });

  const mockItems = [
    { id: 1, name: 'Laptop', sku: 'LAP-001', category: 'Electronics', price: 999.99, quantity: 15 },
    { id: 2, name: 'Smartphone', sku: 'SPH-001', category: 'Electronics', price: 599.99, quantity: 8 }
  ];

  const mockDashboardData = {
    totalItems: 2,
    lowStockItems: 1,
    outOfStockItems: 0,
    items: mockItems,
    recentActivities: []
  };

  test('getDashboardStats fetches data correctly', async () => {
    axios.get.mockResolvedValueOnce({ data: mockDashboardData });
    
    const result = await inventoryService.getDashboardStats();
    
    expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/api/inventory/dashboard');
    expect(result).toEqual(mockDashboardData);
  });

  test('getAllItems fetches all items correctly', async () => {
    axios.get.mockResolvedValueOnce({ data: mockItems });
    
    const result = await inventoryService.getAllItems();
    
    expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/api/inventory');
    expect(result).toEqual(mockItems);
  });

  test('getItemById fetches a single item correctly', async () => {
    const mockItem = mockItems[0];
    axios.get.mockResolvedValueOnce({ data: mockItem });
    
    const result = await inventoryService.getItemById(1);
    
    expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/api/inventory/1');
    expect(result).toEqual(mockItem);
  });

  test('searchItems fetches filtered items correctly', async () => {
    const filteredItems = [mockItems[0]];
    axios.get.mockResolvedValueOnce({ data: filteredItems });
    
    const result = await inventoryService.searchItems('Laptop');
    
    expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/api/inventory/search?query=Laptop');
    expect(result).toEqual(filteredItems);
  });

  test('createItem creates a new item correctly', async () => {
    const newItem = { name: 'New Item', sku: 'NEW-001', category: 'Other', price: 19.99, quantity: 5 };
    const createdItem = { id: 3, ...newItem };
    
    axios.post.mockResolvedValueOnce({ data: createdItem });
    
    const result = await inventoryService.createItem(newItem);
    
    expect(axios.post).toHaveBeenCalledWith('http://localhost:8080/api/inventory', newItem);
    expect(result).toEqual(createdItem);
  });

  test('updateItem updates an existing item correctly', async () => {
    const updatedItem = { id: 1, name: 'Updated Laptop', sku: 'LAP-001', category: 'Electronics', price: 1099.99, quantity: 20 };
    
    axios.put.mockResolvedValueOnce({ data: updatedItem });
    
    const result = await inventoryService.updateItem(1, updatedItem);
    
    expect(axios.put).toHaveBeenCalledWith('http://localhost:8080/api/inventory/1', updatedItem);
    expect(result).toEqual(updatedItem);
  });

  test('deleteItem deletes an item correctly', async () => {
    axios.delete.mockResolvedValueOnce({});
    
    const result = await inventoryService.deleteItem(1);
    
    expect(axios.delete).toHaveBeenCalledWith('http://localhost:8080/api/inventory/1');
    expect(result).toBe(true);
  });

  test('handles errors in API calls', async () => {
    const errorMessage = 'Network Error';
    axios.get.mockRejectedValueOnce(new Error(errorMessage));
    
    // Spy on console.error
    const consoleSpy = jest.spyOn(console, 'error').mockImplementation();
    
    await expect(inventoryService.getDashboardStats()).rejects.toThrow(errorMessage);
    
    expect(consoleSpy).toHaveBeenCalled();
    expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/api/inventory/dashboard');
    
    // Restore console.error
    consoleSpy.mockRestore();
  });
});
