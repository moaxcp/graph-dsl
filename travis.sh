#!/bin/bash
set -euo pipefail

git fetch --unshallow || true #get all commit history for exact blame info
./gradlew audit

if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
    echo "Build for master"

    ./gradlew check
    ./gradlew -Pversioneye.api_key=$VERSIONEYE_KEY versionEyeSecurityAndLicenseCheck

    ./gradlew uploadArchives \
    -Dnexus.username=moaxcp \
    -Dnexus.password=$NEXUS_PASSWORD \
    -Psigning.keyId=A9A4043B \
    -Psigning.secretKeyRingFile=signingkey.gpg \
    -Psigning.password=$SIGNING_PASSWORD

    ./gradlew closeAndReleaseRepository --info --stacktrace \
    -Dnexus.username=moaxcp \
    -Dnexus.password=$NEXUS_PASSWORD

    ./gradlew groovydoc
    ./gradlew jacocoTestReport

    git config --global user.email "travis@travis-ci.org"
    git config --global user.name "travis-ci"
    git clone --branch=gh-pages https://${GITHUB_TOKEN}@github.com/moaxcp/graph-dsl gh-pages
    cd gh-pages
    git rm -rf docs || true
    git rm -rf reports || true
    cp -Rf ../build/docs .
    cp -Rf ../build/reports .
    git add -f .
    git commit -m "Lastest groovydoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
    git push -fq origin gh-pages
    cd ..

elif [ "$TRAVIS_PULL_REQUEST" != "false" ] && [ -n "${GITHUB_TOKEN:-}" ]; then
    echo "Build for internal pull request"
    ./gradlew test #make sure build fails if tests fail

else
    echo "Build for develop, feature, release, or external pull request"
    ./gradlew build \
    -Psigning.keyId=A9A4043B \
    -Psigning.secretKeyRingFile=signingkey.gpg \
    -Psigning.password=$SIGNING_PASSWORD
fi