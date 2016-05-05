'use strict';

angular.module('btravelappApp')
    .factory('Historybtr', function ($resource, DateUtils) {
        return $resource('api/historybtrs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.change_date = DateUtils.convertDateTimeFromServer(data.change_date);
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
