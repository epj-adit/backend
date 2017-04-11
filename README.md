# Engineering Projekt 2017

## Reset DB
- scp testdb.sql root@adit.qo.is:/tmp/
- docker exec -i engineering-projekt-server-develop-postgres psql -U postgres adit < /tmp/testdb.sql
