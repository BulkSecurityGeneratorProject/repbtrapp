'use strict';

angular.module('btravelappApp')
    .factory('ExpenseSearch', function ($resource) {
        return $resource('api/_search/expenses/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
