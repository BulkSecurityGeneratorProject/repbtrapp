'use strict';

angular.module('btravelappApp')
    .controller('HistorybtrDetailController', function ($scope, $rootScope, $stateParams, entity, Historybtr, Btr) {
        $scope.historybtr = entity;
        $scope.load = function (id) {
            Historybtr.get({id: id}, function(result) {
                $scope.historybtr = result;
            });
        };
        var unsubscribe = $rootScope.$on('btravelappApp:historybtrUpdate', function(event, result) {
            $scope.historybtr = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
