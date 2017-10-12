#!/bin/bash
set -x

# build frontend
cd frontend-preact
npm install
npm run build

# delete old public assets in backend
rm -r ../backend-play/public
mkdir ../backend-play/public

# copy preact frontend assets to backend assets directory
cp -r build/* ../backend-play/public
cd ..
cd backend-play


# create play backend zip for distribution (containing the compiled preact assets)
sbt clean dist

cd ..
rm -rf myapp-1.0-SNAPSHOT
unzip backend-play/target/universal/myapp-1.0-SNAPSHOT.zip
