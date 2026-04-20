CREATE TABLE sink_dim_customer (
  id BIGINT,
  fullname STRING,
  phone STRING,
  age INT,
  date_of_birth TIMESTAMP(3),
  date_join TIMESTAMP(3),
  PRIMARY KEY (id) NOT ENFORCED
) WITH (
  'connector' = 'clickhouse',
  'url' = '${CLICKHOUSE_URL}',
  'database-name' = 'ecommerce',
  'table-name' = 'dim_customer',
  'username' = '${CLICKHOUSE_USER}',
  'password' = '${CLICKHOUSE_PASSWORD}',
  'sink.batch-size' = '1000',
  'sink.flush-interval' = '1000',
  'sink.max-retries' = '5'
);

CREATE TABLE sink_dim_vendor (
  id BIGINT,
  name STRING,
  phone STRING,
  date_join DATE,
  PRIMARY KEY (id) NOT ENFORCED
) WITH (
  'connector' = 'clickhouse',
  'url' = '${CLICKHOUSE_URL}',
  'database-name' = 'ecommerce',
  'table-name' = 'dim_vendor',
  'username' = '${CLICKHOUSE_USER}',
  'password' = '${CLICKHOUSE_PASSWORD}',
  'sink.batch-size' = '1000',
  'sink.flush-interval' = '1000',
  'sink.max-retries' = '5'
);

CREATE TABLE sink_dim_product (
  product_id INT,
  product_name STRING,
  brand STRING,
  original_price DECIMAL(12,2),
  stock INT,
  category_id INT,
  vendor_id BIGINT,
  date_created DATE,
  PRIMARY KEY (product_id) NOT ENFORCED
) WITH (
  'connector' = 'clickhouse',
  'url' = '${CLICKHOUSE_URL}',
  'database-name' = 'ecommerce',
  'table-name' = 'dim_product',
  'username' = '${CLICKHOUSE_USER}',
  'password' = '${CLICKHOUSE_PASSWORD}',
  'sink.batch-size' = '1000',
  'sink.flush-interval' = '1000',
  'sink.max-retries' = '5'
);

CREATE TABLE sink_dim_discount (
  discount_id INT,
  name STRING,
  discount_percent DECIMAL(5,2),
  PRIMARY KEY (discount_id) NOT ENFORCED
) WITH (
  'connector' = 'clickhouse',
  'url' = '${CLICKHOUSE_URL}',
  'database-name' = 'ecommerce',
  'table-name' = 'dim_discount',
  'username' = '${CLICKHOUSE_USER}',
  'password' = '${CLICKHOUSE_PASSWORD}',
  'sink.batch-size' = '1000',
  'sink.flush-interval' = '1000',
  'sink.max-retries' = '5'
);

CREATE TABLE sink_fact_review (
  review_id INT,
  product_id INT,
  customer_id BIGINT,
  review_rating INT,
  review_content STRING,
  review_date DATE,
  version BIGINT,
  PRIMARY KEY (review_id) NOT ENFORCED
) WITH (
  'connector' = 'clickhouse',
  'url' = '${CLICKHOUSE_URL}',
  'database-name' = 'ecommerce',
  'table-name' = 'fact_review',
  'username' = '${CLICKHOUSE_USER}',
  'password' = '${CLICKHOUSE_PASSWORD}',
  'sink.batch-size' = '1000',
  'sink.flush-interval' = '1000',
  'sink.max-retries' = '5'
);

CREATE TABLE sink_fact_sales (
  order_product_id INT,
  order_id INT,
  product_id INT,
  customer_id BIGINT,
  quantity INT,
  price DECIMAL(12,2),
  order_total DECIMAL(12,2),
  order_status STRING,
  order_date DATE,
  version BIGINT
) WITH (
  'connector' = 'clickhouse',
  'url' = '${CLICKHOUSE_URL}',
  'database-name' = 'ecommerce',
  'table-name' = 'fact_sales',
  'username' = '${CLICKHOUSE_USER}',
  'password' = '${CLICKHOUSE_PASSWORD}',
  'sink.batch-size' = '1000',
  'sink.flush-interval' = '1000',
  'sink.max-retries' = '5'
);