-- ====================================================================
-- SCRIPT NẠP DỮ LIỆU MẪU (SEEDING DATA) CHO AUTO_CART
-- Lưu ý: Chạy script này SAU KHI đã tạo cấu trúc bảng.
-- Các ID tự tăng sẽ mặc định được hiểu là 1, 2, 3... theo thứ tự Insert
-- ====================================================================

-- 1. NẠP DỮ LIỆU ĐỊA CHỈ (Provinces -> Districts -> Wards -> Addresses)
INSERT INTO provinces (province_name, province_name_en, type) VALUES 
('Hà Nội', 'Ha Noi', 'Thành phố Trung ương'),  -- Sẽ nhận ID = 1
('Hồ Chí Minh', 'Ho Chi Minh', 'Thành phố Trung ương'); -- Sẽ nhận ID = 2

INSERT INTO districts (district_name, district_name_en, type, province_id_id) VALUES 
('Quận Cầu Giấy', 'Cau Giay District', 'Quận', 1), -- ID = 1 (Thuộc HN)
('Quận 1', 'District 1', 'Quận', 2);               -- ID = 2 (Thuộc HCM)

INSERT INTO wards (ward_name, ward_name_en, type, district_id_id, province_id_id) VALUES 
('Phường Dịch Vọng', 'Dich Vong Ward', 'Phường', 1, 1), -- ID = 1
('Phường Bến Nghé', 'Ben Nghe Ward', 'Phường', 2, 2);   -- ID = 2

INSERT INTO addresses (address_detail, district_id, province_id, ward_id) VALUES 
('Số 1, Ngõ 2, Xuân Thủy', 1, 1, 1), -- Address ID = 1 (Dành cho Vendor)
('Tòa nhà Bitexco, Nguyễn Huệ', 2, 2, 2); -- Address ID = 2 (Dành cho Customer)


-- 2. NẠP DỮ LIỆU RBAC (Roles & Permissions)
INSERT INTO roles (role_name, description) VALUES 
('ADMIN', 'Quản trị viên toàn quyền hệ thống'), -- Role ID = 1
('VENDOR', 'Người bán hàng / Chủ shop'),        -- Role ID = 2
('CUSTOMER', 'Khách hàng mua sắm');             -- Role ID = 3

INSERT INTO permissions (permission_name, description) VALUES 
('CREATE_PRODUCT', 'Quyền đăng sản phẩm mới'),  -- Perm ID = 1
('PROCESS_ORDER', 'Quyền xử lý đơn hàng'),      -- Perm ID = 2
('ADD_TO_CART', 'Quyền thêm vào giỏ hàng');     -- Perm ID = 3

INSERT INTO role_permissions (role_id, permission_id) VALUES 
(2, 1), -- Vendor được Create Product
(2, 2), -- Vendor được Process Order
(3, 3); -- Customer được Add to cart


-- 3. NẠP DỮ LIỆU CƠ BẢN (Categories & Carts)
INSERT INTO categories (category_name, slug, category_img_url) VALUES 
('Điện thoại & Phụ kiện', 'dien-thoai-phu-kien', 'https://example.com/phone.png'), -- ID = 1
('Thời trang Nam', 'thoi-trang-nam', 'https://example.com/fashion.png');           -- ID = 2

INSERT INTO carts (grand_total, items_total) VALUES 
(0.00, 0), -- Cart ID = 1 (Chuẩn bị gán cho Customer 1)
(0.00, 0); -- Cart ID = 2 (Chuẩn bị gán cho Customer 2)


-- 4. NẠP DỮ LIỆU TÀI KHOẢN (Users -> User_Roles)
-- Mật khẩu ở đây giả định đã được băm (hash) bằng BCrypt trong thực tế
INSERT INTO users (password, email, address_id, last_login) VALUES 
('$2a$10$xyzHashedPassword1', 'vendor_tech@gmail.com', 1, CURRENT_TIMESTAMP), -- User ID = 1
('$2a$10$xyzHashedPassword2', 'khachhang_vip@gmail.com', 2, CURRENT_TIMESTAMP); -- User ID = 2

INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 2), -- User 1 là VENDOR
(2, 3); -- User 2 là CUSTOMER


-- 5. NẠP DỮ LIỆU HỒ SƠ (Vendors & Customers)
INSERT INTO vendors (name, phone, description, logo_url, date_join, user_id) VALUES 
('Tech Store Chính Hãng', '0901234567', 'Chuyên bán đồ công nghệ uy tín', 'https://example.com/tech_logo.png', CURRENT_DATE, 1); 
-- Vendor ID = 1 (Thuộc User 1)

INSERT INTO customers (phone, fullname, date_of_birth, age, avatar_url, date_join, recommend_product_ids, cart_id, user_id) VALUES 
('0987654321', 'Nguyễn Văn Khách', '1998-05-20', 26, 'https://example.com/avatar.png', CURRENT_DATE, '{}', 1, 2); 
-- Customer ID = 1 (Thuộc User 2, cầm Cart 1)


-- 6. NẠP DỮ LIỆU TÀI KHOẢN NGÂN HÀNG & THANH TOÁN
INSERT INTO vendor_payout_accounts (vendor_id, bank_name, account_number, account_holder_name, branch_name) VALUES 
(1, 'Techcombank', '1903123456789', 'CTY TNHH TECH STORE', 'Chi nhánh Cầu Giấy');

INSERT INTO payment_methods (user_id, provider, masked_account_number, card_holder_name, expires_at, is_default) VALUES 
(2, 'Visa', '****4242', 'NGUYEN VAN KHACH', '2028-12-31', TRUE);


-- 7. NẠP DỮ LIỆU SẢN PHẨM & ẢNH SẢN PHẨM
INSERT INTO products (product_name, original_price, stock, brand, slug, product_description, date_created, ratings, date_add, category_id, vendor_id) VALUES 
('iPhone 15 Pro Max 256GB', 29990000.00, 50, 'Apple', 'iphone-15-pro-max', 'Điện thoại flagship mới nhất của Apple', CURRENT_DATE, 4.9, CURRENT_DATE, 1, 1), -- Product ID = 1
('Tai nghe AirPods Pro 2', 5500000.00, 100, 'Apple', 'airpods-pro-2', 'Tai nghe chống ồn chủ động', CURRENT_DATE, 4.8, CURRENT_DATE, 1, 1); -- Product ID = 2

INSERT INTO product_images (image_url, is_main, product_id_id) VALUES 
('https://example.com/ip15_main.png', TRUE, 1),
('https://example.com/ip15_side.png', FALSE, 1),
('https://example.com/airpods_main.png', TRUE, 2);