-- **************************************************
-- *				MASTER DATA						*
-- **************************************************

-- PERMISSIONS
INSERT INTO public.permission (id, name) VALUES (1, 'create_review');

-- ROLES
INSERT INTO public.role (id, name) VALUES (1, 'admin');
INSERT INTO public.role (id, name) VALUES (2, 'supervisor');
INSERT INTO public.role (id, name) VALUES (3, 'user');

INSERT INTO public.role_permission (permission_id, role_id) VALUES (1, 1);
INSERT INTO public.role_permission (permission_id, role_id) VALUES (1, 2);


-- **************************************************
-- *				DATA							*
-- **************************************************

-- USERS
INSERT INTO public."user" (id, username, email, password_hash, is_active, is_private, wants_notification, created, updated, role_id) VALUES (1, 'rantaplan', 'mwieland@hsr.ch', 'abcde', false, true, 'true', NOW(), NULL ,1);
INSERT INTO public."user" (id, username, email, password_hash, is_active, is_private, wants_notification, created, updated, role_id) VALUES (2, 'vinxhe', 'nvinzens@hsr.ch', 'abcde', true, true, 'true', NOW(), NULL ,1);
INSERT INTO public."user" (id, username, email, password_hash, is_active, is_private, wants_notification, created, updated, role_id) VALUES (3, 'student', 'student@hsr.ch', 'abcde', true, true, 'true', NOW(), NULL , 3);
INSERT INTO public."user" (id, username, email, password_hash, is_active, is_private, wants_notification, created, updated, role_id) VALUES (4, 'deleted', 'deleted@hsr.ch', 'abcde', true, true, 'true', NOW(), NULL , 3);

-- USER LOG
INSERT INTO public.user_log (id, ip, action, created, user_id) VALUES (1, '152.96.235.27', 'Login', NOW(), 1);

-- CATEGORIES
INSERT INTO public.category (id, name, parent_category_id) VALUES (1, 'Bücher', null);
INSERT INTO public.category (id, name, parent_category_id) VALUES (2, 'Jobs', null);
INSERT INTO public.category (id, name, parent_category_id) VALUES (3, 'WG Zimmer', null);

-- SUBSCRIPTION
INSERT INTO public.subscription (id, interval, last_updated, category_id, user_id)  VALUES (1, 2592000000, NOW(), 1, 1);

-- TAGS
INSERT INTO public.tag (id, name) VALUES (1, 'Eduard Glatz');
INSERT INTO public.tag (id, name) VALUES (2, 'Betriebssysteme 1');

-- ADVERTISEMENTS
INSERT INTO public.advertisement (id, title, description, price, created, updated, advertisementstate, category_id, user_id) VALUES (1, 'Betriebsysteme', 'Betriebsysteme Buch von Eduard Glatz mit Notizen ', 30000, NOW(), NULL, 3, 1, 3);
INSERT INTO public.advertisement (id, title, description, price, created, updated, advertisementstate, category_id, user_id) VALUES (2, 'GoF Patterns', 'Buch zu den bekannten Design Pattern der GoF', 20000, NOW(), NULL, 3, 1, 1);
INSERT INTO public.advertisement (id, title, description, price, created, updated, advertisementstate, category_id, user_id) VALUES (3, 'WG Zimmer Jona', 'WG Zimmer inder Tägernaustrasse Jona', 30000, NOW(), NULL, 3, 3, 1);

INSERT INTO public.advertisement_tag (tag_id, advertisement_id) VALUES (1, 1);
INSERT INTO public.advertisement_tag (tag_id, advertisement_id) VALUES (2, 1);

-- MESSAGES
INSERT INTO public.message (id, message, created, advertisement_id, messagestate, recipient_user_id, sender_user_id) VALUES (1, 'Hallo Welt!', NOW(), null, 1, 1, 2);
