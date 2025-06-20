import React from 'react';
import { Paper, Typography, Box } from '@mui/material';

const StatCard = ({ title, value, color }) => {
  return (
    <Paper 
      sx={{ 
        p: 2, 
        height: '100%',
        display: 'flex', 
        flexDirection: 'column', 
        justifyContent: 'center',
        borderTop: `4px solid ${color}`
      }}
    >
      <Typography variant="h6" color="text.secondary" gutterBottom>
        {title}
      </Typography>
      <Box sx={{ display: 'flex', alignItems: 'center' }}>
        <Typography component="p" variant="h3" color="text.primary">
          {value}
        </Typography>
      </Box>
    </Paper>
  );
};

export default StatCard;
