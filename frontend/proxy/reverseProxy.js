'use strict';

var http = require('http'),
    httpProxy = require('http-proxy'),
    HttpProxyRules = require('http-proxy-rules');

module.exports = {
    startProxy: function (proxyPort, uiServerPort, apiServerPort, authServerPort) {
        var proxyRules = new HttpProxyRules({
            rules: {
                '.*/api': 'http://localhost:' + apiServerPort + '/',
                '.*/auth': 'http://localhost:' + authServerPort + '/'
            },
            default: 'http://localhost:' + uiServerPort + '/'
        });

        var proxy = httpProxy.createProxy();

        var server = http.createServer(function (req, res) {
            var target = proxyRules.match(req);
            if (target) {
                return proxy.web(req, res, {
                    target: target
                });
            }

            res.writeHead(500, {'Content-Type': 'text/plain'});
            res.end('The request url and path did not match any of the listed rules!');
        });

        server.listen(proxyPort);

        console.log("Reverse proxy listening on port: " + proxyPort);

    }
};
