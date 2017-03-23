-- DROP AND CREATE DATABASE WITH OWNER
DROP DATABASE if exists adit;
DROP USER if exists adit;

CREATE USER adit WITH PASSWORD '+!r8Ywd\H~#;YR{';
CREATE DATABASE adit WITH OWNER adit ENCODING 'UTF8';

\c adit

-- USER TABLES
CREATE TABLE "user" (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	username TEXT NOT NULL,
  	email TEXT NOT NULL,
  	passwort_hash TEXT NOT NULL,
  	is_private BOOLEAN NOT NULL DEFAULT FALSE,
  	wants_notification BOOLEAN NOT NULL DEFAULT FALSE,
  	is_active BOOLEAN NOT NULL DEFAULT FALSE,
  	created TIMESTAMP NOT NULL DEFAULT NOW(),
  	updated TIMESTAMP,
  	role_id BIGINT NOT NULL
);

CREATE TABLE role (
 	id BIGSERIAL NOT NULL PRIMARY KEY,
 	name TEXT NOT NULL
);

CREATE TABLE permission (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	name TEXT NOT NULL
);

CREATE TABLE role_permission (
	role_id BIGINT NOT NULL,
  	permission_id BIGINT NOT NULL,
  	primary key (role_id, permission_id)
);

CREATE TABLE user_log (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	ip TEXT NOT NULL,
	action TEXT NOT NULL,
	created TIMESTAMP NOT NULL DEFAULT NOW(),
	user_id INTEGER NOT NULL
);

-- AVERTISEMENT TABLES
CREATE TABLE advertisement (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	title TEXT NOT NULL,
	description TEXT NOT NULL,
	price INTEGER NOT NULL DEFAULT 0,
	created TIMESTAMP NOT NULL DEFAULT NOW(),
	updated TIMESTAMP,
	user_id BIGINT NOT NULL,
	category_id BIGINT NOT NULL,
	advertisement_state_id BIGINT NOT NULL
);

CREATE TABLE advertisement_state (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	name TEXT NOT NULL
);

CREATE TABLE tag (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	name TEXT NOT NULL
);

CREATE TABLE advertisement_tag (
	advertisement_id BIGINT NOT NULL,
  	tag_id BIGINT NOT NULL,
  	primary key (advertisement_id, tag_id)
);

CREATE TABLE media (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	filename TEXT NOT NULL,
	description TEXT,
	media BYTEA NOT NULL,
	advertisement_id BIGINT NOT NULL
);

CREATE TABLE category (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	name TEXT NOT NULL,
	parent_category_id BIGINT
);

CREATE TABLE subscription (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	interval INTEGER NOT NULL,
	last_updated TIMESTAMP NOT NULL,
	user_id BIGINT NOT NULL,
	category_id BIGINT NOT NULL
);

-- MESSAGE TABLES
CREATE TABLE message (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	message TEXT NOT NULL,
	created TIMESTAMP NOT NULL DEFAULT NOW(),
	sender_user_id BIGINT,
	recipient_user_id BIGINT NOT NULL,
	message_state_id BIGINT,
	advertisement_id BIGINT
);

CREATE TABLE message_state (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	name TEXT NOT NULL
);

-- USER FOREIGN KEYS
ALTER TABLE "user" ADD FOREIGN KEY(role_id) REFERENCES role(id);
ALTER TABLE role_permission ADD FOREIGN KEY(role_id) REFERENCES role(id);
ALTER TABLE role_permission ADD FOREIGN KEY(permission_id) REFERENCES permission(id);
ALTER TABLE user_log ADD FOREIGN KEY(user_id) REFERENCES "user"(id);

-- USER UNIQUE KEYS
ALTER TABLE "user" ADD CONSTRAINT email_unique UNIQUE (email);

-- AVERTISEMENT FOREIGN KEYS
ALTER TABLE advertisement ADD FOREIGN KEY(user_id) REFERENCES "user"(id);
ALTER TABLE advertisement ADD FOREIGN KEY(category_id) REFERENCES category(id);
ALTER TABLE advertisement ADD FOREIGN KEY(advertisement_state_id) REFERENCES advertisement_state(id);
ALTER TABLE advertisement_tag ADD FOREIGN KEY(advertisement_id) REFERENCES advertisement(id);
ALTER TABLE advertisement_tag ADD FOREIGN KEY(tag_id) REFERENCES tag(id);
ALTER TABLE media ADD FOREIGN KEY(advertisement_id) REFERENCES advertisement(id);
ALTER TABLE category ADD FOREIGN KEY(parent_category_id) REFERENCES category(id);
ALTER TABLE subscription ADD FOREIGN KEY(category_id) REFERENCES category(id);
ALTER TABLE subscription ADD FOREIGN KEY(user_id) REFERENCES "user"(id);

-- MESSAGE FOREIGN KEYS
ALTER TABLE message ADD FOREIGN KEY(sender_user_id) REFERENCES "user"(id);
ALTER TABLE message ADD FOREIGN KEY(recipient_user_id) REFERENCES "user"(id);
ALTER TABLE message ADD FOREIGN KEY(advertisement_id) REFERENCES advertisement(id);
ALTER TABLE message ADD FOREIGN KEY(message_state_id) REFERENCES message_state(id);
