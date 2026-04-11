USE ecommerce;
-- Bảng Chiều: Khách hàng (Từ bảng customers)
CREATE TABLE dim_customer (
    id Int64, -- Khớp với int8 của Postgres
    fullname String,
    phone String,
    age Int32,
    date_of_birth DateTime,
    date_join DateTime,
    PRIMARY KEY (id)
) ENGINE = ReplacingMergeTree()
ORDER BY id;

-- Bảng Chiều: Người bán (Từ bảng vendors)
CREATE TABLE dim_vendor (
    id Int64,
    name String,
    phone String,
    date_join Date,
    PRIMARY KEY (id)
) ENGINE = ReplacingMergeTree()
ORDER BY id;

-- Bảng Chiều: Sản phẩm (Từ bảng products + categories)
CREATE TABLE dim_product (
    product_id Int32,
    product_name String,
    brand String,
    original_price Decimal(12, 2), -- Giữ nguyên độ chính xác tiền tệ
    stock Int32,
    category_id Int32, -- Sẽ được Flink enrich tên danh mục vào đây nếu cần
    vendor_id Int64,
    date_created Date,
    PRIMARY KEY (product_id)
) ENGINE = ReplacingMergeTree()
ORDER BY product_id;

-- Bảng Chiều: Khuyến mãi/Giảm giá (Từ bảng discounts)
CREATE TABLE dim_discount (
    discount_id Int32,
    name String,
    discount_percent Decimal(5, 2),
    PRIMARY KEY (discount_id)
) ENGINE = ReplacingMergeTree()
ORDER BY discount_id;

-- Bảng Chiều: Thời gian (Tự gen ra để phân tích theo quý, năm)
CREATE TABLE dim_date (
    id Int32,
    full_date Date,
    day UInt8,
    month UInt8,
    year UInt16,
    quarter UInt8,
    day_of_week UInt8,
    week_of_year UInt8,
    is_weekend Boolean,
    PRIMARY KEY (id)
) ENGINE = ReplacingMergeTree()
PARTITION BY toYYYYMM(full_date)
ORDER BY id;


-- ==============================================================================
-- 2. CÁC BẢNG FACT (SỰ KIỆN/GIAO DỊCH THỰC TẾ)
-- Sử dụng MergeTree() và BẮT BUỘC Partition theo Tháng để tránh sập I/O ổ cứng
-- ==============================================================================

-- Bảng Fact: Doanh thu bán hàng (Kết hợp từ order_products và orders)
-- Mỗi dòng tương ứng với 1 sản phẩm được bán ra trong 1 đơn hàng
CREATE TABLE fact_sales (
    order_product_id Int32,
    order_id Int32,
    product_id Int32,
    customer_id Int64,
    quantity Int32,
    price Decimal(12, 2),          -- Giá lúc mua của sản phẩm đó
    order_total Decimal(12, 2),    -- Tổng giá trị của cả đơn hàng
    order_status String,
    order_date Date,
    PRIMARY KEY (order_product_id)
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(order_date) -- Gom thư mục vật lý theo từng tháng (VD: 202604)
ORDER BY (order_date, product_id, customer_id); -- Optimize truy vấn thời gian và sản phẩm

-- Bảng Fact: Đánh giá sản phẩm (Từ bảng reviews)
CREATE TABLE fact_review (
    review_id Int32,
    product_id Int32,
    customer_id Int64,
    review_rating Int32,
    review_content String,
    review_date Date,
    PRIMARY KEY (review_id)
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(review_date)
ORDER BY (review_date, product_id, review_rating);