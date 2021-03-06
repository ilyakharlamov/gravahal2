var webpack = require('webpack');
var path = require('path');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = function(options) {
  var cssLoaders = 'style-loader!css-loader!autoprefixer-loader?browsers=last 2 versions';
  var sassLoaders = cssLoaders + '!sass-loader?indentedSyntax=sass';

  if (options.production) {
    cssLoaders = ExtractTextPlugin.extract('style-loader', cssLoaders.substr(cssLoaders.indexOf('!')));
    sassLoaders = ExtractTextPlugin.extract('style-loader', sassLoaders.substr(sassLoaders.indexOf('!')));
  }

  var jsLoaders = ['babel-loader'];

  return {
    entry: './src/main/javascript/index.jsx',
    debug: !options.production,
    devtool: options.devtool,
    output: {
      path: options.production ? './target/classes/web' : './src/main/resources/web',
      publicPath: '',
      filename: options.production ? 'app.[hash].js' : 'app.js',
    },
    eslint: {
      configFile: './conf/.eslintrc'
    },
    module: {
      preLoaders: options.lint ? [
        {
          test: /\.jsx?$/,
          include: /src/,
          loader: 'eslint-loader',
        }
      ] : [],
      loaders: [
        {
          test: /\.js$/,
          include: /src/,
          loaders: jsLoaders,
        },
        {
          test: /\.jsx$/,
          include: /src/,
          loaders: options.production ? jsLoaders : ['react-hot-loader'].concat(jsLoaders),
        },
        {
          test: /\.css$/,
          include: /src/,
          loader: cssLoaders,
        },
        {
          test: /\.sass$/,
          include: /src/,
          loader: sassLoaders,
        },
        {
          test: /\.png$/,
          include: /src/,
          loader: "url-loader?limit=100000&mimetype=image/png",
        },
        {
          test: /\.svg$/,
          include: /src/,
          loader: "url-loader?limit=100000&mimetype=image/svg+xml",
        },
        {
          test: /\.gif$/,
          include: /src/,
          loader: "url-loader?limit=100000&mimetype=image/gif",
        },
        {
          test: /\.jpg$/,
          include: /src/,
          loader: "file-loader",
        },
      ]
    },
    resolve: {
      extensions: ['', '.js', '.jsx'],
    },
    plugins: [
      // Important to keep React file size down
      new webpack.DefinePlugin({
        "process.env": {
          "NODE_ENV": JSON.stringify("production")
        }
      }),
      new webpack.optimize.DedupePlugin(),
      new webpack.optimize.UglifyJsPlugin({
        compress: {
          warnings: false,
        }
      }),
      new ExtractTextPlugin("app.[hash].css"),
      new HtmlWebpackPlugin({
        template: './conf/tmpl.html'
      }),
    ]
  };
};
