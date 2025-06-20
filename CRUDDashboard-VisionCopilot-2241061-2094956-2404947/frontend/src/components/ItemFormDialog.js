import React, { useState, useEffect } from 'react';
import {
  Dialog, DialogTitle, DialogContent, DialogActions,
  TextField, Button, Grid, FormControl, InputLabel,
  Select, MenuItem, InputAdornment
} from '@mui/material';

const defaultItem = {
  name: '',
  sku: '',
  category: '',
  price: '',
  quantity: '',
  imageUrl: ''
};

const ItemFormDialog = ({ open, onClose, onSave, item }) => {
  const [formData, setFormData] = useState(defaultItem);
  const [errors, setErrors] = useState({});
  
  // Reset form when dialog opens with a new item
  useEffect(() => {
    if (open) {
      setFormData(item ? { ...item } : { ...defaultItem });
      setErrors({});
    }
  }, [open, item]);
  
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // Clear error for this field
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: null
      }));
    }
  };
  
  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.name.trim()) newErrors.name = 'Name is required';
    if (!formData.sku.trim()) newErrors.sku = 'SKU is required';
    if (!formData.category.trim()) newErrors.category = 'Category is required';
    
    const price = parseFloat(formData.price);
    if (isNaN(price) || price < 0) newErrors.price = 'Price must be a positive number';
    
    const quantity = parseInt(formData.quantity);
    if (isNaN(quantity) || quantity < 0) newErrors.quantity = 'Quantity must be a positive integer';
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };
  
  const handleSubmit = () => {
    if (validateForm()) {
      // Convert string values to proper types
      const submitData = {
        ...formData,
        price: parseFloat(formData.price),
        quantity: parseInt(formData.quantity)
      };
      
      onSave(submitData);
    }
  };
  
  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle>
        {item ? 'Edit Item' : 'Add New Item'}
      </DialogTitle>
      <DialogContent>
        <Grid container spacing={2} sx={{ mt: 1 }}>
          <Grid item xs={12} md={6}>
            <TextField
              name="name"
              label="Name"
              fullWidth
              value={formData.name}
              onChange={handleChange}
              error={!!errors.name}
              helperText={errors.name}
              required
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              name="sku"
              label="SKU"
              fullWidth
              value={formData.sku}
              onChange={handleChange}
              error={!!errors.sku}
              helperText={errors.sku}
              required
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <FormControl fullWidth required error={!!errors.category}>
              <InputLabel>Category</InputLabel>
              <Select
                name="category"
                value={formData.category}
                onChange={handleChange}
                label="Category"
              >
                <MenuItem value="Electronics">Electronics</MenuItem>
                <MenuItem value="Clothing">Clothing</MenuItem>
                <MenuItem value="Home & Kitchen">Home & Kitchen</MenuItem>
                <MenuItem value="Office Supplies">Office Supplies</MenuItem>
                <MenuItem value="Other">Other</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              name="price"
              label="Price"
              fullWidth
              type="number"
              value={formData.price}
              onChange={handleChange}
              error={!!errors.price}
              helperText={errors.price}
              required
              InputProps={{
                startAdornment: <InputAdornment position="start">$</InputAdornment>,
              }}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              name="quantity"
              label="Quantity"
              fullWidth
              type="number"
              value={formData.quantity}
              onChange={handleChange}
              error={!!errors.quantity}
              helperText={errors.quantity}
              required
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              name="imageUrl"
              label="Image URL"
              fullWidth
              value={formData.imageUrl || ''}
              onChange={handleChange}
              placeholder="Optional"
            />
          </Grid>
        </Grid>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button onClick={handleSubmit} variant="contained" color="primary">
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ItemFormDialog;
