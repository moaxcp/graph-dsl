#!/bin/bash
set -euo pipefail

if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
    echo "build for publishing to maven central *NOT IMPLEMENTED*"

elif [ "$TRAVIS_BRANCH" == "develop" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
    echo "Build for develop"

    git fetch --unshallow || true #allows sonar to get all commit history for exact blame info

    #run tests but continue so sonar gets failed info
    ./gradlew test --continue sonarqube \
    -Psonar.host.url=$SONAR_HOST_URL \
    -Psonar.login=$SONAR_TOKEN

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
    echo "Build for feature or external pull request"
    ./gradlew build
fi