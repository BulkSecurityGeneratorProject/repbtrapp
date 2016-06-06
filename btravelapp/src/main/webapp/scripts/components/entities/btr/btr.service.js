'use strict';

angular.module('btravelappApp')
    .factory('Btr', function ($resource, DateUtils) {
        return $resource('api/btrs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                	console.log(data);
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
angular.module('btravelappApp')
	.factory('BtrInitiated', function($http){
		return  {
    	// btr "initiated"
    	getAllInitiatedBtrs : function(){
    	    return $http.get('api/btrs/initiated').then(function(response) {
    	        return response.data;
    	    });
    	 },
		
		// btrs for admin
    	getAllBtrsForAdmin : function(){
    	    return $http.get('api/btrs/admins').then(function(response) {
    	        return response.data;
    	    });
    	 }
		};
	})

