'use strict';

angular.module('words-popularity-monitor').config(['$locationProvider', '$routeProvider',
    function config($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');

        $routeProvider.when('/wordsStat', {
            templateUrl: 'wordsStat.html',
            controller: 'WordsStatController'
        }).when('/wordDetail/:word', {
            templateUrl: 'wordDetail.html',
            controller: 'WordDetailController'
        }).otherwise('/wordsStat');
    }
]).controller('WordsStatController', ['$scope', '$http', '$interval', function ($scope, $http, $interval) {
    var refresh = function () {
        $http({method: 'GET', url: "/wordStat"}).then(function (response) {

            var list = [];
            angular.forEach(response.data, function (val, key) {
                list.push({word:key, count:val});
            });

            $scope.words = list;

        }, function (response) {
        });
    };

    refresh();
    $interval(refresh, 5000);

}]).controller('WordDetailController', function ($scope, $http, $routeParams, $sce) {
    $http({method: 'GET', url: "/wordStat/detail?word=" + $routeParams.word}).then(function (response) {
        $scope.contentList = [];

        angular.forEach(response.data.contentList, function (val, key) {
            $scope.contentList.push($sce.trustAsHtml(val))
        });

        $scope.labels = [];
        $scope.data = [];
        angular.forEach(response.data.dayToTTF, function (count, day) {
            $scope.labels.push(day);
            $scope.data.push(count);
        });

    }, function (response) {
    });
});
