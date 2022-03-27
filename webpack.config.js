var path = require('path');

module.exports = {
    entry: {
        'app': './src/main/js/app.js'
    },
    cache: true,
    mode: 'development',
    devtool: 'source-map',
    output: {
        path: __dirname,
        filename: 'bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react",{
                            'plugins': ['@babel/plugin-proposal-class-properties']}]
                    }
                }]
            }
        ]
    }
};