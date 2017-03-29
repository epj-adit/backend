-- DROP AND CREATE DATABASE WITH OWNER
DROP DATABASE if exists adit;
DROP USER if exists adit;

CREATE USER adit WITH PASSWORD '+!r8Ywd\H~#;YR{';
CREATE DATABASE adit WITH OWNER adit ENCODING 'UTF8';

\c adit
\i database.sql
