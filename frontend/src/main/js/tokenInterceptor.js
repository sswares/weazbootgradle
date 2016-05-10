'use strict';

module.exports = function ($window) {
    return {
        'request': function (config) {
            // console.log("config", config);
            // if(config.params !== undefined) {
            //     var token = config.params.tokenZ;
            //     console.log("TOKEN WAS:", token);
            //     if (token !== undefined) {
            //         $window.localStorage.setItem('tokenZ', token);
            //     }
            // }
            return config;
        }
    };
};