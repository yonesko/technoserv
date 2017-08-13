'use strict';

angular.module('phonecatApp').config(['$locationProvider', '$routeProvider',
    function config($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');

        $routeProvider.when('/wordsStat', {
            templateUrl: 'wordsStat.html',
            controller: 'WordsStatController'
        }).when('/wordDetail/:phoneId', {
            templateUrl: 'wordDetail.html',
            controller: 'WordDetailController'
        }).otherwise('/wordsStat');
    }
]).controller('WordsStatController', function ($scope) {
    $scope.words = {
        'adam': 1,
        "apple": 34
    }
}).controller('WordDetailController', function ($scope, $routeParams) {
    $scope.params = $routeParams
});
