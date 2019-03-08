module.exports = {
  preset: "jest-puppeteer",
  moduleFileExtensions: [ "ts", "tsx", "js" ],
  transform: {
    "^.+\\.(ts|tsx)$": "ts-jest"
  },
  globals: {
    "ts-jest": {
      "tsConfig": "tsconfig.json"
    }
  },
  testMatch: [
    "**/src/**/*.test.(ts|tsx|js)"
  ],
  collectCoverageFrom: [
    "**/*.{js,jsx}",
    "!**/node_modules/**",
    "!**/vendor/**"
  ]
}