import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import userEvent from '@testing-library/user-event';
import ItemFormDialog from '../ItemFormDialog';

describe('ItemFormDialog Component', () => {
  const mockSave = jest.fn();
  const mockClose = jest.fn();
  
  const defaultProps = {
    open: true,
    onClose: mockClose,
    onSave: mockSave,
    item: null
  };

  const mockItem = {
    id: 1,
    name: 'Test Laptop',
    sku: 'TST-001',
    category: 'Electronics',
    price: '999.99',
    quantity: '10',
    imageUrl: 'https://example.com/laptop.jpg'
  };

  beforeEach(() => {
    mockSave.mockClear();
    mockClose.mockClear();
  });

  test('renders dialog with empty form for new item', () => {
    render(<ItemFormDialog {...defaultProps} />);
    
    expect(screen.getByText('Add New Item')).toBeInTheDocument();
    
    // Check for empty form fields
    expect(screen.getByLabelText(/name/i)).toHaveValue('');
    expect(screen.getByLabelText(/sku/i)).toHaveValue('');
    expect(screen.getByLabelText(/price/i)).toHaveValue(null);
    expect(screen.getByLabelText(/quantity/i)).toHaveValue(null);
  });

  test('renders dialog with pre-filled form for editing', () => {
    render(<ItemFormDialog {...defaultProps} item={mockItem} />);
    
    expect(screen.getByText('Edit Item')).toBeInTheDocument();
    
    // Check for pre-filled form fields
    expect(screen.getByLabelText(/name/i)).toHaveValue('Test Laptop');
    expect(screen.getByLabelText(/sku/i)).toHaveValue('TST-001');
    expect(screen.getByLabelText(/price/i)).toHaveValue(999.99);
    expect(screen.getByLabelText(/quantity/i)).toHaveValue(10);
  });

  test('validation prevents submission of empty form', async () => {
    render(<ItemFormDialog {...defaultProps} />);
    
    // Try to submit without filling the form
    fireEvent.click(screen.getByText('Save'));
    
    // Check for validation error messages
    expect(await screen.findByText('Name is required')).toBeInTheDocument();
    expect(await screen.findByText('SKU is required')).toBeInTheDocument();
    expect(await screen.findByText('Category is required')).toBeInTheDocument();
    
    // Verify that save was not called
    expect(mockSave).not.toHaveBeenCalled();
  });

  test('validation prevents submission with invalid price and quantity', async () => {
    render(<ItemFormDialog {...defaultProps} />);
    
    // Fill out form with invalid price and quantity
    await userEvent.type(screen.getByLabelText(/name/i), 'Test Item');
    await userEvent.type(screen.getByLabelText(/sku/i), 'TST-002');
    
    // Select a category
    fireEvent.mouseDown(screen.getByLabelText(/category/i));
    fireEvent.click(screen.getByText('Electronics'));
    
    // Enter invalid price and quantity
    await userEvent.clear(screen.getByLabelText(/price/i));
    await userEvent.type(screen.getByLabelText(/price/i), '-10.99');
    
    await userEvent.clear(screen.getByLabelText(/quantity/i));
    await userEvent.type(screen.getByLabelText(/quantity/i), '-5');
    
    // Try to submit
    fireEvent.click(screen.getByText('Save'));
    
    // Check for validation error messages
    expect(await screen.findByText('Price must be a positive number')).toBeInTheDocument();
    expect(await screen.findByText('Quantity must be a positive integer')).toBeInTheDocument();
    
    // Verify that save was not called
    expect(mockSave).not.toHaveBeenCalled();
  });

  test('submits form with valid data', async () => {
    render(<ItemFormDialog {...defaultProps} />);
    
    // Fill out the form with valid data
    await userEvent.type(screen.getByLabelText(/name/i), 'New Product');
    await userEvent.type(screen.getByLabelText(/sku/i), 'NP-001');
    
    // Select a category
    fireEvent.mouseDown(screen.getByLabelText(/category/i));
    fireEvent.click(screen.getByText('Clothing'));
    
    await userEvent.type(screen.getByLabelText(/price/i), '24.99');
    await userEvent.type(screen.getByLabelText(/quantity/i), '30');
    await userEvent.type(screen.getByLabelText(/image url/i), 'https://example.com/product.jpg');
    
    // Submit the form
    fireEvent.click(screen.getByText('Save'));
    
    // Verify that save was called with the correct data
    expect(mockSave).toHaveBeenCalledWith({
      name: 'New Product',
      sku: 'NP-001',
      category: 'Clothing',
      price: 24.99,
      quantity: 30,
      imageUrl: 'https://example.com/product.jpg'
    });
  });

  test('closes dialog when cancel button is clicked', () => {
    render(<ItemFormDialog {...defaultProps} />);
    
    fireEvent.click(screen.getByText('Cancel'));
    
    expect(mockClose).toHaveBeenCalledTimes(1);
  });
});
