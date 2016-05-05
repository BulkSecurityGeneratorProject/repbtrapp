'use strict';

angular.module('btravelappApp')
    .controller('HistorybtrController', function ($scope, $state, Historybtr, HistorybtrSearch, ParseLinks) {

        $scope.historybtrs = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Historybtr.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.historybtrs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            HistorybtrSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.historybtrs = result;
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
            $scope.historybtr = {
                btrstatusbefore: null,
                btrstatusafter: null,
                change_date: null,
                changed_by: null,
                start_date: null,
                end_date: null,
                assigned_to: null,
                assigned_from: null,
                location: null,
                center_cost: null,
                request_date: null,
                last_modified_date: null,
                id: null
            };
        };
    });
