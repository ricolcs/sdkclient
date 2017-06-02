var Webpack = require('webpack');
var path = require('path');
var srcPath = path.resolve(__dirname, 'src');
var nodeModulesPath = path.resolve(__dirname, 'node_modules');
var buildPath = path.resolve(__dirname, 'public', 'build');

var isProduction = true;
if (process.env['NODE_ENV'] == 'dev') {
    isProduction = false;
}

var config = {
    context: __dirname,
    devtool: 'source-map',
    entry: path.resolve(srcPath, 'main.js'),
    output: {
        path: buildPath,
        publicPath: '/build/',
        filename: 'bundle.js'
    },
    module: {
        loaders: [{
            test: /\.vue$/,
            loader: 'vue'
        }, {
            test: /\.js$/,
            loader: 'babel',
            exclude: [nodeModulesPath]
        }, {
            test: /\.es6$/,
            loaders: ['babel']
        }, {
            test: /\.css$/,
            loader: 'style!css'
        }, {
            test: /\.html$/,
            loader: 'html'
        }, {
            test: /\.(eot|svg|ttf|woff|woff2)(\?\S*)?$/,
            loader: 'file-loader'
        }, {
            test: /\.(png|jpe?g|gif|svg)(\?\S*)?$/,
            loader: "url-loader?limit=10000"
            // }, {
            // 	test: /\.(png|jpe?g|gif|svg)(\?\S*)?$/,
            // 	loader: 'file-loader',
            // 	query: {
            // 		name: '[name].[ext]?[hash]'
            // 	}
        }]
    },
    resolve: {
        alias: {
            vue: 'vue/dist/vue.js'
        }
    },
    babel: {
        presets: ['es2015', 'stage-1']
    },
    vue: {
        preserveWhitespace: false
    },
    plugins: [
        new Webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery",
            "window.jQuery": "jquery"
        }),
        new Webpack.DefinePlugin({
            NODE_ENV: isProduction ? '"prod"' : '"dev"'
        })
    ],

	devServer: {
	   contentBase: './public/',
	   port: 18080,
       hot: true,
       inline: true,
	   // Send API requests on localhost to API server get around CORS.
	   proxy: {
		  '/statistical': {
			 target: {
				host: "127.0.0.1",
				protocol: 'http:',
				port: 8080
			 },
			 pathRewrite: {
				// '/api': ''
			 }
		  }
	   }
	}
};

module.exports = config;
