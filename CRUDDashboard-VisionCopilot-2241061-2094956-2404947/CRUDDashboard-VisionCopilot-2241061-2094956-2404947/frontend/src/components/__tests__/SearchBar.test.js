import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import SearchBar from '../SearchBar';

describe('SearchBar Component', () => {
  const mockSearch = jest.fn();
  const mockAddItem = jest.fn();
  
  const defaultProps = {
    searchQuery: '',
    onSearch: mockSearch,
    onAddItem: mockAddItem
  };

  beforeEach(() => {
    mockSearch.mockClear();
    mockAddItem.mockClear();
  });

  test('renders search input and add button', () => {
    render(<SearchBar {...defaultProps} />);
    
    expect(screen.getByLabelText(/search inventory/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /add item/i })).toBeInTheDocument();
  });

  test('displays the current search query', () => {
    render(<SearchBar {...defaultProps} searchQuery="test query" />);
    
    const searchInput = screen.getByLabelText(/search inventory/i);
    expect(searchInput).toHaveValue('test query');
  });

  test('calls onSearch when input changes', () => {
    render(<SearchBar {...defaultProps} />);
    
    const searchInput = screen.getByLabelText(/search inventory/i);
    fireEvent.change(searchInput, { target: { value: 'test search' } });
    
    expect(mockSearch).toHaveBeenCalledWith('test search');
  });

  test('calls onAddItem when add button is clicked', () => {
    render(<SearchBar {...defaultProps} />);
    
    const addButton = screen.getByRole('button', { name: /add item/i });
    fireEvent.click(addButton);
    
    expect(mockAddItem).toHaveBeenCalledTimes(1);
  });
});
