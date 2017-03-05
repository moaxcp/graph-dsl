#!/bin/bash
set -euo pipefail

if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
    echo "Build for master"

    git fetch --unshallow || true #allows sonar to get all commit history for exact blame info

    #run tests but continue so sonar gets failed info
    ./gradlew test --continue sonarqube \
    -Dsonar.host.url=$SONAR_HOST_URL \
    -Dsonar.login=$SONAR_TOKEN

    ./gradlew publish \
    -Dnexus.username=moaxcp \
    -Dnexus.password=$NEXUS_PASSWORD \
    -Psigning.keyId=A9A4043B \
    -Psigning.secretKeyRingFile=signingkey.gpg \
    -Psigning.password=$SIGNING_PASSWORD

    ./gradlew closeAndPromoteRepository

elif [ "$TRAVIS_PULL_REQUEST" != "false" ] && [ -n "${GITHUB_TOKEN:-}" ]; then
    echo "Build for internal pull request"
    ./gradlew test --continue sonarqube \
    -Dsonar.analysis.mode=preview \
    -Dsonar.github.oauth=$GITHUB_TOKEN \
    -Dsonar.github.pullRequest=$TRAVIS_PULL_REQUEST \
    -Dsonar.github.repository=$TRAVIS_REPO_SLUG \
    -Dsonar.host.url=$SONAR_HOST_URL \
    -Dsonar.login=$SONAR_TOKEN

else
    echo "Build for develop, feature, release, or external pull request"
    ./gradlew build --debug --stacktrace \
    -Psigning.keyId=A9A4043B \
    -Psigning.secretKeyRingFile=signingkey.gpg \
    -Psigning.password=$SIGNING_PASSWORD
fi