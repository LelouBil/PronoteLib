#!/bin/bash

#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
  openssl aes-256-cbc -K $encrypted_6a8c67a4e80d_key -iv $encrypted_6a8c67a4e80d_iv -in secring.gpg.enc -out C:\\Users\\Lelou\\.gnupg\\secring.gpg -d
  ./gradlew uploadArchives -PossrhUsername=${SONATYPE_USERNAME} -PossrhPassword=${SONATYPE_PASSWORD} -Psigning.keyId=${GPG_KEY_ID} -Psigning.password=${GPG_KEY_PASSPHRASE} -Psigning.secretKeyRingFile=secring.gpg
fi
