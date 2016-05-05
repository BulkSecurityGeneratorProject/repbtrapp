'use strict';

angular.module('btravelappApp')
    .controller('Expense_typeDetailController', function ($scope, $rootScope, $stateParams, entity, Expense_type, Expense) {
        $scope.expense_type = entity;
        $scope.load = function (id) {
            Expense_type.get({id: id}, function(result) {
                $scope.expense_type = result;
            });
        };
        var unsubscribe = $rootScope.$on('btravelappApp:expense_typeUpdate', function(event, result) {
            $scope.expense_type = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
