'use strict';

angular.module('btravelappApp')
    .controller('BtrDetailController', function ($scope, $rootScope, $stateParams, entity, Btr, User, Expense) {
        $scope.expense = entity;
    	$scope.btr = entity;
        $scope.load = function (id) {
            Btr.get({id: id}, function(result) {
                $scope.btr = result;
            });
            // added expense
            Expense.get({id: id}, function(result) {
                $scope.expense = result;
            });
        };
        var unsubscribe = $rootScope.$on('btravelappApp:btrUpdate', function(event, result) {
            $scope.btr = result;
        });
        $scope.$on('$destroy', unsubscribe);

        /* expensses added
        $scope.loadAll = function() {
            Expense.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.expenses = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();*/
    });
