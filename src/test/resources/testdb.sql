-- **************************************************
-- *				MASTER DATA						*
-- **************************************************

-- PERMISSIONS
INSERT INTO public.permission (id, name) VALUES (1, 'basic_permission');
INSERT INTO public.permission (id, name) VALUES (2, 'supervisor_permission');
INSERT INTO public.permission (id, name) VALUES (3, 'administrator_permission');
INSERT INTO public.permission (id, name) VALUES (4, 'edit_categories');
INSERT INTO public.permission (id, name) VALUES (5, 'review_advertisements');
INSERT INTO public.permission (id, name) VALUES (6, 'edit_role');
INSERT INTO public.permission (id, name) VALUES (7, 'edit_isActive');

-- ROLES
INSERT INTO public.role (id, name) VALUES (1, 'admin');
INSERT INTO public.role (id, name) VALUES (2, 'supervisor');
INSERT INTO public.role (id, name) VALUES (3, 'user');
INSERT INTO public.role (id, name) VALUES (4, 'to_delete');

INSERT INTO public.role_permission (permission_id, role_id) VALUES (1, 1);
INSERT INTO public.role_permission (permission_id, role_id) VALUES (2, 1);
INSERT INTO public.role_permission (permission_id, role_id) VALUES (3, 1);
INSERT INTO public.role_permission (permission_id, role_id) VALUES (4, 1);
INSERT INTO public.role_permission (permission_id, role_id) VALUES (5, 1);
INSERT INTO public.role_permission (permission_id, role_id) VALUES (6, 1);
INSERT INTO public.role_permission (permission_id, role_id) VALUES (7, 1);
INSERT INTO public.role_permission (permission_id, role_id) VALUES (1, 2);


-- **************************************************
-- *				DATA							*
-- **************************************************

