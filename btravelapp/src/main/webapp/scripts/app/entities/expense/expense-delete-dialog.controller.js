'use strict';

angular.module('btravelappApp')
	.controller('ExpenseDeleteController', function($scope, $uibModalInstance, entity, Expense) {

        $scope.expense = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Expense.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
