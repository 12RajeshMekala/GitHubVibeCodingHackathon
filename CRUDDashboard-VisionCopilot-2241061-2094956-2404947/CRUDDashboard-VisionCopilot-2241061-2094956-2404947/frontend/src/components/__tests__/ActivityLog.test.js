import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import ActivityLog from '../ActivityLog';

describe('ActivityLog Component', () => {
  const mockActivities = [
    { id: 1, action: 'ITEM_CREATED: New Headphones', timestamp: '2023-05-15T10:30:00Z' },
    { id: 2, action: 'ITEM_UPDATED: Laptop', timestamp: '2023-05-15T09:45:00Z' },
    { id: 3, action: 'ITEM_DELETED: Old Phone', timestamp: '2023-05-14T14:20:00Z' }
  ];

  test('renders activity log title', () => {
    render(<ActivityLog activities={mockActivities} />);
    expect(screen.getByText('Recent Activity')).toBeInTheDocument();
  });

  test('renders all activities in the list', () => {
    render(<ActivityLog activities={mockActivities} />);
    
    expect(screen.getByText('ITEM_CREATED: New Headphones')).toBeInTheDocument();
    expect(screen.getByText('ITEM_UPDATED: Laptop')).toBeInTheDocument();
    expect(screen.getByText('ITEM_DELETED: Old Phone')).toBeInTheDocument();
    
    // Should format timestamps
    const timestamps = screen.getAllByText(content => content.includes('/'));
    expect(timestamps.length).toBe(3);
  });

  test('shows empty state message when no activities', () => {
    render(<ActivityLog activities={[]} />);
    
    expect(screen.getByText('Recent Activity')).toBeInTheDocument();
    expect(screen.getByText('No recent activities')).toBeInTheDocument();
  });

  test('handles null activities prop', () => {
    render(<ActivityLog activities={null} />);
    
    expect(screen.getByText('Recent Activity')).toBeInTheDocument();
    expect(screen.getByText('No recent activities')).toBeInTheDocument();
  });
});
