'use strict';

angular.module('btravelappApp').controller('HistorybtrDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Historybtr', 'Btr',
        function($scope, $stateParams, $uibModalInstance, entity, Historybtr, Btr) {

        $scope.historybtr = entity;
        $scope.btrs = Btr.query();
        $scope.load = function(id) {
            Historybtr.get({id : id}, function(result) {
                $scope.historybtr = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('btravelappApp:historybtrUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.historybtr.id != null) {
                Historybtr.update($scope.historybtr, onSaveSuccess, onSaveError);
            } else {
                Historybtr.save($scope.historybtr, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForChange_date = {};

        $scope.datePickerForChange_date.status = {
            opened: false
        };

        $scope.datePickerForChange_dateOpen = function($event) {
            $scope.datePickerForChange_date.status.opened = true;
        };
        $scope.datePickerForStart_date = {};

        $scope.datePickerForStart_date.status = {
            opened: false
        };

        $scope.datePickerForStart_dateOpen = function($event) {
            $scope.datePickerForStart_date.status.opened = true;
        };
        $scope.datePickerForEnd_date = {};

        $scope.datePickerForEnd_date.status = {
            opened: false
        };

        $scope.datePickerForEnd_dateOpen = function($event) {
            $scope.datePickerForEnd_date.status.opened = true;
        };
        $scope.datePickerForRequest_date = {};

        $scope.datePickerForRequest_date.status = {
            opened: false
        };

        $scope.datePickerForRequest_dateOpen = function($event) {
            $scope.datePickerForRequest_date.status.opened = true;
        };
        $scope.datePickerForLast_modified_date = {};

        $scope.datePickerForLast_modified_date.status = {
            opened: false
        };

        $scope.datePickerForLast_modified_dateOpen = function($event) {
            $scope.datePickerForLast_modified_date.status.opened = true;
        };
}]);
