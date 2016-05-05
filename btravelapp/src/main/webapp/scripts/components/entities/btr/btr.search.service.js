'use strict';

angular.module('btravelappApp')
    .factory('BtrSearch', function ($resource) {
        return $resource('api/_search/btrs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
