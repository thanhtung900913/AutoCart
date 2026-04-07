-- public.carts definition

-- Drop table

-- DROP TABLE public.carts;

CREATE TABLE public.carts (
	cart_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	grand_total numeric(12, 2) DEFAULT 0 NOT NULL,
	items_total int4 DEFAULT 0 NOT NULL,
	CONSTRAINT carts_items_total_check CHECK ((items_total >= 0)),
	CONSTRAINT carts_pkey PRIMARY KEY (cart_id)
);


-- public.categories definition

-- Drop table

-- DROP TABLE public.categories;

CREATE TABLE public.categories (
	category_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	category_name varchar(255) NOT NULL,
	slug varchar(255) NOT NULL,
	category_img_url varchar(255) NOT NULL,
	CONSTRAINT categories_pkey PRIMARY KEY (category_id)
);


-- public.discounts definition

-- Drop table

-- DROP TABLE public.discounts;

CREATE TABLE public.discounts (
	discount_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"name" varchar(255) NOT NULL,
	discount_percent numeric(5, 2) NOT NULL,
	CONSTRAINT discounts_pkey PRIMARY KEY (discount_id)
);


-- public.permissions definition

-- Drop table

-- DROP TABLE public.permissions;

CREATE TABLE public.permissions (
	permission_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	permission_name varchar(100) NOT NULL,
	description varchar(255) NULL,
	CONSTRAINT permissions_permission_name_key UNIQUE (permission_name),
	CONSTRAINT permissions_pkey PRIMARY KEY (permission_id)
);


-- public.provinces definition

-- Drop table

-- DROP TABLE public.provinces;

CREATE TABLE public.provinces (
	province_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	province_name varchar(255) NOT NULL,
	province_name_en varchar(255) NOT NULL,
	"type" varchar(255) NOT NULL,
	CONSTRAINT provinces_pkey PRIMARY KEY (province_id)
);


-- public.roles definition

-- Drop table

-- DROP TABLE public.roles;

CREATE TABLE public.roles (
	role_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	role_name varchar(100) NOT NULL,
	description varchar(255) NULL,
	CONSTRAINT roles_pkey PRIMARY KEY (role_id),
	CONSTRAINT roles_role_name_key UNIQUE (role_name)
);


-- public.districts definition

-- Drop table

-- DROP TABLE public.districts;

CREATE TABLE public.districts (
	district_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	district_name varchar(255) NOT NULL,
	district_name_en varchar(255) NOT NULL,
	"type" varchar(255) NOT NULL,
	province_id int4 NOT NULL,
	CONSTRAINT districts_pkey PRIMARY KEY (district_id),
	CONSTRAINT fk_districts_province FOREIGN KEY (province_id) REFERENCES public.provinces(province_id)
);


-- public.role_permissions definition

-- Drop table

-- DROP TABLE public.role_permissions;

CREATE TABLE public.role_permissions (
	role_id int4 NOT NULL,
	permission_id int4 NOT NULL,
	CONSTRAINT role_permissions_pkey PRIMARY KEY (role_id, permission_id),
	CONSTRAINT role_permissions_permission_id_fkey FOREIGN KEY (permission_id) REFERENCES public.permissions(permission_id),
	CONSTRAINT role_permissions_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(role_id)
);


-- public.wards definition

-- Drop table

-- DROP TABLE public.wards;

CREATE TABLE public.wards (
	ward_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	ward_name varchar(255) NOT NULL,
	ward_name_en varchar(255) NOT NULL,
	"type" varchar(255) NOT NULL,
	district_id int4 NOT NULL,
	province_id int4 NOT NULL,
	CONSTRAINT wards_pkey PRIMARY KEY (ward_id),
	CONSTRAINT fk_wards_district FOREIGN KEY (district_id) REFERENCES public.districts(district_id),
	CONSTRAINT fk_wards_province FOREIGN KEY (province_id) REFERENCES public.provinces(province_id)
);


-- public.addresses definition

-- Drop table

