#!/usr/bin/env bash
if [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
  echo -e "Starting to update gh-pages\n"
  # copy data we're interested in to other place
  cp -R target/docs $HOME/docs
  # go to home and setup git
  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "Travis"
  # using token clone gh-pages branch
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/commercehub-oss/grails-page-resources.git gh-pages > /dev/null
  # go into directory and copy data we're interested in to that directory
  cd gh-pages
  mkdir docs
  cp -Rf $HOME/docs/* docs/
  # add, commit and push files
  git add -f .
  git commit -m "Travis build $TRAVIS_BUILD_NUMBER pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null
  echo -e "Finished updating gh-pages\n"
fi
