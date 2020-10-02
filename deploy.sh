#!/usr/bin/env bash

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
  ./gradlew uploadArchives -PossrhUsername="${SONATYPE_USERNAME}" -PossrhUsername="${SONATYPE_PASSWORD}" -Psigning.keyId="${GPG_KEY_ID}" -Psigning.password="${GPG_KEY_PASSPHRASE}" -Psigning.secretKeyRingFile=secring.gpg
fi
