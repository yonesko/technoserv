var app = angular.module('newsWordStat', ['ngRoute']);

app.controller('wordStat', function ($scope, $http) {
    $http({method: "GET", url: "/wordStat"}).then(function (response) {
        $scope.wordStat = response.data
    }, function (response) {
        $scope.data = response.data || 'Request failed';
        $scope.status = response.status;
    });
});

app.controller('RefineWordController', function ($scope, $http, $routeParams) {
    $scope.params = $routeParams;


    // $http({method: "GET", url: "/wordStat"}).then(function (response) {
    //     $scope.wordStat = response.data
    // }, function (response) {
    //     $scope.data = response.data || 'Request failed';
    //     $scope.status = response.status;
    // });
});


app.config([function ($routeProvider, $locationProvider) {
    $routeProvider.when('/wordStat/refine/:word', {
        templateUrl: 'refine.html',
        controller: 'RefineWordController'
    });
}]);