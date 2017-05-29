# Engineering Project Backend Application (Server)
  
The backend (server) application of the engineering project adit, 2017. See https://adit.github.io/ for more information.

## Build

Prerequisities:

- [Docker](https://www.docker.com/) Version >= 1.12
- GNU [Make](https://www.gnu.org/software/make/) and [Bash](https://www.gnu.org/software/bash/)
- [git](https://www.git-scm.org/)


```bash
git clone https://github.com/fabianhauser/engineering-projekt-server.git engineering-projekt-server
cd engineering-projekt-server

# Build java building docker container
make build-container-testing

# Build application and docker container
make postgres-start
make build
make postgres-stop

# For a release, the docker container could be uploaded now.
```
