'use strict';

var http = require('http'),
    httpProxy = require('http-proxy'),
    HttpProxyRules = require('http-proxy-rules');

module.exports = {
    startProxy: function (proxyPort, uiServerPort, apiServerPort, authServerPort) {
        var proxyRules = new HttpProxyRules({
            rules: {
                '.*/apix': 'http://127.0.0.1:' + apiServerPort + '/',
                '.*/auth': 'http://127.0.0.1:' + authServerPort + '/auth/'
            },
            default: 'http://127.0.0.1:' + uiServerPort + '/'
        });

        var proxy = httpProxy.createProxy({agent: new http.Agent({maxSockets: Number.MAX_VALUE})});

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

        process.on('uncaughtException', function (e) {
            console.log("Exception thrown\n", e);
        });

        console.log("Reverse proxy listening on port: " + proxyPort + "\n");
    }
};
