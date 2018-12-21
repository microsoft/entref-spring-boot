module.exports = {
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
    "**/__tests__/**/*.(ts|tsx|js)"
  ],
  testPathIgnorePatterns: [
    "setup.ts"
  ],
  moduleNameMapper: {
    "\\.(css)$": "identity-obj-proxy"
  },
  setupTestFrameworkScriptFile: "<rootDir>/src/__tests__/setup.ts"
}