/**
 * This global variable will be injected by Webpack during build time.
 * As defined by the documentation, this variable is defined by your AAD app
 * and is available in the Azure Portal.
 */
declare const WEBPACK_PROP_AAD_CLIENT_ID: string

/**
 * This global variable will be injected by Webpack during build time.
 * As defined by the documentation, this variable references your 
 * '/people' and '/titles' API endpoints
 */
declare const WEBPACK_PROP_API_BASE_URL: string
