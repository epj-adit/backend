# Engineering Projekt 2017

## Server Bash Commands
- Show Logs: `docker logs -f engineering-projekt-server-develop`
- Connect to DB: `docker exec -it engineering-projekt-server-develop-postgres psql -U postgres adit`

- Run DB Script: 
    ```
    scp testdb.sql root@adit.qo.is:/tmp/`
    docker exec -i engineering-projekt-server-develop-postgres psql -U postgres adit < /tmp/testdb.sql
    ```
