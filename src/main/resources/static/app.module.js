'use strict';

angular.module('words-popularity-monitor', [
    'ngRoute',
    'chart.js'
]).factory('WordsStatResource', ['$resource', function ($resource) {
    return $resource('/wordStat', {}, {})
}]);
