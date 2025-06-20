# Frontend Unit Testing Guide

This document outlines the approach and structure for unit testing the React frontend components of the Inventory Management System.

## Testing Structure

Tests are organized in `__tests__` directories alongside the components they test:

```
frontend/
  src/
    components/
      StatCard.js
      __tests__/
        StatCard.test.js
    services/
      inventoryService.js
      __tests__/
        inventoryService.test.js
    __tests__/
      App.test.js
```

## Testing Stack

- **Jest**: Testing framework
- **React Testing Library**: For testing React components
- **@testing-library/user-event**: For simulating user interactions
- **jest-dom**: For enhanced DOM element assertions

## Running Tests

To run all tests:

```bash
npm test
```

To run tests with coverage:

```bash
npm test -- --coverage
```

To run a specific test file:

```bash
npm test -- StatCard.test.js
```

## Testing Approach

### Component Tests

Component tests focus on:
1. **Rendering**: Ensuring components render correctly with different props
2. **User Interaction**: Testing component behavior when users interact with it
3. **Integration**: Verifying components work together correctly

Example component test structure:

```javascript
describe('ComponentName', () => {
  const defaultProps = { /* default props */ };

  test('renders correctly with default props', () => {
    // Test implementation
  });

  test('handles user interaction correctly', () => {
    // Test implementation
  });
});
```

### Service Tests

Service tests focus on:
1. **API Calls**: Ensuring correct endpoints are called
2. **Data Transformation**: Verifying data is processed correctly
3. **Error Handling**: Testing error scenarios

## Mocking

We use Jest's mocking capabilities to isolate components and services during testing:

1. **API Calls**: We mock Axios to prevent actual API calls during tests
2. **Chart Components**: We mock Chart.js components for easier testing
3. **Component Dependencies**: We mock child components when needed

Example of mocking:

```javascript
// Mock a service
jest.mock('../services/inventoryService');

// Mock a component
jest.mock('react-chartjs-2', () => ({
  Pie: () => <div data-testid="pie-chart">Chart</div>
}));
```

## Test Examples

### Basic Component Test

```javascript
test('renders a StatCard with correct title and value', () => {
  render(<StatCard title="Total Items" value={42} color="#1976d2" />);
  
  expect(screen.getByText('Total Items')).toBeInTheDocument();
  expect(screen.getByText('42')).toBeInTheDocument();
});
```

### Testing User Interaction

```javascript
test('calls onSearch when input changes', () => {
  const mockSearch = jest.fn();
  render(<SearchBar searchQuery="" onSearch={mockSearch} />);
  
  const searchInput = screen.getByLabelText(/search inventory/i);
  fireEvent.change(searchInput, { target: { value: 'test' } });
  
  expect(mockSearch).toHaveBeenCalledWith('test');
});
```

### Testing API Service

```javascript
test('getAllItems fetches data correctly', async () => {
  axios.get.mockResolvedValueOnce({ data: mockItems });
  
  const result = await inventoryService.getAllItems();
  
  expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/api/inventory');
  expect(result).toEqual(mockItems);
});
```

## Best Practices

1. **Test Behaviors, Not Implementation**: Focus on what the component does, not how it's implemented
2. **Use Data-Testids Sparingly**: Prefer querying by role, text, or label
3. **Mock External Dependencies**: Isolate the component being tested
4. **Test Edge Cases**: Empty states, loading states, error states
5. **Keep Tests Independent**: Each test should run in isolation
6. **Descriptive Test Names**: Name tests so they clearly describe the behavior being tested

By following these guidelines, we maintain a robust test suite that helps prevent regressions and documents component behavior.
