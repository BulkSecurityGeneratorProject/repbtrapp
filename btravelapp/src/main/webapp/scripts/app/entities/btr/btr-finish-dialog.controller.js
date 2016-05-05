'use strict';

angular.module('btravelappApp')
	.controller('BtrFinishController', function($scope, $uibModalInstance, entity, Btr) {

        $scope.btr = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        
        var onSaveSuccess = function (result) {
            $scope.$emit('btravelappApp:btrUpdate', result);
        	//$scope.$emit('Closed', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };
        
        $scope.confirmFinish = function (id) {
        	$scope.isSaving = true;
        	console.log($scope.btr.id); // asta imi returneaza id-ul
        	$scope.btr.status="Finished";
        	Btr.update($scope.btr, onSaveSuccess, onSaveError);
        	
        };
    });
