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
        	
        };
        // added comment
	        $scope.comments = entity;
	        $scope.btrs = Btr.query();
	        $scope.load = function(id) {
	            Comments.get({id : id}, function(result) {
	                $scope.comments = result;
	            });
	        };

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
	                Comments.update($scope.comments, onSaveSuccesscomment, onSaveErrorcomment);
	            } else {
	                Comments.save($scope.comments, onSaveSuccesscomment, onSaveErrorcomment);
	            }
	        };
});
