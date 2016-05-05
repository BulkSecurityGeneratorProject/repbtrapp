'use strict';

angular.module('btravelappApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


