(function() {
    'use strict';

    angular
        .module('btravelappApp')
        .controller('BtrDialogController', BtrDialogController);

    BtrDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Btr', 'User', 'Expense'];

    function BtrDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Btr, User, Expense) {
        var vm = this;
        vm.btr = entity;
        vm.users = User.query();
        vm.expenses = Expense.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('btravelappApp:btrUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.btr.id !== null) {
                Btr.update(vm.btr, onSaveSuccess, onSaveError);
            } else {
                Btr.save(vm.btr, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.start_date = false;
        vm.datePickerOpenStatus.end_date = false;
        vm.datePickerOpenStatus.request_date = false;
        vm.datePickerOpenStatus.last_modified_date = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
