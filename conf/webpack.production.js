var webpack = require('./make-webpack-config');

module.exports = require('./make-webpack-config')({
  production: true,
  lint: true,
  harmony: false, 
  es6module: true
});
