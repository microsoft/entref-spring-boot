export const config = Object.freeze({
  password: process.env.TARGET_PASSWORD || '1234567',
  site: process.env.TARGET_SITE || 'http://bing.com',
  username: process.env.TARGET_USERNAME || 'alice',
})
