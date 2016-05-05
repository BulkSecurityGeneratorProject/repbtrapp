'use strict';

angular.module('btravelappApp')
	.controller('BtrRejectController', function($scope, $uibModalInstance, entity, Btr) {

        $scope.btr = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        
        var onSaveSuccess = function (result) {
            $scope.$emit('btravelappApp:btrUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };
        
        $scope.confirmReject = function (id) {
        	$scope.isSaving = true;
        	console.log($scope.btr.id); 
        	$scope.btr.status="Initiated";
        	$scope.btr.assigned_to=$scope.btr.supplier;
        	$scope.btr.assigned_from=$scope.btr.login;
        	Btr.update($scope.btr, onSaveSuccess, onSaveError);
        	
        };
        
    });