-- USERS
INSERT INTO public."user" (id, username, email, password_hash, jwttoken, is_active, is_private, wants_notification, created, updated, role_id) VALUES (1, 'rantaplan', 'mwieland@hsr.ch', 'abcde', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGFkaXQuY2giLCJwZXJtaXNzaW9ucyI6WyJ0ZXN0X3Blcm1pc3Npb24iXSwiaXNzIjoiYWRpdCIsImV4cCI6MTQ5NDU3NTgwM30.jtrFwDxg9CoyjtUYaUjG1FRqdu-cv3NdhCjBM44zBec', false, true, 'true', NOW(), NULL ,2);
INSERT INTO public."user" (id, username, email, password_hash, jwttoken, is_active, is_private, wants_notification, created, updated, role_id) VALUES (2, 'vinxhe', 'nvinzens@hsr.ch', 'abcde', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGFkaXQuY2giLCJwZXJtaXNzaW9ucyI6WyJ0ZXN0X3Blcm1pc3Npb24iXSwiaXNzIjoiYWRpdCIsImV4cCI6MTQ5NDU3NTgwM30.jtrFwDxg9CoyjtUYaUjG1FRqdu-cv3NdhCjBM44zBec', true, true, 'true', NOW(), NULL ,2);
INSERT INTO public."user" (id, username, email, password_hash, jwttoken, is_active, is_private, wants_notification, created, updated, role_id) VALUES (3, 'student', 'student@hsr.ch', 'abcde', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGFkaXQuY2giLCJwZXJtaXNzaW9ucyI6WyJ0ZXN0X3Blcm1pc3Npb24iXSwiaXNzIjoiYWRpdCIsImV4cCI6MTQ5NDU3NTgwM30.jtrFwDxg9CoyjtUYaUjG1FRqdu-cv3NdhCjBM44zBec', true, true, 'true', NOW(), NULL , 1);
INSERT INTO public."user" (id, username, email, password_hash, jwttoken, is_active, is_private, wants_notification, created, updated, role_id) VALUES (4, 'deleted', 'deleted@hsr.ch', 'abcde', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGFkaXQuY2giLCJwZXJtaXNzaW9ucyI6WyJ0ZXN0X3Blcm1pc3Npb24iXSwiaXNzIjoiYWRpdCIsImV4cCI6MTQ5NDU3NTgwM30.jtrFwDxg9CoyjtUYaUjG1FRqdu-cv3NdhCjBM44zBec', true, true, 'true', NOW(), NULL , 3);
INSERT INTO public."user" (id, username, email, password_hash, jwttoken, is_active, is_private, wants_notification, created, updated, role_id) VALUES (5, 'authenticated', 'authenticated@hsr.ch', '$2a$10$gyXbtQebvTC4bb9w02abluftL2dBbHfwOgJdRk0L0x8oKihQn/4m6', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGFkaXQuY2giLCJwZXJtaXNzaW9ucyI6WyJ0ZXN0X3Blcm1pc3Npb24iXSwiaXNzIjoiYWRpdCIsImV4cCI6MTQ5NDU3NTgwM30.jtrFwDxg9CoyjtUYaUjG1FRqdu-cv3NdhCjBM44zBec', true, true, 'true', NOW(), NULL , 3);
INSERT INTO public."user" (id, username, email, password_hash, jwttoken, is_active, is_private, wants_notification, created, updated, role_id) VALUES (6, 'inactive', 'inactive@hsr.ch', '$2a$10$/XojqMqnwuYCK.OIxZBbHOtYDOIeZ5mCX.ZXlc2RYSaEkLtbNCQQG', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGFkaXQuY2giLCJwZXJtaXNzaW9ucyI6WyJ0ZXN0X3Blcm1pc3Npb24iXSwiaXNzIjoiYWRpdCIsImV4cCI6MTQ5NDU3NTgwM30.jtrFwDxg9CoyjtUYaUjG1FRqdu-cv3NdhCjBM44zBec', false, true, 'true', NOW(), NULL , 3);
INSERT INTO public."user" (id, username, email, password_hash, jwttoken, is_active, is_private, wants_notification, created, updated, role_id) VALUES (7, 'update_to_not_active', 'notactiveanymore@hsr.ch', 'abcde', '', true, true, 'true', NOW(), NULL , 2);

-- USER LOG
INSERT INTO public.user_log (id, ip, action, created, user_id) VALUES (1, '152.96.235.27', 'Login', NOW(), 1);

-- CATEGORIES
INSERT INTO public.category (id, name, parent_category_id) VALUES (1, 'Bücher', null);
INSERT INTO public.category (id, name, parent_category_id) VALUES (2, 'Jobs', null);
INSERT INTO public.category (id, name, parent_category_id) VALUES (3, 'WG Zimmer', null);
INSERT INTO public.category (id, name, parent_category_id) VALUES (4, 'Delete', null);

-- SUBSCRIPTION
INSERT INTO public.subscription (id, interval, last_updated, category_id, user_id)  VALUES (1, 2592000000, NOW(), 1, 1);
INSERT INTO public.subscription (id, interval, last_updated, category_id, user_id)  VALUES (2, 8000000, NOW(), 1, 2);
INSERT INTO public.subscription (id, interval, last_updated, category_id, user_id)  VALUES (3, 8000000, NOW(), 2, 3);

-- TAGS
INSERT INTO public.tag (id, name) VALUES (1, 'Eduard Glatz');
INSERT INTO public.tag (id, name) VALUES (2, 'Betriebssysteme 1');
INSERT INTO public.tag (id, name) VALUES (3, 'Gof');

-- ADVERTISEMENTS
INSERT INTO public.advertisement (id, title, description, price, created, updated, advertisementstate, category_id, user_id) VALUES (1, 'Betriebsysteme', 'Betriebsysteme Buch von Eduard Glatz mit Notizen ', 30000, NOW(), NULL, 2, 1, 3);
INSERT INTO public.advertisement (id, title, description, price, created, updated, advertisementstate, category_id, user_id) VALUES (2, 'GoF Patterns', 'Buch zu den bekannten Design Pattern der GoF', 20000, NOW(), NULL, 2, 1, 3);
INSERT INTO public.advertisement (id, title, description, price, created, updated, advertisementstate, category_id, user_id) VALUES (3, 'WG Zimmer Jona', 'WG Zimmer inder Tägernaustrasse Jona', 30000, NOW(), NULL, 2, 3, 3);
INSERT INTO public.advertisement (id, title, description, price, created, updated, advertisementstate, category_id, user_id) VALUES (4, 'to_delete', 'WG Zimmer inder Tägernaustrasse Jona', 30000, NOW(), NULL, 2, 3, 3);
INSERT INTO public.advertisement (id, title, description, price, created, updated, advertisementstate, category_id, user_id) VALUES (5, 'noup', 'no update allowed', 30000, NOW(), NULL, 2, 1, 1);

INSERT INTO public.advertisement_tag (tag_id, advertisement_id) VALUES (1, 1);
INSERT INTO public.advertisement_tag (tag_id, advertisement_id) VALUES (2, 1);
INSERT INTO public.advertisement_tag (tag_id, advertisement_id) VALUES (1, 3);
INSERT INTO public.advertisement_tag (tag_id, advertisement_id) VALUES (3, 2);

-- MESSAGES
INSERT INTO public.message (id, message, created, advertisement_id, messagestate, recipient_user_id, sender_user_id) VALUES (1, 'Hallo Welt!', NOW(), null, 1, 1, 2);
