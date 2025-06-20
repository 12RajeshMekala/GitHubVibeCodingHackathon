import React from 'react';
import { Box, Typography, List, ListItem, ListItemText, Divider } from '@mui/material';

const ActivityLog = ({ activities }) => {
  if (!activities || activities.length === 0) {
    return (
      <Box>
        <Typography variant="h6" gutterBottom>
          Recent Activity
        </Typography>
        <Typography variant="body2" color="text.secondary">
          No recent activities
        </Typography>
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h6" gutterBottom>
        Recent Activity
      </Typography>
      <List dense>
        {activities.map((activity, index) => (
          <React.Fragment key={activity.id || index}>
            <ListItem alignItems="flex-start" sx={{ py: 0.5 }}>
              <ListItemText
                primary={activity.action}
                secondary={new Date(activity.timestamp).toLocaleString()}
              />
            </ListItem>
            {index < activities.length - 1 && <Divider component="li" />}
          </React.Fragment>
        ))}
      </List>
    </Box>
  );
};

export default ActivityLog;
