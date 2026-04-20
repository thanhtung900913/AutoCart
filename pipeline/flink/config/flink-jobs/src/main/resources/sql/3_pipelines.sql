INSERT INTO sink_dim_customer
SELECT
  id,
  fullname,
  phone,
  age,
  CAST(REPLACE(SUBSTRING(after.date_of_birth, 1, 19), 'T', ' ') AS TIMESTAMP(3)),
  CAST(REPLACE(SUBSTRING(after.date_join, 1, 19), 'T', ' ') AS TIMESTAMP(3))
FROM src_customers;

INSERT INTO sink_dim_vendor
SELECT
  id,
  name,
  phone,
  CAST(TO_DATE(FROM_UNIXTIME(date_join * 86400)) AS DATE)
FROM src_vendors;

INSERT INTO sink_dim_product
SELECT
  product_id,
  product_name,
  brand,
  original_price,
  stock,
  category_id,
  vendor_id,
  CAST(TO_DATE(FROM_UNIXTIME(date_created * 86400)) AS DATE)
FROM src_products;

INSERT INTO sink_dim_discount
SELECT
  discount_id,
  name,
  discount_percent
FROM src_discounts;

INSERT INTO sink_fact_review
SELECT
  review_id,
  product_id,
  customer_id,
  review_rating,
  review_content,
  CAST(TO_DATE(FROM_UNIXTIME(review_date * 86400)) AS DATE),
  CAST(UNIX_TIMESTAMP() * 1000 AS BIGINT)
FROM src_reviews;

INSERT INTO sink_fact_sales
SELECT
  op.order_product_id,
  o.order_id,
  op.product_id,
  o.customer_id,
  op.quantity,
  op.price,
  o.order_total,
  o.order_status,
  CAST(TO_DATE(FROM_UNIXTIME(o.order_date * 86400)) AS DATE),
  CAST(UNIX_TIMESTAMP() * 1000 AS BIGINT)
FROM src_order_products op
JOIN src_orders o
ON op.order_id = o.order_id;