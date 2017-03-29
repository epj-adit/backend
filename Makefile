# External Variables: SSH_KEY, SONAR_KEY, DOCKER_USERNAME, DOCKER_PASSWORD
.SILENT:

DOCKER_RUN=docker run -ti --rm --name "engineering-projekt-server-testing" --env NODE_ENV \
		   --volume "$(shell pwd):/home/java/project" fabianhauser/engineering-projekt-server-testing

BRANCH=`git rev-parse --abbrev-ref HEAD`
VERSION=$(shell ./ci/version.bash)

tests:
	@echo "===================================================================="
	@echo "Executeing tests"
	@echo "===================================================================="
	$(DOCKER_RUN) mvn test

upload-coverage:
	@echo "===================================================================="
	@echo "Upload Coverage results to Sonarqube"
	@echo "===================================================================="
	$(DOCKER_RUN) /opt/sonar-scanner/bin/sonar-scanner -X -Dsonar.login=$(SONAR_KEY) -Dsonar.branch=$(BRANCH)

build:
	@echo "===================================================================="
	@echo "Building application and container"
	@echo "===================================================================="
	$(DOCKER_RUN) mvn build
	docker build -f ci/production/Dockerfile \
		-t "fabianhauser/engineering-projekt-server" ./

build-container-testing:
	@echo "===================================================================="
	@echo "Building testing docker container"
	@echo "===================================================================="
	docker build -f ci/testing/Dockerfile \
		-t "fabianhauser/engineering-projekt-server-testing" ./

postgres-start:
	@echo "===================================================================="
	@echo "Starting testing postgres docker container"
	@echo "===================================================================="
	docker run --rm --detach \
		--env POSTGRES_USER=adit \
		--env POSTGRES_PASSWORD='+!r8Ywd\H~#;YR{' \
		--env POSTGRES_DB=adit \
		--volume $(shell pwd)/database.sql:/docker-entrypoint-initdb.d/database.sql:ro \
		--publish 5432:5432
		--name engineering-projekt-server-testing-postgres postgres:9.6-alpine
	@sleep 2

postgres-stop:
	@echo "===================================================================="
	@echo "Stopping testing postgres docker container"
	@echo "===================================================================="
	docker stop engineering-projekt-server-testing-postgres


deploy-live: DEPLOY_SYSTEM=live
deploy-live: deploy

deploy-develop: DEPLOY_SYSTEM=develop
deploy-develop: CONTAINER_SUFFIX=-develop
deploy-develop: deploy

deploy:
	[ "$(DEPLOY_SYSTEM)" != "" ] || (echo "This target may not be called directly." >&2; false)
	@echo "===================================================================="
	@echo "Deploy to the $(DEPLOY_SYSTEM) system"
	@echo "===================================================================="
	@Echo Push docker image with new tags
	docker tag fabianhauser/engineering-projekt-server fabianhauser/engineering-projekt-server$(CONTAINER_SUFFIX):$(VERSION)
	docker tag fabianhauser/engineering-projekt-server fabianhauser/engineering-projekt-server$(CONTAINER_SUFFIX)
	docker login -u="$(DOCKER_USERNAME)" -p="$(DOCKER_PASSWORD)"
	docker push fabianhauser/engineering-projekt-server$(CONTAINER_SUFFIX)

	@echo "Trigger container pull on server"
	echo ${SSH_KEY} | base64 -d > id_ed25519 && chmod 700 id_ed25519
	@echo "Execute ssh trigger..."
	ssh -o "StrictHostKeyChecking no" -q -i id_ed25519 rollator-epj-client$(CONTAINER_SUFFIX)@adit.qo.is
	@echo "done."
	rm id_ed25519

