import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import CategoryDistributionChart from '../CategoryDistributionChart';

// Mock the react-chartjs-2 component
jest.mock('react-chartjs-2', () => ({
  Pie: () => <div data-testid="pie-chart">Chart Component</div>
}));

describe('CategoryDistributionChart Component', () => {
  const mockItems = [
    { id: 1, name: 'Laptop', category: 'Electronics', quantity: 15 },
    { id: 2, name: 'Monitor', category: 'Electronics', quantity: 10 },
    { id: 3, name: 'T-Shirt', category: 'Clothing', quantity: 25 },
    { id: 4, name: 'Jeans', category: 'Clothing', quantity: 20 },
    { id: 5, name: 'Chair', category: 'Furniture', quantity: 5 },
    { id: 6, name: 'Table', category: 'Furniture', quantity: 8 }
  ];

  test('renders the chart component', () => {
    render(<CategoryDistributionChart items={mockItems} />);
    
    expect(screen.getByTestId('pie-chart')).toBeInTheDocument();
  });

  test('processes data correctly for the chart', () => {
    // We need to access the internal implementation to test the data processing
    // This is a more implementation-focused test that verifies the chart data structure
    
    // Create a modified component that exposes the processed data
    const TestComponent = () => {
      const data = CategoryDistributionChart({ items: mockItems })
        .props.children.props.data;
      
      return (
        <div>
          <div data-testid="labels">{JSON.stringify(data.labels)}</div>
          <div data-testid="data">{JSON.stringify(data.datasets[0].data)}</div>
        </div>
      );
    };
    
    render(<TestComponent />);
    
    // Get the serialized JSON elements
    const labelsJson = screen.getByTestId('labels').textContent;
    const dataJson = screen.getByTestId('data').textContent;
    
    // Parse the JSON
    const labels = JSON.parse(labelsJson);
    const data = JSON.parse(dataJson);
    
    // Verify the processed data
    expect(labels).toContain('Electronics');
    expect(labels).toContain('Clothing');
    expect(labels).toContain('Furniture');
    
    // Expect 2 Electronics, 2 Clothing, 2 Furniture
    expect(data).toEqual([2, 2, 2]);
  });

  test('handles empty items array', () => {
    // Create a modified component that exposes the processed data
    const TestComponent = () => {
      const data = CategoryDistributionChart({ items: [] })
        .props.children.props.data;
      
      return (
        <div>
          <div data-testid="labels">{JSON.stringify(data.labels)}</div>
          <div data-testid="data">{JSON.stringify(data.datasets[0].data)}</div>
        </div>
      );
    };
    
    render(<TestComponent />);
    
    // Get the serialized JSON elements
    const labelsJson = screen.getByTestId('labels').textContent;
    const dataJson = screen.getByTestId('data').textContent;
    
    // Parse the JSON
    const labels = JSON.parse(labelsJson);
    const data = JSON.parse(dataJson);
    
    // Verify default data for empty chart
    expect(labels).toEqual(['No Data']);
    expect(data).toEqual([1]);
  });
});
