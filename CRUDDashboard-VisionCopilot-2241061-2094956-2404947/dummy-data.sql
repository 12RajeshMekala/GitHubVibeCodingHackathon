-- Dummy data for Inventory Management System

-- Store Managers
INSERT INTO store_managers (name, email, password_hash) VALUES
  ('John Smith', 'john.smith@example.com', '$2a$10$BxeGwUGzd.HJXgN9CC1.XeYp1JNZ5JVEYJm5.7BjTzD0xagbIUx.K'), -- password: password123
  ('Emma Johnson', 'emma.johnson@example.com', '$2a$10$Ps9xfzEkNZHQyvCKFTc7weBB0TeQTn1/pZ8oQIUbEwPcl4K/RKeryI'), -- password: secure456
  ('Michael Chen', 'michael.chen@example.com', '$2a$10$DfGt.HrAI89KQO5SLk/DL.hdnlRbVhLBLJNQNJkN9wKuR1vG9NVSO'), -- password: manager789
  ('Sarah Garcia', 'sarah.garcia@example.com', '$2a$10$1XrDsKLNGCsXxw4OJlVhguRgJg0T1RXXRt/7jE53QvR.DEZQan5gq'); -- password: store123

-- Inventory Items
-- Note: Make sure the manager_id values correspond to existing store_managers ids
INSERT INTO inventory_items (manager_id, name, sku, category, price, quantity, image_url) VALUES
  -- Electronics
  (1, 'Wireless Headphones', 'EL-WH-001', 'Electronics', 129.99, 25, 'https://example.com/images/headphones.jpg'),
  (1, 'Smart Watch', 'EL-SW-002', 'Electronics', 249.99, 15, 'https://example.com/images/smartwatch.jpg'),
  (2, 'Bluetooth Speaker', 'EL-BS-003', 'Electronics', 89.99, 30, 'https://example.com/images/speaker.jpg'),
  (3, 'Tablet', 'EL-TB-004', 'Electronics', 349.99, 10, 'https://example.com/images/tablet.jpg'),
  (4, 'Digital Camera', 'EL-DC-005', 'Electronics', 599.99, 8, 'https://example.com/images/camera.jpg'),
  
  -- Clothing
  (1, 'Men\'s T-Shirt', 'CL-MT-001', 'Clothing', 19.99, 50, 'https://example.com/images/tshirt.jpg'),
  (2, 'Women\'s Jeans', 'CL-WJ-002', 'Clothing', 59.99, 35, 'https://example.com/images/jeans.jpg'),
  (2, 'Unisex Hoodie', 'CL-UH-003', 'Clothing', 45.99, 40, 'https://example.com/images/hoodie.jpg'),
  (3, 'Athletic Shoes', 'CL-AS-004', 'Clothing', 89.99, 25, 'https://example.com/images/shoes.jpg'),
  (4, 'Winter Jacket', 'CL-WJ-005', 'Clothing', 129.99, 20, 'https://example.com/images/jacket.jpg'),
  
  -- Home & Kitchen
  (1, 'Coffee Maker', 'HK-CM-001', 'Home & Kitchen', 79.99, 15, 'https://example.com/images/coffeemaker.jpg'),
  (2, 'Blender', 'HK-BL-002', 'Home & Kitchen', 69.99, 12, 'https://example.com/images/blender.jpg'),
  (3, 'Cookware Set', 'HK-CS-003', 'Home & Kitchen', 199.99, 8, 'https://example.com/images/cookware.jpg'),
  (3, 'Toaster', 'HK-TO-004', 'Home & Kitchen', 49.99, 20, 'https://example.com/images/toaster.jpg'),
  (4, 'Stand Mixer', 'HK-SM-005', 'Home & Kitchen', 249.99, 7, 'https://example.com/images/mixer.jpg'),
  
  -- Books
  (1, 'Business Strategy Guide', 'BK-BS-001', 'Books', 24.99, 30, 'https://example.com/images/business-book.jpg'),
  (2, 'Fantasy Novel', 'BK-FN-002', 'Books', 14.99, 45, 'https://example.com/images/fantasy-book.jpg'),
  (3, 'Cookbook', 'BK-CB-003', 'Books', 29.99, 25, 'https://example.com/images/cookbook.jpg'),
  (4, 'Tech Reference', 'BK-TR-004', 'Books', 39.99, 20, 'https://example.com/images/tech-book.jpg'),
  (1, 'Self-Help Guide', 'BK-SH-005', 'Books', 19.99, 35, 'https://example.com/images/selfhelp-book.jpg'),

  -- Toys & Games
  (2, 'Board Game', 'TG-BG-001', 'Toys & Games', 34.99, 18, 'https://example.com/images/boardgame.jpg'),
  (3, 'Action Figure', 'TG-AF-002', 'Toys & Games', 19.99, 25, 'https://example.com/images/figure.jpg'),
  (4, 'Building Blocks', 'TG-BB-003', 'Toys & Games', 39.99, 15, 'https://example.com/images/blocks.jpg'),
  (1, 'Puzzle', 'TG-PZ-004', 'Toys & Games', 14.99, 22, 'https://example.com/images/puzzle.jpg'),
  (2, 'Remote Control Car', 'TG-RC-005', 'Toys & Games', 49.99, 10, 'https://example.com/images/rccar.jpg');

-- Activity Log
-- Add some sample activity logs
INSERT INTO activity_log (manager_id, action, item_id, item_name) VALUES
  (1, 'ITEM_CREATED', 1, 'Wireless Headphones'),
  (1, 'ITEM_UPDATED', 1, 'Wireless Headphones'),
  (2, 'ITEM_CREATED', 3, 'Bluetooth Speaker'),
  (3, 'ITEM_CREATED', 4, 'Tablet'),
  (4, 'ITEM_CREATED', 5, 'Digital Camera'),
  (2, 'ITEM_UPDATED', 7, 'Women''s Jeans'),
  (3, 'ITEM_UPDATED', 13, 'Cookware Set'),
  (1, 'INVENTORY_COUNT', 6, 'Men''s T-Shirt'),
  (4, 'ITEM_CREATED', 15, 'Stand Mixer'),
  (1, 'ITEM_UPDATED', 16, 'Business Strategy Guide'),
  (3, 'INVENTORY_COUNT', 22, 'Action Figure'),
  (2, 'ITEM_CREATED', 25, 'Remote Control Car');
