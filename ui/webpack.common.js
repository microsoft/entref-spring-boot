const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const webpack = require('webpack')

module.exports = {
  entry: "./src/index.tsx",
  output: {
    path: path.resolve(__dirname + "/dist"),
    filename: "bundle.js",
  },
  devtool: "source-map",
  resolve: {
    extensions: [".ts", ".tsx", ".js", ".json"]
  },
  module: {
    rules: [
      { test: /\.tsx?$/, loader: "awesome-typescript-loader" },
      { enforce: "pre", test: /\.js$/, loader: "source-map-loader" },
      { test: /\.css$/, loader: ["style-loader", "css-loader"] }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/public/index.html',
      favicon: './src/public/mountains.svg'
    }),
    // DefinePlugin will inject this env variable anywhere in the code base it ref the attr name
    // new webpack.DefinePlugin({
    //   WEBPACK_PROP_AAD_CLIENT_ID: process.env.WEBPACK_PROP_AAD_CLIENT_ID ? `"${process.env.WEBPACK_PROP_AAD_CLIENT_ID}"` : null,
    //   WEBPACK_PROP_API_BASE_URL: process.env.WEBPACK_PROP_API_BASE_URL ? `"${process.env.WEBPACK_PROP_API_BASE_URL}"` : null,
    //   WEBPACK_PROP_UI_BASEPATH: process.env.WEBPACK_PROP_UI_BASEPATH ? `"${process.env.WEBPACK_PROP_UI_BASEPATH}"` : null
    // }),
    new webpack.EnvironmentPlugin({
      WEBPACK_PROP_AAD_CLIENT_ID: '',
      WEBPACK_PROP_API_BASE_URL: '',
      WEBPACK_PROP_UI_BASEPATH: 'ui'
    })
  ],
}