-- DROP TABLE public.addresses;

CREATE TABLE public.addresses (
	address_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	address_detail varchar(255) NULL,
	district_id int4 NULL,
	province_id int4 NULL,
	ward_id int4 NULL,
	is_deleted bool DEFAULT false NULL,
	user_id int4 NULL,
	CONSTRAINT addresses_pkey PRIMARY KEY (address_id)
);


-- public.cart_products definition

-- Drop table

-- DROP TABLE public.cart_products;

CREATE TABLE public.cart_products (
	cart_product_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	quantity int4 NOT NULL,
	cart_id int4 NOT NULL,
	product_id int4 NOT NULL,
	CONSTRAINT cart_products_pkey PRIMARY KEY (cart_product_id),
	CONSTRAINT cart_products_quantity_check CHECK ((quantity >= 0))
);


-- public.customers definition

-- Drop table

-- DROP TABLE public.customers;

CREATE TABLE public.customers (
	id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	phone varchar(20) NULL,
	fullname varchar(255) NOT NULL,
	date_of_birth timestamptz(6) NULL,
	age int4 NOT NULL,
	avatar_url varchar(255) NULL,
	date_join timestamptz(6) NOT NULL,
	recommend_product_ids _int4 NULL,
	cart_id int4 NULL,
	user_id int8 NULL,
	CONSTRAINT customers_cart_id_key UNIQUE (cart_id),
	CONSTRAINT customers_pkey PRIMARY KEY (id),
	CONSTRAINT customers_user_id_key UNIQUE (user_id)
);


-- public.order_product_discounts definition

-- Drop table

-- DROP TABLE public.order_product_discounts;

CREATE TABLE public.order_product_discounts (
	product_discount_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	start_date date NOT NULL,
	end_date date NOT NULL,
	discount_id int4 NOT NULL,
	product_id int4 NOT NULL,
	CONSTRAINT order_product_discounts_pkey PRIMARY KEY (product_discount_id)
);


-- public.order_products definition

-- Drop table

-- DROP TABLE public.order_products;

CREATE TABLE public.order_products (
	order_product_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	quantity int4 NOT NULL,
	price numeric(12, 2) NOT NULL,
	order_id int4 NOT NULL,
	product_id int4 NOT NULL,
	CONSTRAINT order_products_pkey PRIMARY KEY (order_product_id)
);


-- public.orders definition

-- Drop table

-- DROP TABLE public.orders;

CREATE TABLE public.orders (
	order_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	order_number varchar(100) NOT NULL,
	shipping_date date NOT NULL,
	order_date date NOT NULL,
	order_status varchar(50) NOT NULL,
	order_total numeric(12, 2) NOT NULL,
	address_id int4 NULL,
	payment_method_id int8 NULL,
	customer_id int8 NOT NULL,
	CONSTRAINT orders_order_number_key UNIQUE (order_number),
	CONSTRAINT orders_pkey PRIMARY KEY (order_id)
);


-- public.payment_methods definition

-- Drop table

-- DROP TABLE public.payment_methods;

CREATE TABLE public.payment_methods (
	payment_method_id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	user_id int8 NOT NULL,
	provider varchar(100) NOT NULL,
	masked_account_number varchar(50) NOT NULL,
	card_holder_name varchar(255) NULL,
	expires_at date NULL,
	is_default bool DEFAULT false NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT payment_methods_pkey PRIMARY KEY (payment_method_id)
);


-- public.product_images definition

-- Drop table

-- DROP TABLE public.product_images;

CREATE TABLE public.product_images (
	product_image_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	image_url varchar(255) NOT NULL,
	is_main bool DEFAULT false NOT NULL,
	product_id int4 NOT NULL,
	CONSTRAINT product_images_pkey PRIMARY KEY (product_image_id)
);


-- public.products definition

-- Drop table

-- DROP TABLE public.products;

