import React from 'react';
import { Pie } from 'react-chartjs-2';
import { Box } from '@mui/material';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';

// Register the components
ChartJS.register(ArcElement, Tooltip, Legend);

const CategoryDistributionChart = ({ items }) => {
  // Process data for the chart
  const processDataForChart = () => {
    if (!items || items.length === 0) {
      return {
        labels: ['No Data'],
        datasets: [{
          data: [1],
          backgroundColor: ['#e0e0e0']
        }]
      };
    }

    // Group items by category and count them
    const categoryMap = items.reduce((acc, item) => {
      acc[item.category] = (acc[item.category] || 0) + 1;
      return acc;
    }, {});
    
    // Extract labels and data from the map
    const labels = Object.keys(categoryMap);
    const data = Object.values(categoryMap);
    
    // Define colors for each category
    const backgroundColors = [
      '#1976d2', // Blue
      '#f44336', // Red
      '#4caf50', // Green
      '#ff9800', // Orange
      '#9c27b0', // Purple
      '#607d8b', // Blue Grey
      '#00bcd4', // Cyan
    ];
    
    return {
      labels,
      datasets: [{
        data,
        backgroundColor: backgroundColors.slice(0, labels.length),
        borderWidth: 1
      }]
    };
  };
  
  const chartData = processDataForChart();
  
  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
      }
    }
  };

  return (
    <Box sx={{ height: 300 }}>
      <Pie data={chartData} options={options} />
    </Box>
  );
};

export default CategoryDistributionChart;
