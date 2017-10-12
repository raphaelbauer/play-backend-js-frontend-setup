# Deployment for production

The secret is to first build preact, then copy the created assets to the public folder of play. 
And then  create a dist bundle of play.

This is automated in a file called build.sh in the root folder.

# Running and developing locally

The basic idea is having the frontend (preact) and backend (play) completely separated.
You start the devmode of play in one console. Then start the devmode of preact in another console.

The Play backend will act as reverse proxy for the preact devmode - therefore you should always use http://localhost:9000
for development.

That way you got both frontend and backend up and running nicely. 

## Start backend (Play/Scala)

The backend is written in Play and Scala.

Prerequisites:
    - java
    - sbt (eg via 'brew install sbt')

Run:
    cd backend-play
    sbt run

You can access the application via http://localhost:9000

## Start frontend for development (Preact)

The frontend is done via Preact (aka react). And built via its preact-cli tool.

Prerequisites:
    - node/8.4.0 (install eg via n at https://github.com/tj/n)
    - preact cli (https://github.com/developit/preact-cli - install via 'npm i -g preact-cli') (optional, as it is not necessary to have it globally installed) 

Run:

    cd frontend-preact
    npm install
    npm run dev

This will start the preact dev server at http://localhost:8080. BUT you shall use http://localhost:9000 for development.
The Play backend reverse proxies that development server. That way you can develop both backend and frontend at one url and port.

Note: We are using preact-cli. It does a lot of nice magic for us. The secret gem is not to touch it too much. Therefore
we are using Play to proxy the frontend, not the other way round. Goal: Keep the javascript preact build damn stupid simple.
Hmm. We also try to keep the build.sbt as simple as possible. So simplicity is paramount for both preact/npm and play/sbt builds :D
