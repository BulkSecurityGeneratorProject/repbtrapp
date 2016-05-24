'use strict';

angular.module('btravelappApp')
    .controller('ExpenseDetailController', function ($scope, $rootScope, $stateParams, entity, Expense, Btr, Expense_type) {
        $scope.expense = entity;
        $scope.load = function (id) {
        	$http({
        		method : 'GET',
        		url: '/api/expenses/' + $scope.expense.id
        	}).then(function successCallback(response){
                //Expense.query({id: id}, function(result) {
                	Expense.get({id: id}, function(result) {
                        $scope.expense = result;
                    });
        		
        	}, function errorCallback(response){
        		
        	});
        	

        };
        var unsubscribe = $rootScope.$on('btravelappApp:expenseUpdate', function(event, result) {
            $scope.expense = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
