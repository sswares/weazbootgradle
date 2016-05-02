"use strict";

module.exports = function ($stateProvider, $urlRouterProvider, $httpProvider) {
    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state('root', {
            url: '/',
            views: {
                '@': {
                    controller: require('./controllers/rootController.js'),
                    templateUrl: 'partials/root.html'
                }
            }
        })
        .state('resource', {
            url: '/resource',
            views: {
                '@': {
                    controller: require('./controllers/resourceController.js'),
                    templateUrl: 'partials/resource.html'
                }
            }
        })
    ;
};