'use strict';

angular.module('btravelappApp')
	.controller('BtrRejectController', function($scope, $uibModalInstance, entity, Btr, Comments) {

		$scope.comments = entity;
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
        	
        	// comment save/update
        	var onSaveSuccesscomment = function (result) {
	            $scope.$emit('btravelappApp:commentsUpdate', result);
	            $uibModalInstance.close(result);
	            $scope.isSaving = false;
	        };

	        var onSaveErrorcomment = function (result) {
	            $scope.isSaving = false;
	        };

	        $scope.save = function () {
	            $scope.isSaving = true;
	            if ($scope.comments.id != null) {
	            	$scope.btr.comments.comment=$scope.btr.comments.comment;
	                Comments.update($scope.comments, onSaveSuccesscomment, onSaveErrorcomment);
	            } else {
	            	$scope.btr.comments.btr=$scope.btr.id;
	            	$scope.btr.comments.comment=$scope.btr.comments.comment;
	                Comments.save($scope.comments, onSaveSuccesscomment, onSaveErrorcomment);
	            }
	        };
        	 
        };        
});
