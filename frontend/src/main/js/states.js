"use strict";

module.exports = function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');

    $stateProvider.state('root', {
        url: '/',
        views: {
            '@': {
                controller: require('./controllers/rootController.js'),
                templateUrl: 'partials/root.html'
            }
        }
    });
};