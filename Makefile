# External Variables: SSH_KEY, SONAR_KEY, DOCKER_USERNAME, DOCKER_PASSWORD
.SILENT:

DOCKER_RUN=docker run -ti --rm --name "engineering-projekt-server-testing" \
		   --link engineering-projekt-server-testing-postgres \
		   --volume $(shell pwd)/.m2:/home/java/.m2 --volume "$(shell pwd):/home/java/project" \
		   fabianhauser/engineering-projekt-server-testing

VERSION=$(shell ./ci/version.bash)

ifdef TRAVIS_BRANCH
 BRANCH=$(TRAVIS_BRANCH)
else
 BRANCH=`git rev-parse --abbrev-ref HEAD`
endif

upload-coverage:
	@echo "===================================================================="
	@echo "Upload Coverage results to Sonarqube"
	@echo "===================================================================="
	$(DOCKER_RUN) sonar-scanner -X -Dsonar.login=$(SONAR_KEY) -Dsonar.branch=$(BRANCH)

build: install
	docker build -f ci/production/Dockerfile \
		-t "fabianhauser/engineering-projekt-server" ./


install:
	@echo "===================================================================="
	@echo "Testing and building application and container"
	@echo "===================================================================="
	$(DOCKER_RUN) mvn install

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
	docker run --detach\
		--env POSTGRES_DB=adit --env POSTGRES_USER=adit --env POSTGRES_PASSWORD=adit \
		--volume $(shell pwd)/database.sql:/docker-entrypoint-initdb.d/database.sql:ro \
		--name engineering-projekt-server-testing-postgres postgres:9.6-alpine
	@sleep 4

postgres-stop:
	@echo "===================================================================="
	@echo "Stopping testing postgres docker container"
	@echo "===================================================================="
	docker stop engineering-projekt-server-testing-postgres
	docker rm engineering-projekt-server-testing-postgres


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
	@echo Push docker image with new tags
	docker tag fabianhauser/engineering-projekt-server fabianhauser/engineering-projekt-server$(CONTAINER_SUFFIX):$(VERSION)
	docker tag fabianhauser/engineering-projekt-server fabianhauser/engineering-projekt-server$(CONTAINER_SUFFIX)
	docker login -u="$(DOCKER_USERNAME)" -p="$(DOCKER_PASSWORD)"
	docker push fabianhauser/engineering-projekt-server$(CONTAINER_SUFFIX)

	@echo "Trigger container pull on server"
	echo ${SSH_KEY} | base64 -d > id_ed25519 && chmod 700 id_ed25519
	@echo "Execute ssh trigger..."
	ssh -o "StrictHostKeyChecking no" -q -i id_ed25519 rollator-epj-server$(CONTAINER_SUFFIX)@adit.qo.is
	@echo "done."
	rm id_ed25519

