#!/usr/bin/env sh
mkdir ~/.grails/
cat <<EOF > ~/.grails/settings.groovy
grails.project.repos.grailsCentral.username='$GRAILS_USERNAME'
grails.project.repos.grailsCentral.password='$GRAILS_PASSWORD'
grails.project.portal.grailsCentral.username='$GRAILS_USERNAME'
grails.project.portal.grailsCentral.password='$GRAILS_PASSWORD'
EOF
./grailsw package-plugin && ./grailsw doc && ./grailsw publish-plugin --non-interactive --no-scm --no-overwrite
