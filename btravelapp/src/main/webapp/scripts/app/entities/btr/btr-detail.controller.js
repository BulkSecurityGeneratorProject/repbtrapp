'use strict';

angular.module('btravelappApp')
    .controller('BtrDetailController', function ($scope, $rootScope, $stateParams, entity, Btr, User, Expense) {
        $scope.btr = entity;
        $scope.load = function (id) {
            Btr.get({id: id}, function(result) {
                $scope.btr = result;
            });
        };
        var unsubscribe = $rootScope.$on('btravelappApp:btrUpdate', function(event, result) {
            $scope.btr = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
