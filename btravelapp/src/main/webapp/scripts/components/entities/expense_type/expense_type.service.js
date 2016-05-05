'use strict';

angular.module('btravelappApp')
    .factory('Expense_type', function ($resource, DateUtils) {
        return $resource('api/expense_types/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
