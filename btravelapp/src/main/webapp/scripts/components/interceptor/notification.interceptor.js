 'use strict';

angular.module('btravelappApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-btravelappApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-btravelappApp-params')});
                }
                return response;
            }
        };
    });
