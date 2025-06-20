import React from 'react';
import { Box, TextField, Button } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import AddIcon from '@mui/icons-material/Add';

const SearchBar = ({ searchQuery, onSearch, onAddItem }) => {
  const handleSearchChange = (event) => {
    onSearch(event.target.value);
  };

  return (
    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
      <Box sx={{ display: 'flex', alignItems: 'center', flexGrow: 1, mr: 2 }}>
        <SearchIcon sx={{ color: 'action.active', mr: 1 }} />
        <TextField
          label="Search Inventory"
          variant="outlined"
          size="small"
          fullWidth
          value={searchQuery}
          onChange={handleSearchChange}
          placeholder="Search by name, SKU, or category..."
        />
      </Box>
      <Button
        variant="contained"
        startIcon={<AddIcon />}
        onClick={onAddItem}
      >
        Add Item
      </Button>
    </Box>
  );
};

export default SearchBar;
