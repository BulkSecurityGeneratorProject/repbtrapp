'use strict';

angular.module('btravelappApp')
    .controller('BtrController', function ($scope, $state, Btr, BtrSearch, ParseLinks, Expense, User, Principal) {

    	//added expense
    	$scope.expenses = [];
    	
        $scope.btrs = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        
       // verific user-ul curent pentru approve/reject   
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        	

        $scope.loadAll = function() {
            Btr.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.btrs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();
        
        $scope.search = function () {
            BtrSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.btrs = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.btr = {
                status: null,
                start_date: null,
                end_date: null,
                location: null,
                center_cost: null,
                request_date: null,
                last_modified_date: null,
                id: null
            };
            // added expense
            $scope.expense = {
                    id: null,
                    expense_cost: null
                };
            
        };
      
    });

