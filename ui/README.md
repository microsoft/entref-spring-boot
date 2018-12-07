# Project Jackson Client

- Built using `React`
- Compiled and bundled using `TypeScript` and `Webpack`
- Tested using `Jest` and `Enzyme`

## Deploy

In order to deploy this app, in development or production, you must define the AAD Client ID as an environment variable. It will be injected during build time with Webpack. The environment variable that needs to be set is called `WEBPACK_PROP_AAD_CLIENT_ID` and is provided by your AAD App settings available on the AAD Azure Portal.
You also must define the domain for the '/people' and '/titles' API endpoints as an environment variable, which will also be injected during build time with Webpack. The environment variable that must be set for the people and titles endpoints is `WEBPACK_PROP_API_BASE_URL`.

In development, you should need to set the env variables on process and then run `npm run dev` as usual. For example, on a UNIX Bash shell you can run `WEBPACK_PROP_AAD_CLIENT_ID=<insert-id-here> npm run dev`.

In production, make sure to set this in the build pipeline such as in Azure Dev Ops.

## Contributing

Run `npm run dev` to launch a hot-reloading webpack server

Before commiting your changes make sure to run `npm run lint`

Testing with `npm run test` will run all test files in the `src/__tests__` directory

The `conf` directory is for production deployment purposes with NGINX
