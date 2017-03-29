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
	NODE_ENV=test $(DOCKER_RUN) maven test

upload-coverage:
	@echo "===================================================================="
	@echo "Upload Coverage results to Sonarqube"
	@echo "===================================================================="
	$(DOCKER_RUN) /opt/sonar-scanner/bin/sonar-scanner -X -Dsonar.login=$(SONAR_KEY) -Dsonar.branch=$(BRANCH)

build:
	@echo "===================================================================="
	@echo "Building application and container"
	@echo "===================================================================="
	NODE_ENV=prod $(DOCKER_RUN) maven build
	docker build -f ci/production/Dockerfile \
		-t "fabianhauser/engineering-projekt-server" ./

build-container-testing:
	@echo "===================================================================="
	@echo "Building testing docker container"
	@echo "===================================================================="
	docker build -f ci/testing/Dockerfile \
		-t "fabianhauser/engineering-projekt-server-testing" ./

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
	echo ${SSH_KEY} | base64 -d > id_ed25519
	ssh -i id_ed25519 rollator-epj-server$(CONTAINER_SUFFIX)@adit.qo.is
	rm id_ed25519
