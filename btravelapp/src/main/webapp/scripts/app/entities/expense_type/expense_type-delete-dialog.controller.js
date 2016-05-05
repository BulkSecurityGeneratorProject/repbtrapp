'use strict';

angular.module('btravelappApp')
	.controller('Expense_typeDeleteController', function($scope, $uibModalInstance, entity, Expense_type) {

        $scope.expense_type = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Expense_type.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
