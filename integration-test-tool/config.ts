export const config = Object.freeze({
  backend_site: process.env.TARGET_BACKEND_SITE || 'https://www.projectjackson.io',
  password: process.env.TARGET_PASSWORD || '1234567',
  ui_site: process.env.TARGET_FRONTEND_SITE || 'https://www.projectjackson.io/ui',
  username: process.env.TARGET_USERNAME || 'testuser@projectjackson.onmicrosoft.com',
  environment: process.env.NODE_ENV || 'development'
})
