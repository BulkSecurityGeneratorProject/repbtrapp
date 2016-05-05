'use strict';

angular.module('btravelappApp').controller('ExpenseDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Expense', 'Btr', 'Expense_type',
        function($scope, $stateParams, $uibModalInstance, entity, Expense, Btr, Expense_type) {

        $scope.expense = entity;
        $scope.btrs = Btr.query();
        $scope.expense_types = Expense_type.query();
        $scope.load = function(id) {
            Expense.get({id : id}, function(result) {
                $scope.expense = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('btravelappApp:expenseUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.expense.id != null) {
                Expense.update($scope.expense, onSaveSuccess, onSaveError);
            } else {
                Expense.save($scope.expense, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
