USE ecommerce;

CREATE TABLE dim_customer (
    id Int64,
    fullname String,
    phone String,
    age Int32,
    date_of_birth DateTime,
    date_join DateTime,
    PRIMARY KEY (id)
) ENGINE = ReplacingMergeTree()
ORDER BY id;

CREATE TABLE dim_vendor (
    id Int64,
    name String,
    phone String,
    date_join Date,
    PRIMARY KEY (id)
) ENGINE = ReplacingMergeTree()
ORDER BY id;

CREATE TABLE dim_product (
    product_id Int32,
    product_name String,
    brand String,
    original_price Decimal(12, 2),
    stock Int32,
    category_id Int32,
    vendor_id Int64,
    date_created Date,
    PRIMARY KEY (product_id)
) ENGINE = ReplacingMergeTree()
ORDER BY product_id;

CREATE TABLE dim_discount (
    discount_id Int32,
    name String,
    discount_percent Decimal(5, 2),
    PRIMARY KEY (discount_id)
) ENGINE = ReplacingMergeTree()
ORDER BY discount_id;

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

CREATE TABLE fact_sales
(
    order_product_id Int32,
    order_id Int32,
    product_id Int32,
    customer_id Int64,
    quantity Int32,
    price Decimal(12,2),
    order_total Decimal(12,2),
    order_status String,
    order_date Date,
    version UInt64
)
ENGINE = ReplacingMergeTree(version)
ORDER BY order_product_id;

CREATE TABLE fact_review
(
    review_id Int32,
    product_id Int32,
    customer_id Int64,
    review_rating Int32,
    review_content String,
    review_date Date,
    version UInt64
)
ENGINE = ReplacingMergeTree(version)
ORDER BY review_id;