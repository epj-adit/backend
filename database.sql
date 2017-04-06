-- USER TABLES
CREATE TABLE public."user" (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	username TEXT NOT NULL,
  	email TEXT NOT NULL,
  	password_hash TEXT NOT NULL,
  	is_private BOOLEAN NOT NULL DEFAULT FALSE,
  	wants_notification BOOLEAN NOT NULL DEFAULT FALSE,
  	is_active BOOLEAN NOT NULL DEFAULT FALSE,
  	created TIMESTAMP NOT NULL DEFAULT NOW(),
  	updated TIMESTAMP,
  	role_id BIGINT NOT NULL
);

CREATE TABLE public.role (
 	id BIGSERIAL NOT NULL PRIMARY KEY,
 	name TEXT NOT NULL
);

CREATE TABLE public.permission (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	name TEXT NOT NULL
);

CREATE TABLE public.role_permission (
	role_id BIGINT NOT NULL,
  	permission_id BIGINT NOT NULL,
  	primary key (role_id, permission_id)
);

CREATE TABLE public.user_log (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	ip TEXT NOT NULL,
	action TEXT NOT NULL,
	created TIMESTAMP NOT NULL DEFAULT NOW(),
	user_id INTEGER NOT NULL
);

-- AVERTISEMENT TABLES
CREATE TABLE public.advertisement (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	title TEXT NOT NULL,
	description TEXT NOT NULL,
	price INTEGER NOT NULL DEFAULT 0,
	created TIMESTAMP NOT NULL DEFAULT NOW(),
	updated TIMESTAMP,
	user_id BIGINT NOT NULL,
	category_id BIGINT NOT NULL,
	advertisement INT NOT NULL
);

CREATE TABLE public.tag (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	name TEXT NOT NULL
);

CREATE TABLE public.advertisement_tag (
	advertisement_id BIGINT NOT NULL,
  	tag_id BIGINT NOT NULL,
  	primary key (advertisement_id, tag_id)
);

CREATE TABLE public.media (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	filename TEXT NOT NULL,
	description TEXT,
	media BYTEA NOT NULL,
	advertisement_id BIGINT NOT NULL
);

CREATE TABLE public.category (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	name TEXT NOT NULL,
	parent_category_id BIGINT
);

CREATE TABLE public.subscription (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	interval INTEGER NOT NULL,
	last_updated TIMESTAMP NOT NULL,
	user_id BIGINT NOT NULL,
	category_id BIGINT NOT NULL
);

-- MESSAGE TABLES
CREATE TABLE public.message (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	message TEXT NOT NULL,
	created TIMESTAMP NOT NULL DEFAULT NOW(),
	sender_user_id BIGINT,
	recipient_user_id BIGINT NOT NULL,
	messagestate INT,
	advertisement_id BIGINT
);

CREATE TABLE public.message_state (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	name TEXT NOT NULL
);

-- USER FOREIGN KEYS
ALTER TABLE public."user" ADD FOREIGN KEY(role_id) REFERENCES role(id);
ALTER TABLE public.role_permission ADD FOREIGN KEY(role_id) REFERENCES role(id);
ALTER TABLE public.role_permission ADD FOREIGN KEY(permission_id) REFERENCES permission(id);
ALTER TABLE public.user_log ADD FOREIGN KEY(user_id) REFERENCES "user"(id);

-- USER UNIQUE KEYS
ALTER TABLE public."user" ADD CONSTRAINT email_unique UNIQUE (email);

-- AVERTISEMENT FOREIGN KEYS
ALTER TABLE public.advertisement ADD FOREIGN KEY(user_id) REFERENCES "user"(id);
ALTER TABLE public.advertisement ADD FOREIGN KEY(category_id) REFERENCES category(id);
ALTER TABLE public.advertisement ADD FOREIGN KEY(advertisement_state_id) REFERENCES advertisement_state(id);
ALTER TABLE public.advertisement_tag ADD FOREIGN KEY(advertisement_id) REFERENCES advertisement(id);
ALTER TABLE public.advertisement_tag ADD FOREIGN KEY(tag_id) REFERENCES tag(id);
ALTER TABLE public.media ADD FOREIGN KEY(advertisement_id) REFERENCES advertisement(id);
ALTER TABLE public.category ADD FOREIGN KEY(parent_category_id) REFERENCES category(id);
ALTER TABLE public.subscription ADD FOREIGN KEY(category_id) REFERENCES category(id);
ALTER TABLE public.subscription ADD FOREIGN KEY(user_id) REFERENCES "user"(id);

-- MESSAGE FOREIGN KEYS
ALTER TABLE public.message ADD FOREIGN KEY(sender_user_id) REFERENCES "user"(id);
ALTER TABLE public.message ADD FOREIGN KEY(recipient_user_id) REFERENCES "user"(id);
ALTER TABLE public.message ADD FOREIGN KEY(advertisement_id) REFERENCES advertisement(id);
ALTER TABLE public.message ADD FOREIGN KEY(message_state_id) REFERENCES message_state(id);
