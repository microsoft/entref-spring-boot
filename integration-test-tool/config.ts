export const config = Object.freeze({
  backend_site: process.env.TARGET_BACKEND_SITE || 'http://bing.com',
  password: process.env.TARGET_PASSWORD || '1234567',
  ui_site: process.env.TARGET_FRONTEND_SITE || 'http://bing.com',
  username: process.env.TARGET_USERNAME || 'alice',
})
