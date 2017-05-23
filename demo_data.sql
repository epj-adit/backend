-- user
INSERT INTO public."user" (id, username, email, password_hash, jwttoken, is_active, is_private, wants_notification, created, updated, role_id)
VALUES (1000, 'admin', 'admin@hsr.ch', '$2a$10$gVWTZtEifW0H8KWYljpOwesFhgaVZz7y8UFvVyeSvumWGXWxFpdGW', null, true, false, true, NOW(), NULL , (select id from role where name = 'admin'));

INSERT INTO public."user" (id, username, email, password_hash, jwttoken, is_active, is_private, wants_notification, created, updated, role_id)
VALUES (1001, 'supervisor', 'supervisor@hsr.ch', '$2a$10$gVWTZtEifW0H8KWYljpOwesFhgaVZz7y8UFvVyeSvumWGXWxFpdGW', null, true, false, true, NOW(), NULL , (select id from role where name = 'supervisor'));

INSERT INTO public."user" (id, username, email, password_hash, jwttoken, is_active, is_private, wants_notification, created, updated, role_id)
VALUES (1002, 'user', 'user@hsr.ch', '$2a$10$gVWTZtEifW0H8KWYljpOwesFhgaVZz7y8UFvVyeSvumWGXWxFpdGW', null, true, false, true, NOW(), NULL , (select id from role where name = 'user'));




-- permission migration
truncate permission cascade;

INSERT INTO public.permission (id, name) VALUES (1, 'basic_permission');
INSERT INTO public.permission (id, name) VALUES (2, 'supervisor_permission');
INSERT INTO public.permission (id, name) VALUES (3, 'administrator_permission');
INSERT INTO public.permission (id, name) VALUES (4, 'edit_categories');
INSERT INTO public.permission (id, name) VALUES (5, 'review_advertisements');
INSERT INTO public.permission (id, name) VALUES (6, 'edit_role');
INSERT INTO public.permission (id, name) VALUES (7, 'edit_isActive');

update "user" set role_id = 1;
update role set name = 'admin' where id = 1;
delete from role where id > 1;

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


ALTER TABLE "user" ALTER COLUMN jwttoken TYPE character varying(1000);
