#!/usr/bin/env bash
set -e

if [ "$TRAVIS_SECURE_ENV_VARS" = "true" ];
then
mkdir -p ~/.grails/
cat <<EOF > ~/.grails/settings.groovy
grails.project.repos.grailsCentral.username='$GRAILS_USERNAME'
grails.project.repos.grailsCentral.password='$GRAILS_PASSWORD'
grails.project.portal.grailsCentral.username='$GRAILS_USERNAME'
grails.project.portal.grailsCentral.password='$GRAILS_PASSWORD'
EOF
fi

./grailsw package-plugin
./grailsw doc
if grep -q "version\s*=.*-SNAPSHOT" PageResourcesGrailsPlugin.groovy
then
./grailsw publish-plugin --non-interactive --no-scm --allow-overwrite
else
./grailsw publish-plugin --non-interactive --no-scm --no-overwrite
fi
