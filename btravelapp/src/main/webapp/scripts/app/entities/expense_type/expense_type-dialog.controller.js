'use strict';

angular.module('btravelappApp').controller('Expense_typeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Expense_type', 'Expense',
        function($scope, $stateParams, $uibModalInstance, entity, Expense_type, Expense) {

        $scope.expense_type = entity;
        $scope.expenses = Expense.query();
        $scope.load = function(id) {
            Expense_type.get({id : id}, function(result) {
                $scope.expense_type = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('btravelappApp:expense_typeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.expense_type.id != null) {
                Expense_type.update($scope.expense_type, onSaveSuccess, onSaveError);
            } else {
                Expense_type.save($scope.expense_type, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
