import React from 'react';
import { render, screen, fireEvent, within } from '@testing-library/react';
import '@testing-library/jest-dom';
import InventoryTable from '../InventoryTable';

describe('InventoryTable Component', () => {
  const mockItems = [
    { id: 1, name: 'Laptop', sku: 'LPT-001', category: 'Electronics', price: 999.99, quantity: 15 },
    { id: 2, name: 'Smartphone', sku: 'SPH-001', category: 'Electronics', price: 599.99, quantity: 8 },
    { id: 3, name: 'Headphones', sku: 'HPH-001', category: 'Electronics', price: 149.99, quantity: 0 },
    { id: 4, name: 'T-Shirt', sku: 'TSH-001', category: 'Clothing', price: 19.99, quantity: 50 }
  ];
  
  const mockEdit = jest.fn();
  const mockDelete = jest.fn();
  
  const defaultProps = {
    items: mockItems,
    loading: false,
    onEditItem: mockEdit,
    onDeleteItem: mockDelete
  };

  beforeEach(() => {
    mockEdit.mockClear();
    mockDelete.mockClear();
  });

  test('renders the table with all items', () => {
    render(<InventoryTable {...defaultProps} />);
    
    // Check for column headers
    expect(screen.getByText('Name')).toBeInTheDocument();
    expect(screen.getByText('SKU')).toBeInTheDocument();
    expect(screen.getByText('Category')).toBeInTheDocument();
    expect(screen.getByText('Price')).toBeInTheDocument();
    expect(screen.getByText('Quantity')).toBeInTheDocument();
    expect(screen.getByText('Actions')).toBeInTheDocument();
    
    // Check if all items are rendered
    expect(screen.getByText('Laptop')).toBeInTheDocument();
    expect(screen.getByText('Smartphone')).toBeInTheDocument();
    expect(screen.getByText('Headphones')).toBeInTheDocument();
    expect(screen.getByText('T-Shirt')).toBeInTheDocument();
  });

  test('displays loading spinner when loading', () => {
    render(<InventoryTable {...defaultProps} loading={true} />);
    
    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  test('calls onEditItem when edit button is clicked', () => {
    render(<InventoryTable {...defaultProps} />);
    
    // Find the edit buttons (could be improved by adding data-testid attributes)
    const rows = screen.getAllByRole('row');
    const laptopRow = rows.find(row => within(row).queryByText('Laptop'));
    
    const editButton = within(laptopRow).getByLabelText('Edit');
    fireEvent.click(editButton);
    
    expect(mockEdit).toHaveBeenCalledWith(mockItems[0]);
  });

  test('calls onDeleteItem when delete button is clicked', () => {
    render(<InventoryTable {...defaultProps} />);
    
    // Find the delete buttons
    const rows = screen.getAllByRole('row');
    const smartphoneRow = rows.find(row => within(row).queryByText('Smartphone'));
    
    const deleteButton = within(smartphoneRow).getByLabelText('Delete');
    fireEvent.click(deleteButton);
    
    expect(mockDelete).toHaveBeenCalledWith(mockItems[1]);
  });

  test('applies low-stock styling to items with quantity < 10', () => {
    const { container } = render(<InventoryTable {...defaultProps} />);
    
    // Find the smartphone row (quantity 8)
    const rows = screen.getAllByRole('row');
    const smartphoneRow = rows.find(row => within(row).queryByText('Smartphone'));
    
    // Check if it has the low-stock class
    expect(smartphoneRow).toHaveClass('low-stock');
  });

  test('applies out-of-stock styling to items with quantity = 0', () => {
    const { container } = render(<InventoryTable {...defaultProps} />);
    
    // Find the headphones row (quantity 0)
    const rows = screen.getAllByRole('row');
    const headphonesRow = rows.find(row => within(row).queryByText('Headphones'));
    
    // Check if it has the out-of-stock class
    expect(headphonesRow).toHaveClass('out-of-stock');
  });

  test('sorts items when column header is clicked', () => {
    render(<InventoryTable {...defaultProps} />);
    
    // Get the Name column header
    const nameColumnHeader = screen.getByText('Name').closest('th');
    const sortButton = within(nameColumnHeader).getByRole('button');
    
    // Click to sort by name ascending
    fireEvent.click(sortButton);
    
    // Check the order of items in the table (first should be Headphones)
    const rows = screen.getAllByRole('row');
    
    // First row is the header, so rows[1] is the first data row
    expect(within(rows[1]).getByText('Headphones')).toBeInTheDocument();
    
    // Click again to sort descending
    fireEvent.click(sortButton);
    
    // Check the new order (first should be T-Shirt)
    const rowsAfterSort = screen.getAllByRole('row');
    expect(within(rowsAfterSort[1]).getByText('T-Shirt')).toBeInTheDocument();
  });
});
