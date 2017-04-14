#!/bin/bash
echo -e "Uploading Javadoc...\n"

cd $HOME
git config --global user.email "git-bot@inventivetalent.org"
git config --global user.name "InventiveBot"
git clone --quiet --branch=gh-pages https://${GITHUB_TOKEN}@github.com/InventivetalentDev/CompactNPCLib gh-pages > /dev/null

cd gh-pages
git rm -rf .
cp -Rf $HOME/target/site .
git add -f .
git commit -m "Javadoc for build $TRAVIS_BUILD_NUMBER"
git push -fq origin gh-pages > /dev/null

echo -e "Uploaded Javadoc\n"
 
