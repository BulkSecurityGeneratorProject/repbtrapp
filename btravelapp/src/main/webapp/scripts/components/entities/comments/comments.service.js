'use strict';

angular.module('btravelappApp')
    .factory('Comments', function ($resource, DateUtils) {
        return $resource('api/commentss/:id', {}, {
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
