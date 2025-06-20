import React from 'react';
import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import '@testing-library/jest-dom';
import App from '../App';
import { inventoryService } from '../services/inventoryService';

// Mock the inventoryService
jest.mock('../services/inventoryService');

describe('App Component', () => {
  const mockDashboardData = {
    totalItems: 25,
    lowStockItems: 5,
    outOfStockItems: 2,
    items: [
      { id: 1, name: 'Laptop', sku: 'LAP-001', category: 'Electronics', price: 999.99, quantity: 15 },
      { id: 2, name: 'Smartphone', sku: 'SPH-001', category: 'Electronics', price: 599.99, quantity: 8 },
      { id: 3, name: 'Headphones', sku: 'HPH-001', category: 'Electronics', price: 149.99, quantity: 0 }
    ],
    recentActivities: [
      { id: 1, action: 'ITEM_CREATED', timestamp: '2023-06-01T10:30:00Z' },
      { id: 2, action: 'ITEM_UPDATED', timestamp: '2023-06-01T11:15:00Z' }
    ]
  };

  beforeEach(() => {
    // Mock the service methods
    inventoryService.getDashboardStats = jest.fn().mockResolvedValue(mockDashboardData);
    inventoryService.createItem = jest.fn().mockResolvedValue({ id: 4, name: 'New Item' });
    inventoryService.updateItem = jest.fn().mockResolvedValue({ id: 1, name: 'Updated Laptop' });
    inventoryService.deleteItem = jest.fn().mockResolvedValue(true);
  });

  test('renders dashboard with stats and inventory table', async () => {
    await act(async () => {
      render(<App />);
    });

    // Wait for dashboard data to load
    await waitFor(() => {
      expect(screen.getByText('Inventory Dashboard')).toBeInTheDocument();
    });

    // Check if stats are displayed correctly
    expect(screen.getByText('Total Items')).toBeInTheDocument();
    expect(screen.getByText('25')).toBeInTheDocument();
    
    expect(screen.getByText('Low on Stock')).toBeInTheDocument();
    expect(screen.getByText('5')).toBeInTheDocument();
    
    expect(screen.getByText('Out of Stock')).toBeInTheDocument();
    expect(screen.getByText('2')).toBeInTheDocument();

    // Check if inventory table shows items
    expect(screen.getByText('Laptop')).toBeInTheDocument();
    expect(screen.getByText('Smartphone')).toBeInTheDocument();
    expect(screen.getByText('Headphones')).toBeInTheDocument();
    
    // Check if activity log is rendered
    expect(screen.getByText('Recent Activity')).toBeInTheDocument();
    
    // Check if chart is rendered
    expect(screen.getByText('Items by Category')).toBeInTheDocument();
  });

  test('filters items when searching', async () => {
    await act(async () => {
      render(<App />);
    });

    // Wait for data to load
    await waitFor(() => {
      expect(screen.getByText('Inventory Dashboard')).toBeInTheDocument();
    });

    // Get search input and type "Laptop"
    const searchInput = screen.getByLabelText(/search inventory/i);
    fireEvent.change(searchInput, { target: { value: 'Laptop' } });

    // Should show only Laptop and filter out other items
    expect(screen.getByText('Laptop')).toBeInTheDocument();
    expect(screen.queryByText('Smartphone')).not.toBeInTheDocument();
    expect(screen.queryByText('Headphones')).not.toBeInTheDocument();
  });

  test('opens dialog when Add Item button is clicked', async () => {
    await act(async () => {
      render(<App />);
    });

    // Wait for data to load
    await waitFor(() => {
      expect(screen.getByText('Inventory Dashboard')).toBeInTheDocument();
    });

    // Click Add Item button
    const addButton = screen.getByText('Add Item');
    fireEvent.click(addButton);

    // Dialog should be open
    expect(screen.getByText('Add New Item')).toBeInTheDocument();
  });

  test('opens edit dialog when edit button is clicked', async () => {
    await act(async () => {
      render(<App />);
    });

    // Wait for data to load
    await waitFor(() => {
      expect(screen.getByText('Inventory Dashboard')).toBeInTheDocument();
    });

    // Find and click the first edit button
    const editButtons = screen.getAllByLabelText('Edit');
    fireEvent.click(editButtons[0]);

    // Dialog should be open with edit title
    expect(screen.getByText('Edit Item')).toBeInTheDocument();
  });

  test('calls deleteItem when delete button is clicked', async () => {
    await act(async () => {
      render(<App />);
    });

    // Wait for data to load
    await waitFor(() => {
      expect(screen.getByText('Inventory Dashboard')).toBeInTheDocument();
    });

    // Find and click the first delete button
    const deleteButtons = screen.getAllByLabelText('Delete');
    fireEvent.click(deleteButtons[0]);

    // Check if deleteItem was called (should be called with the first item's id)
    await waitFor(() => {
      expect(inventoryService.deleteItem).toHaveBeenCalledWith(1);
    });

    // Check if getDashboardStats was called again to refresh data
    expect(inventoryService.getDashboardStats).toHaveBeenCalledTimes(2);
  });
});
