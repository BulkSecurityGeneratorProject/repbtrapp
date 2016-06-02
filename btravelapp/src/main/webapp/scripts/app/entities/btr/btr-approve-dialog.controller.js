'use strict';

angular.module('btravelappApp').controller('BtrApproveController',
		function($scope, $uibModalInstance, $http, entity, Btr) {

			$scope.user = entity;
			$scope.btr = entity;
			$scope.clear = function() {
				$uibModalInstance.dismiss('cancel');
			};

			var onSaveSuccess = function(result) {
				$scope.$emit('btravelappApp:btrUpdate', result);
				$uibModalInstance.close(result);
				$scope.isSaving = false;
			};

			var onSaveError = function(result) {
				$scope.isSaving = false;
			};

			$scope.confirmApprove = function(id) {

				$http({
					method : 'GET',
					url : '/api/users/' + $scope.btr.assigned_to.idManager
				}).then(function successCallback(response) {
					$scope.isSaving = true;
					$scope.btr.assigned_from = $scope.btr.manager;
					$scope.btr.assigned_to = response.data;

					Btr.update($scope.btr, onSaveSuccess, onSaveError);
				}, function errorCallback(response) {

					$scope.btr.status = "Issuing ticket";
					$scope.btr.assigned_from = $scope.btr.manager;
					$scope.btr.assigned_to = $scope.btr.supplier;

					Btr.update($scope.btr, onSaveSuccess, onSaveError);
				});

			};

		});
