'use strict';

angular.module('btravelappApp')
    .factory('Btr', function ($resource, DateUtils) {
        return $resource('api/btrs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.start_date = DateUtils.convertDateTimeFromServer(data.start_date);
                    data.end_date = DateUtils.convertDateTimeFromServer(data.end_date);
                    data.request_date = DateUtils.convertDateTimeFromServer(data.request_date);
                    data.last_modified_date = DateUtils.convertDateTimeFromServer(data.last_modified_date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
