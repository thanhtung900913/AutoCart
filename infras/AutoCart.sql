-- ==========================================
-- SCRIPT KHỞI TẠO DATABASE CHO DỰ ÁN AUTO_CART
-- Dành cho hệ quản trị cơ sở dữ liệu: PostgreSQL
-- Phiên bản: Chuẩn SQL:2003 (Identity Columns)
-- ==========================================

-- 1. ĐỊA CHỈ (GEOGRAPHY) - Cấp độ độc lập
CREATE TABLE provinces (
    province_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    province_name VARCHAR(255) NOT NULL,
    province_name_en VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE districts (
    district_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    district_name VARCHAR(255) NOT NULL,
    district_name_en VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    province_id INTEGER NOT NULL REFERENCES provinces(province_id)
);

CREATE TABLE wards (
    ward_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ward_name VARCHAR(255) NOT NULL,
    ward_name_en VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    district_id INTEGER NOT NULL REFERENCES districts(district_id),
    province_id INTEGER NOT NULL REFERENCES provinces(province_id)
);

CREATE TABLE addresses (
    address_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    address_detail VARCHAR(255),
    district_id INTEGER REFERENCES districts(district_id),
    province_id INTEGER REFERENCES provinces(province_id),
    ward_id INTEGER REFERENCES wards(ward_id)
);

-- 2. RBAC & PHÂN QUYỀN CƠ BẢN
CREATE TABLE roles (
    role_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    role_name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE permissions (
    permission_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    permission_name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE role_permissions (
    role_id INTEGER NOT NULL REFERENCES roles(role_id),
    permission_id INTEGER NOT NULL REFERENCES permissions(permission_id),
    PRIMARY KEY (role_id, permission_id) -- Composite Key
);

-- 3. CÁC BẢNG ĐỘC LẬP (GIỎ HÀNG, KHUYẾN MÃI, DANH MỤC)
CREATE TABLE carts (
    cart_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    grand_total DECIMAL(12, 2) NOT NULL DEFAULT 0,
    items_total INTEGER NOT NULL CHECK (items_total >= 0) DEFAULT 0
);

CREATE TABLE discounts (
    discount_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    discount_percent DECIMAL(5, 2) NOT NULL
);

CREATE TABLE categories (
    category_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    category_img_url VARCHAR(255) NOT NULL
);

-- 4. TÀI KHOẢN (USERS) & TOKEN
CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    last_login TIMESTAMP,
    email VARCHAR(255) UNIQUE NOT NULL,
    address_id INTEGER UNIQUE REFERENCES addresses(address_id)
);

CREATE TABLE refresh_tokens (
    token_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    token VARCHAR(500) UNIQUE NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_revoked BOOLEAN DEFAULT FALSE
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id),
    role_id INTEGER NOT NULL REFERENCES roles(role_id),
    PRIMARY KEY (user_id, role_id) -- Composite Key
);

-- 5. THANH TOÁN & PROFILES (CUSTOMERS, VENDORS)
CREATE TABLE payment_methods (
    payment_method_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    provider VARCHAR(100) NOT NULL, -- Ví dụ: Visa, VNPay
    masked_account_number VARCHAR(50) NOT NULL,
    card_holder_name VARCHAR(255),
    expires_at DATE,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vendors (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    phone VARCHAR(20),
    description TEXT,
    logo_url VARCHAR(255),
    date_join DATE NOT NULL,
    user_id BIGINT UNIQUE NOT NULL REFERENCES users(id)
);

CREATE TABLE vendor_payout_accounts (
    payout_account_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vendor_id BIGINT UNIQUE NOT NULL REFERENCES vendors(id),
    bank_name VARCHAR(255) NOT NULL,
    account_number VARCHAR(100) NOT NULL,
    account_holder_name VARCHAR(255) NOT NULL,
    branch_name VARCHAR(255)
);

CREATE TABLE customers (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    phone VARCHAR(20),
    fullname VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    age SMALLINT NOT NULL,
    avatar_url VARCHAR(255),
    date_join DATE NOT NULL,
    recommend_product_ids INTEGER[] NOT NULL,
    cart_id INTEGER UNIQUE REFERENCES carts(cart_id),
    user_id BIGINT UNIQUE REFERENCES users(id)
);

-- 6. SẢN PHẨM & MỐI QUAN HỆ VỚI SẢN PHẨM
CREATE TABLE products (
    product_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    original_price DECIMAL(12, 2) NOT NULL,
    stock INTEGER NOT NULL CHECK (stock >= 0),
    brand VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    product_description TEXT NOT NULL,
    date_created DATE NOT NULL,
    ratings DECIMAL(3, 2) NOT NULL DEFAULT 0,
    date_add DATE NOT NULL,
    category_id INTEGER NOT NULL REFERENCES categories(category_id),
    vendor_id BIGINT NOT NULL REFERENCES vendors(id)
);

CREATE TABLE product_images (
    product_image_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL,
    is_main BOOLEAN NOT NULL DEFAULT FALSE,
    product_id INTEGER NOT NULL REFERENCES products(product_id)
);

CREATE TABLE cart_products (
    cart_product_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    cart_id INTEGER NOT NULL REFERENCES carts(cart_id),
    product_id INTEGER NOT NULL REFERENCES products(product_id)
);

CREATE TABLE order_product_discounts (
    product_discount_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    discount_id INTEGER NOT NULL REFERENCES discounts(discount_id),
    product_id INTEGER NOT NULL REFERENCES products(product_id)
);

-- 7. ĐƠN HÀNG, TRANSACTIONS & ĐÁNH GIÁ (ORDERS & REVIEWS)
CREATE TABLE orders (
    order_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_number VARCHAR(100) UNIQUE NOT NULL,
    shipping_date DATE NOT NULL,
    order_date DATE NOT NULL,
    order_status VARCHAR(50) NOT NULL,
    order_total DECIMAL(12, 2) NOT NULL,
    address_id INTEGER REFERENCES addresses(address_id),
    payment_method_id BIGINT REFERENCES payment_methods(payment_method_id),
    customer_id BIGINT NOT NULL REFERENCES customers(id)
);

CREATE TABLE order_products (
    order_product_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    quantity INTEGER NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    order_id INTEGER NOT NULL REFERENCES orders(order_id),
    product_id INTEGER NOT NULL REFERENCES products(product_id)
);

CREATE TABLE transactions (
    transaction_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transation_date TIMESTAMP NOT NULL,
    total_money DECIMAL(12, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    order_id INTEGER NOT NULL REFERENCES orders(order_id)
);

CREATE TABLE reviews (
    review_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    review_rating INTEGER NOT NULL CHECK (review_rating BETWEEN 1 AND 5),
    review_date DATE NOT NULL,
    review_content TEXT NOT NULL,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    product_id INTEGER NOT NULL REFERENCES products(product_id)
);