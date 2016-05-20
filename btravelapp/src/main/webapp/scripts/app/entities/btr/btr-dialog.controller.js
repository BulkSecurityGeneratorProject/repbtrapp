'use strict';

angular.module('btravelappApp').controller('BtrDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Btr', 'User', 'Expense',
        function($scope, $stateParams, $uibModalInstance, entity, Btr, User, Expense) {

    	//$scope.authorities = ["ROLE_MANAGER", "ROLE_SUPPLIER"]; // adaugat 31.03.2016
        $scope.btr = entity;
        $scope.users = User.query();
        $scope.user = entity;
        $scope.expenses = Expense.query();
        $scope.load = function(id) {
            Btr.get({id : id}, function(result) {
                $scope.btr = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('btravelappApp:btrUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.btr.id != null) {
                Btr.update($scope.btr, onSaveSuccess, onSaveError);
            } else {
                Btr.save($scope.btr, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
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
        
        // supplier list   
        $scope.suppliers = function() {
        	$http({
        		method : 'GET',
        		url: 'api/users/supplier/' + $scope.user.id
        	}).then(function successCallback(response){
        		$scope.isSaving = true;
        		$scope.btr.assigned_to = response.data;        		
        	}, function errorCallback(response){
        		
        	});
        };
        
        // manager's employees
        $scope.employees = function() {
        	$http({
        		method : 'GET',
        		url: 'api/users/employees/' + $scope.btr.user.id
        	}).then(function successCallback(response){
        		$scope.isSaving = true;
        		$scope.btr.user = response.data;        		
        	}, function errorCallback(response){
        		
        	});
        };
}]);
