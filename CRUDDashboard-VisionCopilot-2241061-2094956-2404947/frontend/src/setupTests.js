// jest-setup.js
import '@testing-library/jest-dom';

// Mock CSS modules
jest.mock('*.css', () => ({}));

// Mock DOM properties not implemented in jsdom
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: jest.fn().mockImplementation(query => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: jest.fn(),
    removeListener: jest.fn(),
    addEventListener: jest.fn(),
    removeEventListener: jest.fn(),
    dispatchEvent: jest.fn(),
  })),
});

// Suppress React 18 console warnings
const originalConsoleError = console.error;
console.error = (...args) => {
  if (
    args[0]?.includes('Warning: ReactDOM.render is no longer supported in React 18') ||
    args[0]?.includes('Warning: An update to') ||
    args[0]?.includes('Warning: Did not expect server HTML to contain')
  ) {
    return;
  }
  originalConsoleError(...args);
};
