'use strict';

angular.module('btravelappApp')
    .factory('Expense_typeSearch', function ($resource) {
        return $resource('api/_search/expense_types/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
