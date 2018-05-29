#!/bin/bash
set -euo pipefail

git fetch --unshallow || true #get all commit history for exact blame info
./gradlew build \
    -Psigning.keyId=A9A4043B \
    -Psigning.secretKeyRingFile=signingkey.gpg \
    -Psigning.password=$SIGNING_PASSWORD

if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
    echo "build for master branch"
    ./gradlew uploadArchives \
        -Dnexus.username=moaxcp \
        -Dnexus.password=$NEXUS_PASSWORD \
        -Psigning.keyId=A9A4043B \
        -Psigning.secretKeyRingFile=signingkey.gpg \
        -Psigning.password=$SIGNING_PASSWORD
fi

./gradlew jacocoTestCoverageVerification

if [ -n "$TRAVIS_TAG" ]; then
    echo "release for $TRAVIS_TAG"
    ./gradlew uploadArchives \
        -Dnexus.username=moaxcp \
        -Dnexus.password=$NEXUS_PASSWORD \
        -Psigning.keyId=A9A4043B \
        -Psigning.secretKeyRingFile=signingkey.gpg \
        -Psigning.password=$SIGNING_PASSWORD

    ./gradlew closeAndReleaseRepository --info --stacktrace \
        -Dnexus.username=moaxcp \
        -Dnexus.password=$NEXUS_PASSWORD \
        -Psigning.keyId=A9A4043B \
        -Psigning.secretKeyRingFile=signingkey.gpg \
        -Psigning.password=$SIGNING_PASSWORD

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
fi