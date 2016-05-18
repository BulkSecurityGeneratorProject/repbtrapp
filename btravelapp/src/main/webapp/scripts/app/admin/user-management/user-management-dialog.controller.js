'use strict';

angular.module('btravelappApp').controller('UserManagementDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'User', 'Language',
        function($scope, $stateParams, $uibModalInstance, entity, User, Language) {

    	//added 18.05.2016
    	//$scope.managers = managers.query();
    	$scope.manager = entity;
    	
    	$scope.users = User.query(); //adaugat 11.03.2016
        $scope.user = entity;
        $scope.authorities = ["ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SUPPLIER"]; //MODIFICAT 08.03.2016
        Language.getAll().then(function (languages) {
            $scope.languages = languages;
        });
        var onSaveSuccess = function (result) {
            $scope.isSaving = false;
            $uibModalInstance.close(result);
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.user.id != null) {
                User.update($scope.user, onSaveSuccess, onSaveError);
            } else {
                User.save($scope.user, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        
     // managers login
        $scope.managers = function() {
        $http({
			method : 'GET',
			url : '/api/users/managers' 
		}).then(function successCallback(response) {
			$scope.isSaving = true;

				$scope.user.id_manager = $scope.user.id_manager;

			User.update($scope.user, onSaveSuccess, onSaveError);
		}, function errorCallback(response) {
			
				$scope.user.id_manager = $scope.user.id_manager;
				
			User.update($scope.user, onSaveSuccess, onSaveError);
		})
        };
        // validare mail
        var app = angular.module('btravelappApp', []);
        app.directive('myDirective', function() {
            return {
                require: 'ngModel',
                link: function(scope, element, attr, mCtrl) {
                    function myValidation(value) {
                        if (value.indexOf("@") > -1 && value.indexOf(".")>value.indexOf("@")) {
                            mCtrl.$setValidity('charE', true);
                        } else {
                            mCtrl.$setValidity('charE', false);
                        }
                        return value;
                    }
                    mCtrl.$parsers.push(myValidation);
                }
            };
        });
        
        
}]);
