-- Source: customers
CREATE TABLE src_customers (
  after ROW<
    id BIGINT,
    fullname STRING,
    phone STRING,
    age INT,
    date_of_birth STRING,
    date_join STRING
  >,
  ts_ms BIGINT
) WITH (
  'connector' = 'kafka',
  'topic' = 'autocart_db_10.public.customers',
  'properties.bootstrap.servers' = '${KAFKA_BOOTSTRAP_SERVERS}',
  'properties.group.id' = 'dwh-source-group',
  'scan.startup.mode' = 'earliest-offset',
  'format' = 'json'
);

-- Source: vendors
CREATE TABLE src_vendors (
  after ROW<
    id BIGINT,
    name STRING,
    phone STRING,
    date_join INT
  >,
  ts_ms BIGINT
) WITH (
  'connector' = 'kafka',
  'topic' = 'autocart_db_10.public.vendors',
  'properties.bootstrap.servers' = '${KAFKA_BOOTSTRAP_SERVERS}',
  'properties.group.id' = 'dwh-source-group',
  'scan.startup.mode' = 'earliest-offset',
  'format' = 'json'
);

-- Source: products
CREATE TABLE src_products (
  after ROW<
    product_id INT,
    product_name STRING,
    brand STRING,
    original_price DECIMAL(12, 2),
    stock INT,
    category_id INT,
    vendor_id BIGINT,
    date_created INT
  >,
  ts_ms BIGINT
) WITH (
  'connector' = 'kafka',
  'topic' = 'autocart_db_10.public.products',
  'properties.bootstrap.servers' = '${KAFKA_BOOTSTRAP_SERVERS}',
  'properties.group.id' = 'dwh-source-group',
  'scan.startup.mode' = 'earliest-offset',
  'format' = 'json'
);

-- Source: discounts
CREATE TABLE src_discounts (
  after ROW<
    discount_id INT,
    name STRING,
    discount_percent DECIMAL(5, 2)
  >,
  ts_ms BIGINT
) WITH (
  'connector' = 'kafka',
  'topic' = 'autocart_db_10.public.discounts',
  'properties.bootstrap.servers' = '${KAFKA_BOOTSTRAP_SERVERS}',
  'properties.group.id' = 'dwh-source-group',
  'scan.startup.mode' = 'earliest-offset',
  'format' = 'json'
);

-- Source: reviews
CREATE TABLE src_reviews (
  after ROW<
    review_id INT,
    product_id INT,
    customer_id BIGINT,
    review_rating INT,
    review_content STRING,
    review_date INT
  >,
  ts_ms BIGINT
) WITH (
  'connector' = 'kafka',
  'topic' = 'autocart_db_10.public.reviews',
  'properties.bootstrap.servers' = '${KAFKA_BOOTSTRAP_SERVERS}',
  'properties.group.id' = 'dwh-source-group',
  'scan.startup.mode' = 'earliest-offset',
  'format' = 'json'
);

-- Source: orders
CREATE TABLE src_orders (
  after ROW<
    order_id INT,
    customer_id BIGINT,
    order_total DECIMAL(12, 2),
    order_status STRING,
    order_date INT
  >,
  ts_ms BIGINT
) WITH (
  'connector' = 'kafka',
  'topic' = 'autocart_db_10.public.orders',
  'properties.bootstrap.servers' = '${KAFKA_BOOTSTRAP_SERVERS}',
  'properties.group.id' = 'dwh-source-group',
  'scan.startup.mode' = 'earliest-offset',
  'format' = 'json'
);

-- Source: order_products
CREATE TABLE src_order_products (
  after ROW<
    order_product_id INT,
    order_id INT,
    product_id INT,
    quantity INT,
    price DECIMAL(12, 2)
  >,
  ts_ms BIGINT
) WITH (
  'connector' = 'kafka',
  'topic' = 'autocart_db_10.public.order_products',
  'properties.bootstrap.servers' = '${KAFKA_BOOTSTRAP_SERVERS}',
  'properties.group.id' = 'dwh-source-group',
  'scan.startup.mode' = 'earliest-offset',
  'format' = 'json'
);