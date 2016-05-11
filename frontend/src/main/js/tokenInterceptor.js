'use strict';

module.exports = function ($window) {
    return {
        'request': function (config) {
            var tokenX = $window.localStorage.getItem('tokenX');
            if (tokenX !== undefined) {
                config.headers.Authorization = 'Bearer ' + tokenX;
            }
            return config;
        }
    };
};