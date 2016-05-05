'use strict';

angular.module('btravelappApp')
    .factory('HistorybtrSearch', function ($resource) {
        return $resource('api/_search/historybtrs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
