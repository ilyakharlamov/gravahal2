var webpack = require('./make-webpack-config');

module.exports = require('./make-webpack-config')({
  production: false,
  lint: true,
});
