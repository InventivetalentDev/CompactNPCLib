#!/bin/sh
if [ -n "$GITHUB_API_KEY" ]; then
    cd "$TRAVIS_BUILD_DIR"
    mkdir docs
    cd docs
    git init
    git checkout -b gh-pages || git checkout gh-pageswd
    cp "$TRAVIS_BUILD_DIR/target/site/apidocs" "."
    git add .
    git config --global user.email "git-bot@inventivetalent.org"
    git config --global user.name "InventiveBot"
    git commit -m "Update Javadoc"
    # Make sure to make the output quiet, or else the API token will leak!
    # This works because the API key can replace your password.
    git push -f -q https://InventiveBot:$GITHUB_API_KEY@github.com/InventivetalentDev/CompactNPCLibs gh-pages &2>/dev/null
    cd "$TRAVIS_BUILD_DIR"
endif
