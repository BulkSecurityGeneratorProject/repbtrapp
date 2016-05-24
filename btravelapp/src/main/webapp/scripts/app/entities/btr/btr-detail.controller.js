'use strict';

angular.module('btravelappApp')
    .controller('BtrDetailController', function ($scope, $rootScope, $stateParams, $http, entity, Btr, User, Expense) {
        $scope.expense = entity;
    	$scope.btr = entity;
    	//console.log($scope.btr.expenses.length);
        $scope.load = function (id) {
            Btr.get({id: id}, function(result) {
                $scope.btr = result;
            });
            
        };
        var unsubscribe = $rootScope.$on('btravelappApp:btrUpdate', function(event, result) {
            $scope.btr = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.loadAll = function() {
        	$http({
        		method : 'GET',
        		url: '/api/btrs/' + $scope.btr.id
        	}).then(function successCallback(response){
        		$scope.isSaving = true;
        		$scope.btr.expenses = response.data;
        		//console.log($scope.response.data);
        		
        	}, function errorCallback(response){
        		
        	});
        };
        $scope.loadAll();
        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

    });