CREATE TABLE public.products (
	product_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	product_name varchar(255) NOT NULL,
	original_price numeric(12, 2) NOT NULL,
	stock int4 NOT NULL,
	brand varchar(255) NOT NULL,
	slug varchar(255) NOT NULL,
	product_description text NOT NULL,
	date_created date NOT NULL,
	ratings numeric(3, 2) DEFAULT 0 NOT NULL,
	date_add date NOT NULL,
	category_id int4 NOT NULL,
	vendor_id int8 NOT NULL,
	description text NULL,
	thumbnail_url varchar(255) NULL,
	CONSTRAINT products_pkey PRIMARY KEY (product_id),
	CONSTRAINT products_stock_check CHECK ((stock >= 0))
);


-- public.refresh_tokens definition

-- Drop table

-- DROP TABLE public.refresh_tokens;

CREATE TABLE public.refresh_tokens (
	token_id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	user_id int8 NOT NULL,
	"token" varchar(500) NULL,
	expires_at timestamp NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	is_revoked bool DEFAULT false NULL,
	CONSTRAINT refresh_tokens_pkey PRIMARY KEY (token_id),
	CONSTRAINT refresh_tokens_token_key UNIQUE (token)
);


-- public.reviews definition

-- Drop table

-- DROP TABLE public.reviews;

CREATE TABLE public.reviews (
	review_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	review_rating int4 NOT NULL,
	review_date date NOT NULL,
	review_content text NOT NULL,
	customer_id int8 NOT NULL,
	product_id int4 NOT NULL,
	CONSTRAINT reviews_pkey PRIMARY KEY (review_id),
	CONSTRAINT reviews_review_rating_check CHECK (((review_rating >= 1) AND (review_rating <= 5)))
);


-- public.transactions definition

-- Drop table

-- DROP TABLE public.transactions;

CREATE TABLE public.transactions (
	transaction_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	transation_date timestamp NOT NULL,
	total_money numeric(12, 2) NOT NULL,
	status varchar(50) NOT NULL,
	customer_id int8 NOT NULL,
	order_id int4 NOT NULL,
	CONSTRAINT transactions_pkey PRIMARY KEY (transaction_id)
);


-- public.user_roles definition

-- Drop table

-- DROP TABLE public.user_roles;

CREATE TABLE public.user_roles (
	user_id int8 NOT NULL,
	role_id int4 NOT NULL,
	CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id)
);


-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
	id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"password" varchar(255) NOT NULL,
	last_login timestamp NULL,
	email varchar(255) NOT NULL,
	address_id int4 NULL,
	CONSTRAINT users_email_key UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);


-- public.vendor_payout_accounts definition

-- Drop table

-- DROP TABLE public.vendor_payout_accounts;

CREATE TABLE public.vendor_payout_accounts (
	payout_account_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	vendor_id int8 NOT NULL,
	bank_name varchar(255) NOT NULL,
	account_number varchar(100) NOT NULL,
	account_holder_name varchar(255) NOT NULL,
	branch_name varchar(255) NULL,
	CONSTRAINT vendor_payout_accounts_pkey PRIMARY KEY (payout_account_id),
	CONSTRAINT vendor_payout_accounts_vendor_id_key UNIQUE (vendor_id)
);


-- public.vendors definition

-- Drop table

-- DROP TABLE public.vendors;

CREATE TABLE public.vendors (
	id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"name" varchar(255) NULL,
	phone varchar(20) NULL,
	description text NULL,
	logo_url varchar(255) NULL,
	date_join date NOT NULL,
	user_id int8 NOT NULL,
	CONSTRAINT vendors_pkey PRIMARY KEY (id),
	CONSTRAINT vendors_user_id_key UNIQUE (user_id)
);


-- public.addresses foreign keys

