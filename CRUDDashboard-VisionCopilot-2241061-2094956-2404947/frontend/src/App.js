import React, { useState, useEffect } from 'react';
import { Box, Container, Grid, Paper, Typography } from '@mui/material';
import StatCard from './components/StatCard';
import InventoryTable from './components/InventoryTable';
import ItemFormDialog from './components/ItemFormDialog';
import SearchBar from './components/SearchBar';
import ActivityLog from './components/ActivityLog';
import { inventoryService } from './services/inventoryService';
import CategoryDistributionChart from './components/CategoryDistributionChart';

function App() {
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    totalItems: 0,
    lowStockItems: 0,
    outOfStockItems: 0
  });
  const [items, setItems] = useState([]);
  const [filteredItems, setFilteredItems] = useState([]);
  const [recentActivities, setRecentActivities] = useState([]);
  const [openDialog, setOpenDialog] = useState(false);
  const [currentItem, setCurrentItem] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  
  // Load dashboard data
  const loadDashboardData = async () => {
    try {
      setLoading(true);
      const data = await inventoryService.getDashboardStats();
      setStats({
        totalItems: data.totalItems,
        lowStockItems: data.lowStockItems,
        outOfStockItems: data.outOfStockItems
      });
      setItems(data.items);
      setFilteredItems(data.items);
      setRecentActivities(data.recentActivities || []);
    } catch (error) {
      console.error('Error loading dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };
  
  useEffect(() => {
    loadDashboardData();
  }, []);
  
  // Handle search
  const handleSearch = (query) => {
    setSearchQuery(query);
    if (!query.trim()) {
      setFilteredItems(items);
    } else {
      const filtered = items.filter(item => 
        item.name.toLowerCase().includes(query.toLowerCase()) ||
        item.sku.toLowerCase().includes(query.toLowerCase()) ||
        item.category.toLowerCase().includes(query.toLowerCase())
      );
      setFilteredItems(filtered);
    }
  };
  
  // Open dialog for creating a new item
  const handleAddItem = () => {
    setCurrentItem(null);
    setOpenDialog(true);
  };
  
  // Open dialog for editing an existing item
  const handleEditItem = (item) => {
    setCurrentItem(item);
    setOpenDialog(true);
  };
  
  // Handle dialog close
  const handleCloseDialog = () => {
    setOpenDialog(false);
  };
  
  // Handle save (create or update)
  const handleSaveItem = async (item) => {
    try {
      if (item.id) {
        // Update
        await inventoryService.updateItem(item.id, item);
      } else {
        // Create
        await inventoryService.createItem(item);
      }
      
      handleCloseDialog();
      loadDashboardData();
    } catch (error) {
      console.error('Error saving item:', error);
    }
  };
  
  // Handle delete
  const handleDeleteItem = async (id) => {
    try {
      await inventoryService.deleteItem(id);
      loadDashboardData();
    } catch (error) {
      console.error('Error deleting item:', error);
    }
  };
  
  return (
    <Container maxWidth="xl" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Inventory Dashboard
        </Typography>
      </Box>
      
      {/* Stats Section */}
      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} md={4}>
          <StatCard 
            title="Total Items" 
            value={stats.totalItems} 
            color="#1976d2"
          />
        </Grid>
        <Grid item xs={12} md={4}>
          <StatCard 
            title="Low on Stock" 
            value={stats.lowStockItems} 
            color="#ff9800"
          />
        </Grid>
        <Grid item xs={12} md={4}>
          <StatCard 
            title="Out of Stock" 
            value={stats.outOfStockItems} 
            color="#f44336"
          />
        </Grid>
      </Grid>
      
      {/* Search Bar and Table Section */}
      <Grid container spacing={3}>
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 2, mb: 3 }}>
            <SearchBar 
              searchQuery={searchQuery}
              onSearch={handleSearch}
              onAddItem={handleAddItem}
            />
          </Paper>
          
          <Paper sx={{ p: 2 }}>
            <InventoryTable 
              items={filteredItems} 
              loading={loading}
              onEditItem={handleEditItem}
              onDeleteItem={handleDeleteItem}
            />
          </Paper>
        </Grid>
        
        <Grid item xs={12} md={4}>
          {/* Activity Log */}
          <Paper sx={{ p: 2, mb: 3 }}>
            <ActivityLog activities={recentActivities} />
          </Paper>
          
          {/* Chart */}
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Items by Category
            </Typography>
            <CategoryDistributionChart items={items} />
          </Paper>
        </Grid>
      </Grid>
      
      {/* Item Dialog */}
      <ItemFormDialog 
        open={openDialog}
        onClose={handleCloseDialog}
        onSave={handleSaveItem}
        item={currentItem}
      />
    </Container>
  );
}

export default App;
