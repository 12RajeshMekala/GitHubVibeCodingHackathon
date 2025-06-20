import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import StatCard from '../StatCard';

describe('StatCard Component', () => {
  const defaultProps = {
    title: 'Total Items',
    value: 42,
    color: '#1976d2'
  };

  test('renders the card with title and value', () => {
    render(<StatCard {...defaultProps} />);
    
    expect(screen.getByText('Total Items')).toBeInTheDocument();
    expect(screen.getByText('42')).toBeInTheDocument();
  });

  test('applies the correct border color', () => {
    const { container } = render(<StatCard {...defaultProps} />);
    
    // Find the Paper component (first div with role="region")
    const paperElement = container.querySelector('.MuiPaper-root');
    
    // Check that it has the correct styling
    expect(paperElement).toHaveStyle(`border-top: 4px solid ${defaultProps.color}`);
  });

  test('renders with different values', () => {
    const props = {
      title: 'Low Stock',
      value: 5,
      color: '#f44336'
    };
    
    render(<StatCard {...props} />);
    
    expect(screen.getByText('Low Stock')).toBeInTheDocument();
    expect(screen.getByText('5')).toBeInTheDocument();
  });
});
