(function() {
    'use strict';

    angular
        .module('btravelappApp')
        .factory('BtrSearch', BtrSearch);

    BtrSearch.$inject = ['$resource'];

    function BtrSearch($resource) {
        var resourceUrl =  'api/_search/btrs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
