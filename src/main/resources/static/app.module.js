'use strict';

angular.module('phonecatApp', [
    'ngRoute',
    'ngResource',
    'chart.js'
]).factory('WordsStatResource', ['$resource', function ($resource) {
    return $resource('/wordStat', {}, {})
}]);
