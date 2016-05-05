'use strict';

angular.module('btravelappApp')
	.controller('BtrCloseController', function($scope, $uibModalInstance, entity, Btr) {

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
        
        $scope.confirmClose = function (id) {
        	$scope.isSaving = true;
        	console.log(id);			// asta imi returneaza undefined
        	console.log($scope.btr.id); // asta imi returneaza id-ul
        	$scope.btr.status="Closed";
        	Btr.update($scope.btr, onSaveSuccess, onSaveError);
        	
        };
        
        
        /*	 AFTER 2

        $scope.confirmClose = function () {
            $scope.isSaving = true;
            $scope.btr.status="Closed";
            if ($scope.btr.stauts != "Closed")
                Btr.update($scope.btr.status, onSaveSuccess, onSaveError);
        };
        
        	AFTER
        $scope.confirmClose = function (id) {
            Btr.close({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
*/
    });