ALTER TABLE public.addresses ADD CONSTRAINT addresses_district_id_fkey FOREIGN KEY (district_id) REFERENCES public.districts(district_id);
ALTER TABLE public.addresses ADD CONSTRAINT addresses_province_id_fkey FOREIGN KEY (province_id) REFERENCES public.provinces(province_id);
ALTER TABLE public.addresses ADD CONSTRAINT addresses_ward_id_fkey FOREIGN KEY (ward_id) REFERENCES public.wards(ward_id);
ALTER TABLE public.addresses ADD CONSTRAINT fk1fa36y2oqhao3wgg2rw1pi459 FOREIGN KEY (user_id) REFERENCES public.users(id);
ALTER TABLE public.addresses ADD CONSTRAINT fk_addresses_user FOREIGN KEY (user_id) REFERENCES public.users(id);


-- public.cart_products foreign keys

ALTER TABLE public.cart_products ADD CONSTRAINT cart_products_cart_id_fkey FOREIGN KEY (cart_id) REFERENCES public.carts(cart_id);
ALTER TABLE public.cart_products ADD CONSTRAINT cart_products_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(product_id);


-- public.customers foreign keys

ALTER TABLE public.customers ADD CONSTRAINT customers_cart_id_fkey FOREIGN KEY (cart_id) REFERENCES public.carts(cart_id);
ALTER TABLE public.customers ADD CONSTRAINT customers_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


-- public.order_product_discounts foreign keys

ALTER TABLE public.order_product_discounts ADD CONSTRAINT order_product_discounts_discount_id_fkey FOREIGN KEY (discount_id) REFERENCES public.discounts(discount_id);
ALTER TABLE public.order_product_discounts ADD CONSTRAINT order_product_discounts_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(product_id);


-- public.order_products foreign keys

ALTER TABLE public.order_products ADD CONSTRAINT order_products_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(order_id);
ALTER TABLE public.order_products ADD CONSTRAINT order_products_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(product_id);


-- public.orders foreign keys

ALTER TABLE public.orders ADD CONSTRAINT orders_address_id_fkey FOREIGN KEY (address_id) REFERENCES public.addresses(address_id);
ALTER TABLE public.orders ADD CONSTRAINT orders_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES public.customers(id);
ALTER TABLE public.orders ADD CONSTRAINT orders_payment_method_id_fkey FOREIGN KEY (payment_method_id) REFERENCES public.payment_methods(payment_method_id);


-- public.payment_methods foreign keys

ALTER TABLE public.payment_methods ADD CONSTRAINT payment_methods_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


-- public.product_images foreign keys

ALTER TABLE public.product_images ADD CONSTRAINT fk_product_images_product FOREIGN KEY (product_id) REFERENCES public.products(product_id);


-- public.products foreign keys

ALTER TABLE public.products ADD CONSTRAINT products_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.categories(category_id);
ALTER TABLE public.products ADD CONSTRAINT products_vendor_id_fkey FOREIGN KEY (vendor_id) REFERENCES public.vendors(id);


-- public.refresh_tokens foreign keys

ALTER TABLE public.refresh_tokens ADD CONSTRAINT refresh_tokens_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


-- public.reviews foreign keys

ALTER TABLE public.reviews ADD CONSTRAINT fk_reviews_customer FOREIGN KEY (customer_id) REFERENCES public.customers(id);
ALTER TABLE public.reviews ADD CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) REFERENCES public.products(product_id);


-- public.transactions foreign keys

ALTER TABLE public.transactions ADD CONSTRAINT transactions_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES public.customers(id);
ALTER TABLE public.transactions ADD CONSTRAINT transactions_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(order_id);


-- public.user_roles foreign keys

ALTER TABLE public.user_roles ADD CONSTRAINT user_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(role_id);
ALTER TABLE public.user_roles ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


-- public.users foreign keys

ALTER TABLE public.users ADD CONSTRAINT users_address_id_fkey FOREIGN KEY (address_id) REFERENCES public.addresses(address_id);


-- public.vendor_payout_accounts foreign keys

ALTER TABLE public.vendor_payout_accounts ADD CONSTRAINT vendor_payout_accounts_vendor_id_fkey FOREIGN KEY (vendor_id) REFERENCES public.vendors(id);


-- public.vendors foreign keys

ALTER TABLE public.vendors ADD CONSTRAINT vendors_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);